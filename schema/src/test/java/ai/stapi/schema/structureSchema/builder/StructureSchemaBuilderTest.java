package ai.stapi.schema.structureSchema.builder;

import ai.stapi.schema.structureSchema.ComplexStructureType;
import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structureSchema.FieldType;
import ai.stapi.schema.structureSchema.PrimitiveStructureType;
import ai.stapi.schema.test.integration.IntegrationTestCase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class StructureSchemaBuilderTest extends IntegrationTestCase {

  @Test
  public void itCanCopyStructureTypeCorrectly() {
    var schemaBuilder = new StructureSchemaBuilder();
    var expectedStructure = new PrimitiveStructureType(
        "string",
        "This is string",
        false,
        "Element"
    );
    var copiedBuilderFromExpected =
        new PrimitiveStructureTypeBuilder().copyToBuilder(expectedStructure);
    this.thenObjectsEquals(expectedStructure, copiedBuilderFromExpected.build(schemaBuilder));
  }

  @Test
  public void itCanCopyComplexStructureTypeCorrectly() {
    var schemaBuilder = new StructureSchemaBuilder();
    var complexStructure = new ComplexStructureType(
        "Element",
        new HashMap<>(
            Map.of(
                "field1", new FieldDefinition(
                    "field1",
                    0,
                    "*",
                    "This is tested field",
                    new ArrayList<>(List.of(new FieldType("string", "string"))),
                    "Element"
                )
            )
        ),
        "This is element",
        "ElementsParent",
        false
    );
    var copiedBuilderFromExpected =
        new ComplexStructureTypeBuilder().copyToBuilder(complexStructure);
    this.thenObjectsEquals(complexStructure, copiedBuilderFromExpected.build(schemaBuilder));
  }
}