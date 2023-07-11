package ai.stapi.formapi;

import ai.stapi.formapi.formmapper.JsonSchemaMapper;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionProvider;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FormEndpoint {
  
  private final JsonSchemaMapper jsonSchemaMapper;
  private final OperationDefinitionProvider operationDefinitionProvider;

  public FormEndpoint(
      JsonSchemaMapper jsonSchemaMapper, 
      OperationDefinitionProvider operationDefinitionProvider
  ) {
    this.jsonSchemaMapper = jsonSchemaMapper;
    this.operationDefinitionProvider = operationDefinitionProvider;
  }

  @GetMapping("/form/{operationId}")
  @ResponseBody
  public Map<String, Object> form(@PathVariable String operationId) {
    var operation = this.operationDefinitionProvider.provide(operationId);
    return this.jsonSchemaMapper.map(operation);
  }
}
