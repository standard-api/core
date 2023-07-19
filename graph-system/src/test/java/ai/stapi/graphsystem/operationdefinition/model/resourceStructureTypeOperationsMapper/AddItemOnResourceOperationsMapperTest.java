package ai.stapi.graphsystem.operationdefinition.model.resourceStructureTypeOperationsMapper;

import ai.stapi.objectRenderer.infrastructure.objectToJsonStringRenderer.ObjectToJSonStringOptions.RenderFeature;
import ai.stapi.schema.structureSchema.ComplexStructureType;
import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structureSchema.FieldType;
import ai.stapi.schema.structureSchema.ResourceStructureType;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaProvider;
import ai.stapi.schema.structureSchemaProvider.exception.CannotProvideStructureSchema;
import ai.stapi.test.SystemSchemaIntegrationTestCase;
import ai.stapi.test.schemaintegration.StructureDefinitionScope;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@StructureDefinitionScope(AddItemOnResourceOperationsMapperTestDefinitionsLoader.SCOPE)
class AddItemOnResourceOperationsMapperTest extends SystemSchemaIntegrationTestCase {

  @Autowired
  private AddItemOnResourceOperationsMapper mapper;

  @Autowired
  private StructureSchemaProvider schemaProvider;

  @Test
  void itCanMapSimpleStructure() throws CannotProvideStructureSchema {
    var input = this.schemaProvider.provideSpecific("ExampleTestResource");
    var actual = this.mapper.map((ResourceStructureType) input);
    this.thenObjectApproved(
        actual,
        RenderFeature.SORT_FIELDS,
        RenderFeature.RENDER_GETTERS
    );
  }

  @Test
  void itCanMapDeeperFieldToAdd() {
    var actual = this.mapper.map(
        new ResourceStructureType(
            "ExampleResourceType",
            Map.of(
                "exampleElementField", new FieldDefinition(
                    "exampleElementField",
                    1,
                    "1",
                    "Example Element field on Example Resource Type",
                    List.of(
                        new FieldType("Timing", "Timing")
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
                        new FieldType("StructureDefinition", "Reference"),
                        new FieldType("Timing", "Timing"),
                        new FieldType("SameFieldComplex1", "SameFieldComplex1"),
                        new FieldType("SameFieldComplex2", "SameFieldComplex2")
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
  void itCanMapResourceWithUnionListField() {
    var actual = this.mapper.map(
        new ResourceStructureType(
            "ExampleResourceType",
            Map.of(
                "exampleUnionField", new FieldDefinition(
                    "exampleUnionField",
                    1,
                    "*",
                    "Example Union field on Example Resource Type",
                    List.of(
                        new FieldType("BoxedDecimal", "decimal"),
                        new FieldType("StructureDefinition", "Reference"),
                        new FieldType("Timing", "Timing"),
                        new FieldType("SameFieldComplex1", "SameFieldComplex1"),
                        new FieldType("SameFieldComplex2", "SameFieldComplex2")
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
  void itCanMapOperationDefinition() throws CannotProvideStructureSchema {
    var input = this.schemaProvider.provideSpecific("OperationDefinition");
    var actual = this.mapper.map((ResourceStructureType) input);
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
        List.of("extension")
    );
    this.thenObjectApproved(
        actual,
        RenderFeature.SORT_FIELDS,
        RenderFeature.RENDER_GETTERS
    );
  }

  @Test
  void itCanMapNewFieldOnComplexType() throws CannotProvideStructureSchema {
    var modifiedResource = (ComplexStructureType) this.schemaProvider.provideSpecific(
        "CodeableConcept"
    );
    var actual = this.mapper.mapNewFields(
        modifiedResource,
        List.of("coding")
    );
    this.thenObjectApproved(
        actual,
        RenderFeature.SORT_FIELDS,
        RenderFeature.RENDER_GETTERS
    );
  }

  @Test
  void itCanMapNewInheritedFieldOnComplexType() throws CannotProvideStructureSchema {
    var modifiedResource = (ComplexStructureType) this.schemaProvider.provideSpecific(
        "Element"
    );
    var actual = this.mapper.mapNewFields(
        modifiedResource,
        List.of("extension")
    );
    this.thenObjectApproved(
        actual,
        RenderFeature.SORT_FIELDS,
        RenderFeature.RENDER_GETTERS
    );
  }

  @Test
  void itCanDealWithContentReference() throws CannotProvideStructureSchema {
    var input = this.schemaProvider.provideSpecific("ValueSet");
    var actual = this.mapper.map((ResourceStructureType) input);
    this.thenObjectApproved(
        actual,
        RenderFeature.SORT_FIELDS,
        RenderFeature.RENDER_GETTERS
    );
  }
}