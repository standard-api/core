package ai.stapi.graphsystem.operationdefinition.model.resourceStructureTypeOperationsMapper;

import ai.stapi.objectRenderer.infrastructure.objectToJsonStringRenderer.ObjectToJSonStringOptions.RenderFeature;
import ai.stapi.schema.structureSchema.ComplexStructureType;
import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structureSchema.FieldType;
import ai.stapi.schema.structureSchema.ResourceStructureType;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaProvider;
import ai.stapi.schema.structureSchemaProvider.exception.CannotProvideStructureSchema;
import ai.stapi.test.SystemSchemaIntegrationTestCase;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CreateResourceOperationMapperTest extends SystemSchemaIntegrationTestCase {

  @Autowired
  private CreationalResourceOperationMapper mapper;

  @Autowired
  private StructureSchemaProvider schemaProvider;

  @Test
  void itCanMap() throws CannotProvideStructureSchema {
    var input = this.schemaProvider.provideSpecific("AggregateDefinition");
    var actual = this.mapper.map((ResourceStructureType) input);
    this.thenObjectApproved(
        actual,
        RenderFeature.SORT_FIELDS,
        RenderFeature.RENDER_GETTERS
    );
  }

  @Test
  void itCanMapResourceWithUnionField() {
    var actual = this.mapper.map(
        new ResourceStructureType(
            "ExampleResourceType",
            Map.of(
                "exampleUnionField", new FieldDefinition(
                    "exampleUnionField",
                    1,
                    "1",
                    "Example Union field on Example Resource Type",
                    List.of(
                        new FieldType("BoxedDecimal", "decimal"),
                        new FieldType("Extension", "Extension"),
                        new FieldType("StructureDefinition", "Reference"),
                        new FieldType("OperationDefinition", "Reference")
                    ),
                    "ExampleResourceType"
                )
            ),
            "This is a example resource type.",
            null,
            false
        )
    );
    this.thenObjectApproved(
        actual,
        RenderFeature.SORT_FIELDS,
        RenderFeature.RENDER_GETTERS
    );
  }

  @Test
  void itCanMapNewFieldOnResource() throws CannotProvideStructureSchema {
    var modifiedResource = (ComplexStructureType) this.schemaProvider.provideSpecific(
        "AggregateDefinition"
    );
    var actual = this.mapper.mapNewFields(
        modifiedResource,
        List.of("command")
    );
    this.thenObjectApproved(
        actual,
        RenderFeature.SORT_FIELDS,
        RenderFeature.RENDER_GETTERS
    );
  }

  @Test
  void itCanMapNewInheritedFieldOnResource() throws CannotProvideStructureSchema {
    var modifiedResource = (ComplexStructureType) this.schemaProvider.provideSpecific(
        "DomainResource"
    );
    var actual = this.mapper.mapNewFields(
        modifiedResource,
        List.of("text")
    );
    this.thenObjectApproved(
        actual,
        RenderFeature.SORT_FIELDS,
        RenderFeature.RENDER_GETTERS
    );
  }
}