package ai.stapi.formapi.formmapper;

import ai.stapi.formapi.formmapper.fixtures.FormApiTestDefinitionsLoader;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionProvider;
import ai.stapi.graphsystem.systemfixtures.model.SystemModelDefinitionsLoader;
import ai.stapi.test.schemaintegration.SchemaIntegrationTestCase;
import ai.stapi.test.schemaintegration.StructureDefinitionScope;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@StructureDefinitionScope({FormApiTestDefinitionsLoader.SCOPE, SystemModelDefinitionsLoader.SCOPE})
class JsonSchemaMapperTest extends SchemaIntegrationTestCase {

  @Autowired
  private OperationDefinitionProvider operationDefinitionProvider;

  @Autowired
  private JsonSchemaMapper jsonSchemaMapper;

  @Test
  void itShouldReturnEmptySchemaForOperationDefinitionWithoutParameters() {
    var operationDefinition = this.operationDefinitionProvider.provide("TestEmptyCommand");
    var jsonSchema = this.jsonSchemaMapper.map(operationDefinition);
    this.thenObjectApproved(jsonSchema);
  }

  @Test
  void itShouldReturnSchemaForOperationDefinitionWithStringParameter() {
    var operationDefinition = this.operationDefinitionProvider.provide("TestSimpleCommand");
    var jsonSchema = this.jsonSchemaMapper.map(operationDefinition);
    this.thenObjectApproved(jsonSchema);
  }

  @Test
  void itShouldReturnSchemaForOperationDefinitionWithOptionalStringParameter() {
    var operationDefinition = this.operationDefinitionProvider.provide("TestOptionalSimpleCommand");
    var jsonSchema = this.jsonSchemaMapper.map(operationDefinition);
    this.thenObjectApproved(jsonSchema);
  }

  @Test
  void itShouldReturnSchemaForOperationDefinitionWithMultiplePrimitiveParameters() {
    var operationDefinition = this.operationDefinitionProvider.provide("TestMultipleSimpleCommand");
    var jsonSchema = this.jsonSchemaMapper.map(operationDefinition);
    this.thenObjectApproved(jsonSchema);
  }

  @Test
  void itShouldReturnSchemaForOperationDefinitionWithNonNativePrimitiveParameters() {
    var operationDefinition = this.operationDefinitionProvider.provide("TestNonNativeSimpleCommand");
    var jsonSchema = this.jsonSchemaMapper.map(operationDefinition);
    this.thenObjectApproved(jsonSchema);
  }

  @Test
  void itShouldReturnSchemaForOperationDefinitionWithArrayPrimitiveParameters() {
    var operationDefinition = this.operationDefinitionProvider.provide("TestArrayCommand");
    var jsonSchema = this.jsonSchemaMapper.map(operationDefinition);
    this.thenObjectApproved(jsonSchema);
  }

  @Test
  void itShouldReturnSchemaForOperationDefinitionWithComplexParameter() {
    var operationDefinition = this.operationDefinitionProvider.provide("TestObjectCommand");
    var jsonSchema = this.jsonSchemaMapper.map(operationDefinition);
    this.thenObjectApproved(jsonSchema);
  }

  @Test
  void itShouldReturnSchemaForOperationDefinitionWithUnionType() {
    var operationDefinition = this.operationDefinitionProvider.provide("TestUnionCommand");
    var jsonSchema = this.jsonSchemaMapper.map(operationDefinition);
    this.thenObjectApproved(jsonSchema);
  }

  @Test
  void itShouldReturnSchemaForOperationDefinitionWithReference() {
    var operationDefinition = this.operationDefinitionProvider.provide("TestReferenceCommand");
    var jsonSchema = this.jsonSchemaMapper.map(operationDefinition);
    this.thenObjectApproved(jsonSchema);
  }

  @Test
  void itShouldReturnSchemaForOperationDefinitionWithReferenceToMultipleResources() {
    var operationDefinition = this.operationDefinitionProvider.provide("TestMultipleReferenceCommand");
    var jsonSchema = this.jsonSchemaMapper.map(operationDefinition);
    this.thenObjectApproved(jsonSchema);
  }

  @Test
  void itShouldReturnSchemaForCreateStructureDefinition() {
    var operationDefinition = this.operationDefinitionProvider.provide("CreateStructureDefinition");
    var jsonSchema = this.jsonSchemaMapper.map(operationDefinition);
    this.thenObjectApproved(jsonSchema);
  }
}