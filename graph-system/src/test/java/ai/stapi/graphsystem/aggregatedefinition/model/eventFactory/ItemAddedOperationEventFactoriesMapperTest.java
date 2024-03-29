package ai.stapi.graphsystem.aggregatedefinition.model.eventFactory;

import ai.stapi.graphsystem.operationdefinition.infrastructure.AdHocOperationDefinitionProvider;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import ai.stapi.test.SystemSchemaIntegrationTestCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
            "targetProfileRef" : [ ]
          }, {
            "max" : "1",
            "min" : 1,
            "name" : "exampleUnionFieldExtension",
            "type" : "Extension",
            "use" : "in",
            "referencedFrom" : [ {
              "source" : "ExampleResourceType.exampleUnionField"
            } ],
            "targetProfileRef" : [ ]
          }, {
            "max" : "1",
            "min" : 1,
            "name" : "exampleUnionFieldReference",
            "type" : "Reference",
            "use" : "in",
            "referencedFrom" : [ {
              "source" : "ExampleResourceType.exampleUnionField"
            } ],
            "targetProfileRef" : [ {
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

  @Test
  public void itCanDealWithContentReference() throws JsonProcessingException {
    var inputJson = """
        {
          "id" : "AddContainsOnValueSetExpansionContains",
          "code" : "AddContainsOnValueSetExpansionContains",
          "description" : "Generated command for adding contains(ValueSetExpansionContains) on ValueSet Aggregate",
          "instance" : true,
          "kind" : "operation",
          "name" : "Add Contains on ValueSet.expansion.contains",
          "status" : "draft",
          "system" : false,
          "type" : false,
          "parameter" : [ {
            "max" : "*",
            "min" : 1,
            "name" : "contains",
            "type" : "ValueSetExpansionContains",
            "use" : "in",
            "referencedFrom" : [ {
              "source" : "ValueSet.expansion.contains.contains"
            } ],
            "targetProfileRef" : [ ]
          }, {
            "max" : "1",
            "min" : 1,
            "name" : "containsId",
            "type" : "id",
            "use" : "in",
            "referencedFrom" : [ {
              "source" : "ValueSet.expansion.contains.id"
            } ],
            "targetProfileRef" : [ ]
          } ],
          "resource" : [ "ValueSet" ]
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