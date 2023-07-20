package ai.stapi.graphsystem.operationdefinition.model.resourceStructureTypeOperationsMapper;

import ai.stapi.objectRenderer.infrastructure.objectToJsonStringRenderer.ObjectToJSonStringOptions;
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

class UpdateResourceOperationMapperTest extends SystemSchemaIntegrationTestCase {

  @Autowired
  private UpdateResourceOperationMapper mapper;

  @Autowired
  private StructureSchemaProvider schemaProvider;

  @Test
  void itCanMap() throws CannotProvideStructureSchema {
    var input = this.schemaProvider.provideSpecific("AggregateDefinition");
    var actual = this.mapper.map((ResourceStructureType) input);
    this.thenObjectApproved(
        actual,
        ObjectToJSonStringOptions.RenderFeature.SORT_FIELDS,
        ObjectToJSonStringOptions.RenderFeature.RENDER_GETTERS
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
                        new FieldType("BoxedString", "string")
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
        ObjectToJSonStringOptions.RenderFeature.SORT_FIELDS,
        ObjectToJSonStringOptions.RenderFeature.RENDER_GETTERS
    );
  }

  @Test
  void itCanMapNewFieldOnResource() throws CannotProvideStructureSchema {
    var modifiedResource = (ComplexStructureType) this.schemaProvider.provideSpecific(
        "AggregateDefinition"
    );
    var actual = this.mapper.mapNewFields(
        modifiedResource,
        List.of("name")
    );
    this.thenObjectApproved(
        actual,
        ObjectToJSonStringOptions.RenderFeature.SORT_FIELDS,
        ObjectToJSonStringOptions.RenderFeature.RENDER_GETTERS
    );
  }

  @Test
  void itCanMapNewInheritedFieldOnResource() throws CannotProvideStructureSchema {
    var modifiedResource = (ComplexStructureType) this.schemaProvider.provideSpecific(
        "Resource"
    );
    var actual = this.mapper.mapNewFields(
        modifiedResource,
        List.of("language")
    );
    this.thenObjectApproved(
        actual,
        ObjectToJSonStringOptions.RenderFeature.SORT_FIELDS,
        ObjectToJSonStringOptions.RenderFeature.RENDER_GETTERS
    );
  }

}