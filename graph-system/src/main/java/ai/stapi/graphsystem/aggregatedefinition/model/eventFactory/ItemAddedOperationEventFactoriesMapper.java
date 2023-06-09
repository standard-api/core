package ai.stapi.graphsystem.aggregatedefinition.model.eventFactory;

import ai.stapi.graphsystem.aggregatedefinition.model.CommandHandlerDefinitionDTO.EventFactory;
import ai.stapi.graphsystem.aggregatedefinition.model.CommandHandlerDefinitionDTO.EventFactory.EventFactoryModification;
import ai.stapi.graphsystem.eventdefinition.EventMessageDefinitionData;
import ai.stapi.graphsystem.operationdefinition.model.FieldDefinitionWithSource;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionStructureTypeMapper;
import ai.stapi.graphsystem.operationdefinition.model.resourceStructureTypeOperationsMapper.OperationDefinitionParameters;
import ai.stapi.schema.structuredefinition.StructureDefinitionId;
import java.util.List;

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
                    fieldDefinition.getSource(),
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

  private EventFactoryModification createModification(
      String sourcePath,
      String parameterName
  ) {
    return EventFactoryModification.add(
        sourcePath,
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
