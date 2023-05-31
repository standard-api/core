package ai.stapi.schema.structuredefinition.loader;

import ai.stapi.schema.adHocLoaders.GenericAdHocModelDefinitionsLoader;
import ai.stapi.schema.scopeProvider.ScopeCacher;
import ai.stapi.schema.scopeProvider.ScopeOptions;
import ai.stapi.schema.structureSchema.AbstractStructureType;
import ai.stapi.schema.structuredefinition.RawStructureDefinitionData;
import ai.stapi.schema.structuredefinition.StructureDefinitionData;
import ai.stapi.schema.structuredefinition.StructureDefinitionNormalizer;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class AdHocStructureDefinitionLoader implements StructureDefinitionLoader {

  private final GenericAdHocModelDefinitionsLoader genericAdHocModelDefinitionsLoader;
  private final ScopeCacher scopeCacher;

  public AdHocStructureDefinitionLoader(
      GenericAdHocModelDefinitionsLoader genericAdHocModelDefinitionsLoader,
      ScopeCacher scopeCacher
  ) {
    this.genericAdHocModelDefinitionsLoader = genericAdHocModelDefinitionsLoader;
    this.scopeCacher = scopeCacher;
  }

  @Override
  public List<StructureDefinitionData> load() {
    return this.scopeCacher.getCachedOrCompute(
        AdHocStructureDefinitionLoader.class,
        this::load
    );
  }

  private List<StructureDefinitionData> load(ScopeOptions scopeOptions) {
    var rawStructure = this.genericAdHocModelDefinitionsLoader.load(
            scopeOptions,
            RawStructureDefinitionData.SERIALIZATION_TYPE,
            RawStructureDefinitionData.class
        ).stream()
        .map(StructureDefinitionNormalizer::normalize)
        .toList();

    var structureDefinition = this.genericAdHocModelDefinitionsLoader.load(
        scopeOptions,
        StructureDefinitionData.SERIALIZATION_TYPE,
        StructureDefinitionData.class
    );

    var finalStructures = new ArrayList<>(rawStructure);
    finalStructures.addAll(structureDefinition);
    
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
}
