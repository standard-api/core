package ai.stapi.schema.structureSchemaProvider;

import ai.stapi.schema.structureSchema.ComplexStructureType;
import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structureSchema.FieldType;
import ai.stapi.schema.structureSchema.PrimitiveStructureType;
import ai.stapi.schema.structureSchema.StructureSchema;
import ai.stapi.schema.structureSchema.exception.FieldsNotFoundException;
import ai.stapi.schema.structuredefinition.ElementDefinition;
import ai.stapi.schema.structuredefinition.ElementDefinitionType;
import ai.stapi.schema.structuredefinition.StructureDefinitionData;
import ai.stapi.schema.test.integration.IntegrationTestCase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(StubStructureSchemaProvider.class)
class StructureSchemaFinderTest extends IntegrationTestCase {

  @Autowired
  private StructureSchemaFinder structureSchemaFinder;

  @Autowired
  private StubStructureSchemaProvider structureSchemaProvider;

  @Test
  void itWillFailGettingFieldForNonExistingField() {
    this.structureSchemaProvider.setSchema(
        new StructureSchema(
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
        )
    );

    Executable throwable =
        () -> this.structureSchemaFinder.getFieldDefinitionFor("nonExistingType", "any");

    this.thenExceptionMessageApproved(
        FieldsNotFoundException.class,
        throwable
    );
  }

  @Test
  void itWillFailGettingFieldForPrimitive() {
    this.structureSchemaProvider.setSchema(
        new StructureSchema(
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
        )
    );

    Executable throwable =
        () -> this.structureSchemaFinder.getFieldDefinitionFor("primitive", "any");

    this.thenExceptionMessageApproved(
        FieldsNotFoundException.class,
        throwable
    );
  }

  @Test
  void itWillGetFieldDefinitionOnComplex() {

    this.structureSchemaProvider.setSchema(
        new StructureSchema(
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
                        "testedField",
                        new FieldDefinition(
                            "testedField",
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
        )
    );

    this.thenObjectApproved(
        this.structureSchemaFinder.getFieldDefinitionFor("TestComplex", "testedField"));
    Assertions.assertTrue(this.structureSchemaFinder.isList("TestComplex", "testedField"));
  }

  @Test
  void itWillGetFieldDefinitionOnBoxed() {
    this.structureSchemaProvider.add(
        new ArrayList<>(List.of(
            new StructureDefinitionData(
                "testPrimitive",
                "http://test.url/for/primitive",
                "draft",
                "Structure Definition for test primitive",
                "primitive-type",
                false,
                "testPrimitive",
                null,
                null,
                new ArrayList<>()
            ),
            new StructureDefinitionData(
                "Element",
                "http://test.url/for/Element",
                "draft",
                "Structure Definition for Element",
                "complex-type",
                false,
                "Element",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "Element.exampleLeafField",
                            new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                            0,
                            "1",
                            "example field with primitive",
                            "",
                            ""
                        ),
                        new ElementDefinition(
                            "Element.exampleListField",
                            new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                            0,
                            "*",
                            "example field with primitive",
                            "",
                            ""
                        )
                    )
                )
            )
        ))
    );

    this.thenObjectApproved(
        this.structureSchemaFinder.getFieldDefinitionFor("BoxedTestPrimitive", "exampleLeafField"));
    Assertions.assertFalse(
        this.structureSchemaFinder.isList("BoxedTestPrimitive", "exampleLeafField"));
    Assertions.assertTrue(
        this.structureSchemaFinder.isList("BoxedTestPrimitive", "exampleListField"));
  }
}