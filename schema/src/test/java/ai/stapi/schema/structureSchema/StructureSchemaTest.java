package ai.stapi.schema.structureSchema;

import ai.stapi.schema.test.integration.IntegrationTestCase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class StructureSchemaTest extends IntegrationTestCase {

  @Test
  void itCreatesSchemaWithSerializationTypeAsMapKeys() {
    var schema = new StructureSchema(
        new PrimitiveStructureType(
            "primitive",
            "This is primitive",
            false,
            ""
        ),
        new ComplexStructureType(
            "Element",
            new HashMap<>(),
            "Required type for primitive",
            "",
            false
        )
    );
    this.thenObjectApproved(schema);
  }

  @Test
  void itCanMergeTwoSchemas() {
    var schemaA = new StructureSchema(
        new PrimitiveStructureType(
            "primitive",
            "This is primitive",
            false,
            ""
        ),
        new ComplexStructureType(
            "Element",
            new HashMap<>(),
            "Required type for primitive",
            "",
            false
        )
    );
    var schemaB = new StructureSchema(
        new PrimitiveStructureType(
            "anotherPrimitive",
            "This is primitive",
            false,
            ""
        ),
        new ComplexStructureType(
            "Element",
            new HashMap<>(),
            "Required type for primitive",
            "",
            false
        )
    );
    var merged = schemaA.merge(schemaB);
    this.thenObjectApproved(merged);
  }

  @Test
  void itMergesFieldsWhenMergingSameTypes() {
    var schemaA = new StructureSchema(
        new ComplexStructureType(
            "Element",
            new HashMap<>(),
            "Required type for primitive",
            "",
            false
        ),
        new PrimitiveStructureType(
            "primitive",
            "This is primitive",
            false,
            ""
        ),
        new ComplexStructureType(
            "TestComplex",
            new HashMap<>(
                Map.of(
                    "originalField",
                    new FieldDefinition(
                        "originalField",
                        0,
                        "*",
                        "this is parent's field",
                        new ArrayList<>(List.of(new FieldType("primitive", "primitive"))),
                        "TestComplex"
                    )
                )
            ),
            "This is old complex type",
            "",
            false
        )
    );
    var schemaB = new StructureSchema(
        new ComplexStructureType(
            "Element",
            new HashMap<>(),
            "Required type for primitive",
            "",
            false
        ),
        new PrimitiveStructureType(
            "primitive",
            "This is primitive",
            false,
            ""
        ),
        new ComplexStructureType(
            "TestComplex",
            new HashMap<>(
                Map.of(
                    "otherField",
                    new FieldDefinition(
                        "otherField",
                        0,
                        "*",
                        "this is parent's field",
                        new ArrayList<>(List.of(new FieldType("primitive", "primitive"))),
                        "TestComplex"
                    )
                )
            ),
            "This is new complex type",
            "",
            false
        )
    );
    var merged = schemaA.merge(schemaB);
    this.thenObjectApproved(merged);
  }
}