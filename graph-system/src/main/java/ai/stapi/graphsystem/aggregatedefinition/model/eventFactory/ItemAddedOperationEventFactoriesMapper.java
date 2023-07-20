package ai.stapi.graphsystem.aggregatedefinition.model.eventFactory;

import ai.stapi.graphsystem.aggregatedefinition.model.CommandHandlerDefinitionDTO.EventFactory;
import ai.stapi.graphsystem.aggregatedefinition.model.CommandHandlerDefinitionDTO.EventFactory.EventFactoryModification;
import ai.stapi.graphsystem.eventdefinition.EventMessageDefinitionData;
import ai.stapi.graphsystem.operationdefinition.model.FieldDefinitionWithSource;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionStructureTypeMapper;
import ai.stapi.graphsystem.operationdefinition.model.resourceStructureTypeOperationsMapper.OperationDefinitionParameters;
import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structuredefinition.StructureDefinitionId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ItemAddedOperationEventFactoriesMapper implements OperationEventFactoriesMapper {

  private final OperationDefinitionStructureTypeMapper mapper;

  public ItemAddedOperationEventFactoriesMapper(OperationDefinitionStructureTypeMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public List<EventFactory> map(OperationDefinitionDTO operationDefinition) {
    var resourceName = operationDefinition.getResource().get(0);
    var itemAddedEventId = this.createItemAddedEventId(operationDefinition.getId());
    var itemAddedEventName = this.createItemAddedEventName(operationDefinition.getId());
    var fakedStructure = this.mapper.map(operationDefinition);
    var idParameter = fakedStructure.getAllFields().values().stream()
        .map(FieldDefinitionWithSource.class::cast)
        .filter(field -> field.getLastSourcePath().equals("id"))
        .findFirst();
    
    return List.of(
        new EventFactory(
            new EventMessageDefinitionData(
                itemAddedEventId,
                itemAddedEventName,
                new StructureDefinitionId(resourceName),
                "Generated Event for adding on " + resourceName + "."
            ),
            fakedStructure.getAllFields().values().stream()
                .map(FieldDefinitionWithSource.class::cast)
                .filter(field -> !field.getLastSourcePath().equals("id"))
                .map(fieldDefinition -> this.createModification(
                    this.createModificationPath(fieldDefinition, idParameter),
                    idParameter.map(FieldDefinition::getName).orElse(null),
                    fieldDefinition.getName()
                )).toList()
        )
    );
  }

  @Override
  public List<EventFactoryModificationResult> mapParameters(
      OperationDefinitionParameters operationDefinitionParameters
  ) {
    return List.of();
  }

  private String createModificationPath(
      FieldDefinitionWithSource fieldDefinition,
      Optional<FieldDefinitionWithSource> idParameter
  ) {
    var source = fieldDefinition.getSource().split("\\.");
    var startIndex = idParameter
        .map(fieldDefinitionWithSource -> fieldDefinitionWithSource.getSourceLength() - 1)
        .orElse(1);
    
    return String.join(".", Arrays.copyOfRange(source, startIndex, source.length));
  }

  private EventFactoryModification createModification(
      String sourcePath,
      String startIdParameterName,
      String parameterName
  ) {
    return EventFactoryModification.add(
        sourcePath,
        startIdParameterName,
        parameterName
    );
  }

  private String createItemAddedEventId(String operationName) {
    var prefix = "Add";
    var suffix = "Added";

    // Check if input string starts with "Add"
    if (operationName.startsWith(prefix)) {
      // If so, remove the "Add" prefix and append the "Added" suffix
      return operationName.substring(prefix.length()) + suffix;
    } else {
      // If not, simply append the "Added" suffix to the input string
      return operationName + suffix;
    }
  }

  private String createItemAddedEventName(String operationName) {
    var prefix = "Add";
    var suffix = "Added";

    String substring = operationName;
    // Check if input string starts with "Add"
    if (operationName.startsWith(prefix)) {
      // If so, remove the "Add" prefix and append the "Added" suffix
      substring = operationName.substring(prefix.length());
    }
    var parts = substring.split("On");
    return String.format(
        "%s %s",
        String.join(" on ", parts),
        suffix
    );
  }
}
