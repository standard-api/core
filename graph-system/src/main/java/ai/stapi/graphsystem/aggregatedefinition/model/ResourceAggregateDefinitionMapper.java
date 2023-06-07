package ai.stapi.graphsystem.aggregatedefinition.model;

import ai.stapi.schema.structureSchema.ResourceStructureType;
import ai.stapi.schema.structuredefinition.StructureDefinitionId;

public class ResourceAggregateDefinitionMapper {

  public AggregateDefinitionDTO map(ResourceStructureType resourceStructureType) {
    var resourceName = resourceStructureType.getDefinitionType();
    return new AggregateDefinitionDTO(
        String.format("%sAggregate", resourceName),
        resourceName,
        resourceStructureType.getDescription(),
        new StructureDefinitionId(resourceName)
    );
  }
}
