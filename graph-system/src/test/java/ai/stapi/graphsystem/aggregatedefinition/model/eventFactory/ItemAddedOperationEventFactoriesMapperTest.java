package ai.stapi.graphsystem.aggregatedefinition.model.eventFactory;

import ai.stapi.test.SystemSchemaIntegrationTestCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ai.stapi.graphsystem.operationdefinition.infrastructure.AdHocOperationDefinitionProvider;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ItemAddedOperationEventFactoriesMapperTest extends SystemSchemaIntegrationTestCase {

  @Autowired
  private ItemAddedOperationEventFactoriesMapper mapper;

  @Autowired
  private AdHocOperationDefinitionProvider provider;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void itCanMapFirstLevelListFieldAddOperation() {
    var operationName = "AddJurisdictionOnOperationDefinition";
    this.testMapper(operationName);
  }

  @Test
  void itCanMapListFieldOnLeafComplexFieldAddOperation() {
    var operationName = "AddElementOnStructureDefinitionDifferential";
    this.testMapper(operationName);
  }

  @Test
  void itCanMapListFieldOnListComplexFieldAddOperation() {
    var operationName = "AddCodingOnOperationDefinitionJurisdiction";
    this.testMapper(operationName);
  }

  @Test
  void itCanMapListUnionFieldAddOperation() throws JsonProcessingException {
    var inputJson = """
        {
          "id" : "AddExampleUnionFieldOnExampleResourceType",
          "code" : "AddExampleUnionFieldOnExampleResourceType",
          "description" : "Generated command for adding exampleUnionField(decimal | Extension | Reference) on ExampleResourceType Aggregate",
          "instance" : true,
          "kind" : "operation",
          "name" : "Add ExampleUnionField on ExampleResourceType",
          "status" : "draft",
          "system" : false,
          "type" : false,
          "parameter" : [ {
            "max" : "1",
            "min" : 1,
            "name" : "exampleUnionFieldDecimal",
            "type" : "decimal",
            "use" : "in",
            "referencedFrom" : [ {
              "source" : "ExampleResourceType.exampleUnionField"
            } ],
            "targetProfileReference" : [ ]
          }, {
            "max" : "1",
            "min" : 1,
            "name" : "exampleUnionFieldExtension",
            "type" : "Extension",
            "use" : "in",
            "referencedFrom" : [ {
              "source" : "ExampleResourceType.exampleUnionField"
            } ],
            "targetProfileReference" : [ ]
          }, {
            "max" : "1",
            "min" : 1,
            "name" : "exampleUnionFieldReference",
            "type" : "Reference",
            "use" : "in",
            "referencedFrom" : [ {
              "source" : "ExampleResourceType.exampleUnionField"
            } ],
            "targetProfileReference" : [ {
              "id" : "OperationDefinition"
            }, {
              "id" : "StructureDefinition"
            } ]
          } ],
          "resource" : [ "ExampleResourceType" ]
        }
        """;

    var input = this.objectMapper.readValue(inputJson, OperationDefinitionDTO.class);
    var actual = this.mapper.map(input);
    this.thenObjectApproved(actual);
  }

  private void testMapper(String operationName) {
    var input = this.provider.provide(operationName);
    var actual = this.mapper.map(input);
    this.thenObjectApproved(actual);
  }
}