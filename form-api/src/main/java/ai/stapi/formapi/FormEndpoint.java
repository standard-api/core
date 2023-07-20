package ai.stapi.formapi;

import ai.stapi.formapi.formmapper.FormDataLoader;
import ai.stapi.formapi.formmapper.JsonSchemaMapper;
import ai.stapi.formapi.formmapper.UISchemaLoader;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionProvider;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FormEndpoint {

  private final JsonSchemaMapper jsonSchemaMapper;
  private final UISchemaLoader uiSchemaLoader;
  private FormDataLoader formDataLoader;
  private final OperationDefinitionProvider operationDefinitionProvider;

  public FormEndpoint(
      JsonSchemaMapper jsonSchemaMapper,
      UISchemaLoader uiSchemaLoader,
      FormDataLoader formDataLoader,
      OperationDefinitionProvider operationDefinitionProvider
  ) {
    this.jsonSchemaMapper = jsonSchemaMapper;
    this.uiSchemaLoader = uiSchemaLoader;
    this.formDataLoader = formDataLoader;
    this.operationDefinitionProvider = operationDefinitionProvider;
  }

  @GetMapping("/form/{operationId}/")
  @ResponseBody
  public Map<String, Object> form(
      @PathVariable String operationId,
      @PathVariable String resourceId,
      @PathVariable String startModificationNodeIdAndType
  ) {
    var operation = this.operationDefinitionProvider.provide(operationId);

    return Map.of(
        "formSchema", this.jsonSchemaMapper.map(operation),
        "uiSchema", this.uiSchemaLoader.load(operation),
        "formData", this.formDataLoader.load(operation, resourceId, startModificationNodeIdAndType)
    );
  }
}
