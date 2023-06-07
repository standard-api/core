package ai.stapi.graphsystem.operationdefinition.model.resourceStructureTypeOperationsMapper;

import ai.stapi.schema.structureSchema.ComplexStructureType;
import ai.stapi.schema.structureSchema.ResourceStructureType;
import java.util.List;
import java.util.Map;

public interface ResourceOperationsMapper {

  ResourceOperationsMapperResult map(ResourceStructureType resourceStructureType);

  Map<String, ResourceOperationsMapperResult> mapNewFields(
      ComplexStructureType modifiedStructureType,
      List<String> newFieldNames
  );
}
