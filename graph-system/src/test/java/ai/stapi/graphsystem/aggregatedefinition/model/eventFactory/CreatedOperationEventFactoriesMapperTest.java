package ai.stapi.graphsystem.aggregatedefinition.model.eventFactory;

import ai.stapi.graphsystem.operationdefinition.infrastructure.AdHocOperationDefinitionProvider;
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

class CreatedOperationEventFactoriesMapperTest extends SystemSchemaIntegrationTestCase {

  @Autowired
  private CreatedOperationEventFactoriesMapper mapper;

  @Autowired
  private AdHocOperationDefinitionProvider provider;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void itCanMap() {
    var input = this.provider.provide("CreateAggregateDefinition");
    var actual = this.mapper.map(input);
    this.thenObjectApproved(actual);
  }

  @Test
  void itCanMapOperationWithUnion() throws JsonProcessingException {
    var inputJson = """
           {
                 "id" : "CreateExampleResourceType",
                 "code" : "Create ExampleResourceType",
                 "description" : "Generated command for creating ExampleResourceType with all fields.",
                 "instance" : true,
                 "kind" : "operation",
                 "name" : "Create ExampleResourceType",
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
  void itCanMapNewParameter() {
    var actual = this.mapper.mapParameters(
        new OperationDefinitionParameters(
            "CreateAggregateDefinition",
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