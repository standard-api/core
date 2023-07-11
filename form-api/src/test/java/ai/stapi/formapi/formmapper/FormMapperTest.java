package ai.stapi.formapi.formmapper;

import ai.stapi.formapi.formmapper.fixtures.FormApiTestDefinitionsLoader;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionProvider;
import ai.stapi.test.schemaintegration.SchemaIntegrationTestCase;
import ai.stapi.test.schemaintegration.StructureDefinitionScope;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@StructureDefinitionScope(FormApiTestDefinitionsLoader.SCOPE)
class FormMapperTest extends SchemaIntegrationTestCase {
  
  @Autowired
  private OperationDefinitionProvider operationDefinitionProvider;
  
  @Autowired
  private FormMapper formMapper;

  @Test
  void itShouldReturnEmptySchemaForOperationDefinitionWithoutParameters() {
    var operationDefinition = this.operationDefinitionProvider.provide("TestEmptyCommand");
    var formSchema = this.formMapper.map(operationDefinition);
    this.thenObjectApproved(formSchema);
  }

  @Test
  void itShouldReturnSchemaForOperationDefinitionWithStringParameter() {
    var operationDefinition = this.operationDefinitionProvider.provide("TestSimpleCommand");
    var formSchema = this.formMapper.map(operationDefinition);
    this.thenObjectApproved(formSchema);
  }

}