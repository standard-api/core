package ai.stapi.formapi.formmapper;

import ai.stapi.formapi.formmapper.fixtures.FormApiTestDefinitionsLoader;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionProvider;
import ai.stapi.graphsystem.systemfixtures.model.SystemModelDefinitionsLoader;
import ai.stapi.test.schemaintegration.SchemaIntegrationTestCase;
import ai.stapi.test.schemaintegration.StructureDefinitionScope;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@StructureDefinitionScope({FormApiTestDefinitionsLoader.SCOPE, SystemModelDefinitionsLoader.SCOPE})
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

  @Test
  void itShouldReturnSchemaForOperationDefinitionWithOptionalStringParameter() {
    var operationDefinition = this.operationDefinitionProvider.provide("TestOptionalSimpleCommand");
    var formSchema = this.formMapper.map(operationDefinition);
    this.thenObjectApproved(formSchema);
  }

  @Test
  void itShouldReturnSchemaForOperationDefinitionWithMultiplePrimitiveParameters() {
    var operationDefinition = this.operationDefinitionProvider.provide("TestMultipleSimpleCommand");
    var formSchema = this.formMapper.map(operationDefinition);
    this.thenObjectApproved(formSchema);
  }

  @Test
  void itShouldReturnSchemaForOperationDefinitionWithNonNativePrimitiveParameters() {
    var operationDefinition = this.operationDefinitionProvider.provide("TestNonNativeSimpleCommand");
    var formSchema = this.formMapper.map(operationDefinition);
    this.thenObjectApproved(formSchema);
  }

  @Test
  void itShouldReturnSchemaForOperationDefinitionWithArrayPrimitiveParameters() {
    var operationDefinition = this.operationDefinitionProvider.provide("TestArrayCommand");
    var formSchema = this.formMapper.map(operationDefinition);
    this.thenObjectApproved(formSchema);
  }

  @Test
  void itShouldReturnSchemaForOperationDefinitionWithComplexParameter() {
    var operationDefinition = this.operationDefinitionProvider.provide("TestObjectCommand");
    var formSchema = this.formMapper.map(operationDefinition);
    this.thenObjectApproved(formSchema);
  }

  @Test
  void itShouldReturnSchemaForOperationDefinitionWithUnionType() {
    var operationDefinition = this.operationDefinitionProvider.provide("TestUnionCommand");
    var formSchema = this.formMapper.map(operationDefinition);
    this.thenObjectApproved(formSchema);
  }

  @Test
  void itShouldReturnSchemaForCreateStructureDefinition() {
    var operationDefinition = this.operationDefinitionProvider.provide("CreateStructureDefinition");
    var formSchema = this.formMapper.map(operationDefinition);
    this.thenObjectApproved(formSchema);
  }
}