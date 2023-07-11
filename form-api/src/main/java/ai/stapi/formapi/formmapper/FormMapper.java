package ai.stapi.formapi.formmapper;

import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import java.util.Map;
import org.everit.json.schema.ObjectSchema;
import org.springframework.stereotype.Service;

@Service
public class FormMapper {
  
    private final StructureSchemaFinder structureSchemaFinder;

    public FormMapper(StructureSchemaFinder structureSchemaFinder) {
        this.structureSchemaFinder = structureSchemaFinder;
    }

    public Map<String, Object> map(OperationDefinitionDTO operationDefinitionDTO) {
        
        return new ObjectSchema.Builder().unprocessedProperties;
    }
}
