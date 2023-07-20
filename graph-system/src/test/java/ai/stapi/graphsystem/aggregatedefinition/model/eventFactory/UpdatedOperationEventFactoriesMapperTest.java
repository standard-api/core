package ai.stapi.graphsystem.aggregatedefinition.model.eventFactory;

import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO.ParameterDTO;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO.ParameterDTO.ReferencedFrom;
import ai.stapi.graphsystem.operationdefinition.model.resourceStructureTypeOperationsMapper.OperationDefinitionParameters;
import ai.stapi.schema.structuredefinition.StructureDefinitionId;
import ai.stapi.test.SystemSchemaIntegrationTestCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UpdatedOperationEventFactoriesMapperTest extends SystemSchemaIntegrationTestCase {

  @Autowired
  private UpdatedOperationEventFactoriesMapper mapper;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void itCanMap() throws JsonProcessingException {
    var inputJson = """
          {
            "id" : "UpdateAggregateDefinition",
            "code" : "Update AggregateDefinition",
            "description" : "Generated command for updating AggregateDefinition with only primitive fields.",
            "instance" : true,
            "kind" : "operation",
            "name" : "Update AggregateDefinition",
            "status" : "draft",
            "system" : false,
            "type" : true,
            "parameter" : [ {
              "max" : "1",
              "min" : 0,
              "name" : "name",
              "type" : "string",
              "use" : "in",
              "referencedFrom" : [ {
                "source" : "AggregateDefinition.name"
              } ],
              "targetProfileRef" : [ ]
            }, {
              "max" : "1",
              "min" : 0,
              "name" : "description",
              "type" : "string",
              "use" : "in",
              "referencedFrom" : [ {
                "source" : "AggregateDefinition.description"
              } ],
              "targetProfileRef" : [ ]
            }, {
              "max" : "1",
              "min" : 0,
              "name" : "implicitRules",
              "type" : "uri",
              "use" : "in",
              "referencedFrom" : [ {
                "source" : "AggregateDefinition.implicitRules"
              } ],
              "targetProfileRef" : [ ]
            }, {
              "max" : "1",
              "min" : 0,
              "name" : "language",
              "type" : "code",
              "use" : "in",
              "referencedFrom" : [ {
                "source" : "AggregateDefinition.language"
              } ],
              "targetProfileRef" : [ ]
            } ],
            "resource" : [ "AggregateDefinition" ]
          }
        """;
    var input = this.objectMapper.readValue(inputJson, OperationDefinitionDTO.class);
    var actual = this.mapper.map(input);
    this.thenObjectApproved(actual);
  }

  @Test
  void itCanMapOperationWithUnion() throws JsonProcessingException {
    var inputJson = """
           {
                 "id" : "UpdateExampleResourceType",
                 "code" : "Update ExampleResourceType",
                 "description" : "Generated command for updating ExampleResourceType.",
                 "instance" : true,
                 "kind" : "operation",
                 "name" : "Update ExampleResourceType",
                 "status" : "draft",
                 "system" : false,
                 "type" : true,
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
                   "name" : "exampleUnionFieldString",
                   "type" : "string",
                   "use" : "in",
                   "referencedFrom" : [ {
                     "source" : "ExampleResourceType.exampleUnionField"
                   } ],
                   "targetProfileRef" : [ ]
                 } ],
                 "resource" : [ "ExampleResourceType" ]
               }
        """;
    var input = this.objectMapper.readValue(inputJson, OperationDefinitionDTO.class);
    var actual = this.mapper.map(input);
    this.thenObjectApproved(actual);
  }

  @Test
  void itCanMapNewParameter() {
    var actual = this.mapper.mapParameters(
        new OperationDefinitionParameters(
            "UpdateAggregateDefinition",
            List.of(
                new ParameterDTO(
                    "exampleParameter",
                    "in",
                    1,
                    "1",
                    "string",
                    new ReferencedFrom("AggregateDefinition.exampleParameter"),
                    List.of(
                        new StructureDefinitionId("AggregateDefinition")
                    )
                )
            )
        )
    );
    this.thenObjectApproved(actual);
  }
}