package ai.stapi.graphsystem.structuredefinition.loader;

import ai.stapi.graphsystem.structuredefinition.command.importStructureDefinitionFromSource.ImportStructureDefinition;
import ai.stapi.schema.adHocLoaders.GenericAdHocModelDefinitionsLoader;
import ai.stapi.schema.scopeProvider.ScopeCacher;
import ai.stapi.schema.scopeProvider.ScopeOptions;
import ai.stapi.schema.structureSchema.AbstractStructureType;
import ai.stapi.schema.structuredefinition.ElementDefinition;
import ai.stapi.schema.structuredefinition.RawStructureDefinitionData;
import ai.stapi.schema.structuredefinition.StructureDefinitionData;
import ai.stapi.schema.structuredefinition.StructureDefinitionNormalizer;
import ai.stapi.schema.structuredefinition.loader.StructureDefinitionLoader;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class SystemAdHocStructureDefinitionLoader implements StructureDefinitionLoader {

  private final GenericAdHocModelDefinitionsLoader genericAdHocModelDefinitionsLoader;
  private final ObjectMapper objectMapper;
  private final ScopeCacher scopeCacher;

  public SystemAdHocStructureDefinitionLoader(
      GenericAdHocModelDefinitionsLoader genericAdHocModelDefinitionsLoader,
      ObjectMapper objectMapper,
      ScopeCacher scopeCacher
  ) {
    this.genericAdHocModelDefinitionsLoader = genericAdHocModelDefinitionsLoader;
    this.objectMapper = objectMapper;
    this.scopeCacher = scopeCacher;
  }

  @Override
  public List<StructureDefinitionData> load() {
    return this.scopeCacher.getCachedOrCompute(
        SystemAdHocStructureDefinitionLoader.class,
        this::load
    );
  }

  private List<StructureDefinitionData> load(ScopeOptions scopeOptions) {
    var importStructure = this.genericAdHocModelDefinitionsLoader.load(
            scopeOptions,
            ImportStructureDefinition.SERIALIZATION_TYPE,
            RawStructureDefinitionData.class
        ).stream()
        .map(StructureDefinitionNormalizer::normalize)
        .toList();

    var createStructure = this.genericAdHocModelDefinitionsLoader.load(
            scopeOptions,
            "CreateStructureDefinition",
            RawStructureDefinitionData.class
        ).stream()
        .map(StructureDefinitionNormalizer::normalize)
        .toList();

    var structureDefinition = this.genericAdHocModelDefinitionsLoader.load(
        scopeOptions,
        StructureDefinitionData.SERIALIZATION_TYPE,
        StructureDefinitionData.class
    );

    var finalStructures = new ArrayList<>(importStructure);
    finalStructures.addAll(createStructure);
    finalStructures.addAll(structureDefinition);

    var addElement = this.genericAdHocModelDefinitionsLoader.load(
            scopeOptions,
            "AddElementOnStructureDefinitionDifferential"
        ).stream()
        .map(add -> this.fixAddElement(add, finalStructures))
        .toList();

    finalStructures.addAll(addElement);
    return this.sortDefinitionsByKind(finalStructures);
  }
  @NotNull
  private ArrayList<StructureDefinitionData> sortDefinitionsByKind(
      List<StructureDefinitionData> definitionDTOs
  ) {
    var sortedList = new ArrayList<StructureDefinitionData>();
    sortedList.addAll(
        definitionDTOs.stream()
            .filter(dto -> dto.getKind().equals(AbstractStructureType.PRIMITIVE_TYPE))
            .toList()
    );
    sortedList.addAll(
        definitionDTOs.stream()
            .filter(dto -> dto.getKind().equals(AbstractStructureType.COMPLEX_TYPE))
            .toList()
    );
    sortedList.addAll(
        definitionDTOs.stream()
            .filter(dto -> dto.getKind().equals(AbstractStructureType.RESOURCE))
            .toList()
    );
    return sortedList;
  }

  private StructureDefinitionData fixAddElement(
      HashMap addElementMap,
      ArrayList<StructureDefinitionData> finalStructures
  ) {
    var id = (String) addElementMap.get("id");

    var parentStructures = finalStructures.stream()
        .filter(structure -> structure.getId().equals(id))
        .toList();

    if (parentStructures.isEmpty()) {
      throw new RuntimeException(
          String.format(
              "You are trying to add ElementDefinition to StructureDefinition with id '%s'. "
                  + "But it cant be found in requested scope.",
              id
          )
      );
    }
    var element = addElementMap.get("element");
    ArrayList<ElementDefinition> castedElement = this.objectMapper.convertValue(
        element,
        new TypeReference<>() {
        }
    );

    var parentStructure = parentStructures.get(0);
    return new StructureDefinitionData(
        id,
        parentStructure.getUrl(),
        parentStructure.getStatus(),
        parentStructure.getDescription(),
        parentStructure.getKind(),
        parentStructure.getIsAbstract(),
        parentStructure.getType(),
        parentStructure.getBaseDefinition(),
        parentStructure.getBaseDefinitionReference(),
        castedElement
    );
  }
}
