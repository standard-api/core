package ai.stapi.schema.structureSchemaMapper;

import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.schema.fixtures.TestSystemModelDefinitionsLoader;
import ai.stapi.schema.structuredefinition.loader.AdHocStructureDefinitionLoader;
import ai.stapi.schema.scopeProvider.ScopeOptions;
import ai.stapi.schema.scopeProvider.ScopeProvider;
import ai.stapi.schema.structureSchema.ComplexStructureType;
import ai.stapi.schema.structureSchema.PrimitiveStructureType;
import ai.stapi.schema.structureSchema.StructureSchema;
import ai.stapi.schema.structuredefinition.ElementDefinition;
import ai.stapi.schema.structuredefinition.ElementDefinitionType;
import ai.stapi.schema.structuredefinition.StructureDefinitionData;
import ai.stapi.schema.test.integration.IntegrationTestCase;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StructureDefinitionToSSMapperTest extends IntegrationTestCase {

    private List<StructureDefinitionData> structureDefinitionDataList;

    @Autowired
    private StructureDefinitionToSSMapper structureDefinitionToSSMapper;

    @Autowired
    private AdHocStructureDefinitionLoader structureDefinitionLoader;

    @BeforeAll
    public static void beforeAll(
        @Autowired ScopeProvider scopeProvider
    ) {
        scopeProvider.set(
            new ScopeOptions(
                TestSystemModelDefinitionsLoader.SCOPE,
                TestSystemModelDefinitionsLoader.TAG
            )
        );
    }

    @Test
    void itCanMapPrimitiveStructureDefinition() {
        var actual = this.structureDefinitionToSSMapper.map(
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
                    new ArrayList<>()
                )
            ))
        );
        this.thenObjectApproved(actual);
    }

    @Test
    void itCanMapBoxedStructureDefinitionWithFieldsFromParent() {
        var actual = this.structureDefinitionToSSMapper.map(
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
                                "Element.exampleField",
                                new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                                0,
                                "1",
                                "example field with primitive",
                                "",
                                ""
                            )
                        )
                    )
                )
            ))
        );
        this.thenObjectApproved(actual);
    }

    @Test
    void itCanMapPrimitiveStructuresWhenTheyAreParentsDefinition() {
        var actual = this.structureDefinitionToSSMapper.map(
            new ArrayList<>(List.of(
                new StructureDefinitionData(
                    "primitiveTop",
                    "http://test.url/for/primitiveTopParent",
                    "draft",
                    "Structure Definition for top primitive type with no parent",
                    "primitive-type",
                    false,
                    "primitiveTop",
                    null,
                    null,
                    new ArrayList<>()
                ),
                new StructureDefinitionData(
                    "primitiveMiddle",
                    "http://test.url/for/primitiveMiddle",
                    "draft",
                    "Structure Definition for primitive which parent and also has parent",
                    "primitive-type",
                    false,
                    "primitiveMiddle",
                    "http://test.url/for/primitiveTop",
                    new UniqueIdentifier("primitiveTop"),
                    new ArrayList<>()
                ),
                new StructureDefinitionData(
                    "primitiveBottom",
                    "http://test.url/for/primitiveBottom",
                    "draft",
                    "Structure Definition for primitive",
                    "primitive-type",
                    false,
                    "primitiveBottom",
                    "http://test.url/for/primitiveMiddle",
                    new UniqueIdentifier("primitiveMiddle"),
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
                    new ArrayList<>()
                )
            ))
        );
        this.thenObjectApproved(actual);
    }

    @Test
    void itShouldFailDueToMissingElementDependencyWhenMappingOnlyPrimitive() {
        var actual = this.structureDefinitionToSSMapper.map(
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
            )
        );
        this.thenObjectApproved(actual);
    }

    @Test
    void itCanMapPrimitiveStructureDefinitionWithExistingParentInContext() {
        var elementStructure = new ComplexStructureType(
            "Element",
            new HashMap<>(),
            "Element in context",
            "",
            false
        );
        var actual = this.structureDefinitionToSSMapper.map(
            new StructureDefinitionData(
                "testPrimitive",
                "http://test.url/for/testPrimitive",
                "draft",
                "Structure Definition for test primitive",
                "primitive-type",
                false,
                "testPrimitive",
                null,
                null,
                new ArrayList<>()
            ),
            new StructureSchema(elementStructure)
        );
        this.thenObjectApproved(actual);
    }


    @Test
    void itCanOverrideExistingStructure() {
        var elementStructure = new ComplexStructureType(
            "Element",
            new HashMap<>(),
            "Old desctiption",
            "ElementsParent",
            false
        );
        var elementParent = new ComplexStructureType(
            "ElementsParent",
            new HashMap<>(),
            "Old desctiption",
            null,
            false
        );
        var actual = this.structureDefinitionToSSMapper.map(
            new StructureDefinitionData(
                "Element",
                "http://test.url/for/Element",
                "draft",
                "New description",
                "complex-type",
                true,
                "Element",
                "",
                new UniqueIdentifier(""),
                new ArrayList<>()
            ),
            new StructureSchema(
                elementStructure,
                elementParent
            )
        );
        this.thenObjectApproved(actual);
    }

    @Test
    void itCanMapPrimitiveStructureDefinitionWithExistingParentInContextRecursively() {
        var elementStructure = new ComplexStructureType(
            "Element",
            new HashMap<>(),
            "Element in context",
            "ElementsParent",
            false
        );
        var elementsParent = new ComplexStructureType(
            "ElementsParent",
            new HashMap<>(),
            "Element's parent",
            "",
            false
        );
        var actual = this.structureDefinitionToSSMapper.map(
            new ArrayList<>(List.of(
                new StructureDefinitionData(
                    "testPrimitive",
                    "http://test.url/for/testPrimitive",
                    "draft",
                    "Structure Definition for test primitive",
                    "primitive-type",
                    false,
                    "testPrimitive",
                    null,
                    null,
                    new ArrayList<>()
                )
            )),
            new StructureSchema(
                elementStructure,
                elementsParent
            )
        );
        this.thenObjectApproved(actual);
    }

    @Test
    void itCanMapPrimitiveStructureDefinitionWithExisting() {
        var primitiveStructure = new PrimitiveStructureType(
            "Primitive",
            "Existing Primitive type",
            false,
            "Element"
        );
        var elementStructure = new ComplexStructureType(
            "Element",
            new HashMap<>(),
            "Element in context",
            "",
            false
        );
        var actual = this.structureDefinitionToSSMapper.map(
            new StructureDefinitionData(
                "TestComplex",
                "http://test.url/for/TestComplex",
                "draft",
                "Structure Definition to test field which has type already defined in context",
                "complex-type",
                false,
                "TestComplex",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplex.primitiveField",
                            new ArrayList<>(List.of(new ElementDefinitionType("Primitive"))),
                            0,
                            "1",
                            "Field with type already existing in context",
                            "",
                            ""
                        )
                    ))
            ),
            new StructureSchema(
                primitiveStructure,
                elementStructure
            )
        );

        this.thenObjectApproved(actual);
    }

    @Test
    void itCanMapComplexAndPrimitive() {
        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
            new StructureDefinitionData(
                "TestComplex",
                "http://test.url/for/complex",
                "draft",
                "Structure Definition for test complex",
                "complex-type",
                false,
                "TestComplex",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplex.someField",
                            new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                            0,
                            "1",
                            "Example Field",
                            "",
                            ""
                        )
                    )
                )
            )
        );
        definitions.add(
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
            )
        );
        definitions.add(
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
                new ArrayList<>()
            )
        );
        var actual = this.structureDefinitionToSSMapper.map(definitions);
        this.thenObjectApproved(actual);
    }

    @Test
    void itCanMapComplexAndPrimitivesInUnionType() {
        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
            new StructureDefinitionData(
                "TestComplex",
                "http://test.url/for/complex",
                "draft",
                "Structure Definition for test complex",
                "complex-type",
                false,
                "TestComplex",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplex.someField",
                            new ArrayList<>(List.of(
                                new ElementDefinitionType("decimalPrimitive"),
                                new ElementDefinitionType("stringPrimitive")
                            )),
                            0,
                            "1",
                            "Example Field",
                            "",
                            ""
                        )
                    )
                )
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "decimalPrimitive",
                "http://test.url/for/primitive",
                "draft",
                "Structure Definition for test decimal primitive",
                "primitive-type",
                false,
                "decimalPrimitive",
                null,
                null,
                new ArrayList<>()
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "stringPrimitive",
                "http://test.url/for/primitive",
                "draft",
                "Structure Definition for test string primitive",
                "primitive-type",
                false,
                "stringPrimitive",
                null,
                null,
                new ArrayList<>()
            )
        );
        definitions.add(
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
                new ArrayList<>()
            )
        );
        var actual = this.structureDefinitionToSSMapper.map(definitions);
        this.thenObjectApproved(actual);
    }

    @Test
    void itCanMapPrimitivesInUnionType() {
        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
            new StructureDefinitionData(
                "TestComplex",
                "http://test.url/for/complex",
                "draft",
                "Structure Definition for test complex",
                "complex-type",
                false,
                "TestComplex",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplex.someField",
                            new ArrayList<>(List.of(
                                new ElementDefinitionType("code"),
                                new ElementDefinitionType("string")
                            )),
                            0,
                            "1",
                            "Example Field",
                            "",
                            ""
                        )
                    )
                )
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "code",
                "http://test.url/for/code",
                "draft",
                "Structure Definition for test decimal primitive",
                "primitive-type",
                false,
                "decimalPrimitive",
                "http://test.url/for/string",
                new UniqueIdentifier("string"),
                new ArrayList<>()
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "string",
                "http://test.url/for/string",
                "draft",
                "Structure Definition for test string primitive",
                "primitive-type",
                false,
                "string",
                null,
                null,
                new ArrayList<>()
            )
        );
        definitions.add(
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
                new ArrayList<>()
            )
        );
        var actual = this.structureDefinitionToSSMapper.map(definitions);
        this.thenObjectApproved(actual);
    }

    @Test
    void itCanMapComplexWithReferenceTypeReferencingComplexType() {
        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
            new StructureDefinitionData(
                "TestComplex",
                "http://test.url/for/TestComplex",
                "draft",
                "Structure Definition for TestComplex",
                "complex-type",
                false,
                "TestComplex",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplex.fieldWithReference",
                            new ArrayList<>(List.of(
                                new ElementDefinitionType(
                                    "Reference",
                                    List.of("http://test.url/for/ReferencedType")
                                )
                            )),
                            0,
                            "1",
                            "Example Field",
                            "",
                            ""
                        )
                    )
                )
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "ReferencedType",
                "http://test.url/for/ReferencedType",
                "draft",
                "Structure Definition for ReferencedType",
                "complex-type",
                false,
                "ReferencedType",
                null,
                null,
                new ArrayList<>()
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "Reference",
                "http://test.url/for/Reference",
                "draft",
                "Structure Definition for Reference",
                "complex-type",
                false,
                "Reference",
                null,
                null,
                new ArrayList<>()
            )
        );
        definitions.add(
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
                new ArrayList<>()
            )
        );
        var actual = this.structureDefinitionToSSMapper.map(definitions);
        this.thenObjectApproved(actual);
    }

    @Test
    void itAddsTypeToUnresolvableTypesWhenMissingDependency() {
        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
            new StructureDefinitionData(
                "TestComplex",
                "http://test.url/for/complex",
                "draft",
                "Structure Definition for test complex",
                "complex-type",
                false,
                "TestComplex",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplex.someField",
                            new ArrayList<>(List.of(
                                new ElementDefinitionType("decimalPrimitive"),
                                new ElementDefinitionType("stringPrimitive")
                            )),
                            0,
                            "1",
                            "Example Field",
                            "",
                            ""
                        )
                    )
                )
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "stringPrimitive",
                "http://test.url/for/primitive",
                "draft",
                "Structure Definition for test string primitive",
                "primitive-type",
                false,
                "stringPrimitive",
                null,
                null,
                new ArrayList<>()
            )
        );
        definitions.add(
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
                new ArrayList<>()
            )
        );
        var actual = this.structureDefinitionToSSMapper.map(definitions);
        this.thenObjectApproved(actual);
    }

    @Test
    void itCanMapTwoComplexWhichReferenceEachOtherThroughField() {
        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
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
            )
        );
        definitions.add(
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
                new ArrayList<>()
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "TestComplex1",
                "http://test.url/for/complex",
                "draft",
                "Structure Definition for test complex 1",
                "complex-type",
                false,
                "TestComplex1",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplex1.somePrimitiveField",
                            new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                            0,
                            "1",
                            "Primitive Example Field on Test Complex 1",
                            "",
                            ""
                        ),
                        new ElementDefinition(
                            "TestComplex1.otherComplexTypeField",
                            new ArrayList<>(List.of(new ElementDefinitionType("TestComplex2"))),
                            0,
                            "1",
                            "Other Complex Type Field on Test Complex 1",
                            "",
                            ""
                        )
                    )
                )
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "TestComplex2",
                "http://test.url/for/complex2",
                "draft",
                "Structure Definition for test complex 2",
                "complex-type",
                false,
                "TestComplex2",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplex2.somePrimitiveField",
                            new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                            0,
                            "1",
                            "Primitive Example Field on Test Complex 2",
                            "",
                            ""
                        ),
                        new ElementDefinition(
                            "TestComplex2.otherComplexTypeField",
                            new ArrayList<>(List.of(new ElementDefinitionType("TestComplex1"))),
                            0,
                            "1",
                            "Other Complex Type Field on Test Complex 2",
                            "",
                            ""
                        )
                    )
                )
            )
        );
        var actual = this.structureDefinitionToSSMapper.map(definitions);
        this.thenObjectApproved(actual);
    }

    @Test
    void itCanMapTwoSameComplexTypesWithMergedFields() {
        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
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
            )
        );
        definitions.add(
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
                new ArrayList<>()
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "TestComplex",
                "http://test.url/for/TestComplex",
                "draft",
                "Structure Definition for test TestComplex",
                "complex-type",
                false,
                "TestComplex",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplex.somePrimitiveField",
                            new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                            0,
                            "1",
                            "Primitive Example Field on TestComplex",
                            "",
                            ""
                        )
                    )
                )
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "TestComplex",
                "http://test.url/for/TestComplex",
                "draft",
                "Structure Definition for test TestComplex",
                "complex-type",
                false,
                "TestComplex",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplex.otherPrimitiveField",
                            new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                            0,
                            "1",
                            "Primitive Example Field on TestComplex",
                            "",
                            ""
                        )
                    )
                )
            )
        );
        var actual = this.structureDefinitionToSSMapper.map(definitions);
        this.thenObjectApproved(actual);
    }

    @Test
    void itCanMapTwoSameComplexTypesWithMergedFieldValues() {
        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
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
            )
        );
        definitions.add(
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
                new ArrayList<>()
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "TestComplex",
                "http://test.url/for/TestComplex",
                "draft",
                "Structure Definition for test TestComplex",
                "complex-type",
                false,
                "TestComplex",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplex.somePrimitiveField",
                            new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                            0,
                            "1",
                            "Old description",
                            "Old definition",
                            "Old comment"
                        )
                    )
                )
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "TestComplex",
                "http://test.url/for/TestComplex",
                "draft",
                "Structure Definition for test TestComplex",
                "complex-type",
                false,
                "TestComplex",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplex.somePrimitiveField",
                            new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                            1,
                            "*",
                            "New description",
                            "New definition",
                            "New comment"
                        )
                    )
                )
            )
        );
        var actual = this.structureDefinitionToSSMapper.map(definitions);
        this.thenObjectApproved(actual);
    }

    @Test
    void itCanMapTwoSameComplexTypesWithMergedFieldValuesOnAnonymous() {
        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
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
            )
        );
        definitions.add(
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
                new ArrayList<>()
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "TestComplex",
                "http://test.url/for/TestComplex",
                "draft",
                "Structure Definition for test TestComplex",
                "complex-type",
                false,
                "TestComplex",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplex.anonymousField",
                            new ArrayList<>(List.of(new ElementDefinitionType("Element"))),
                            0,
                            "1",
                            "Old description",
                            "Old definition",
                            "Old comment"
                        ),
                        new ElementDefinition(
                            "TestComplex.anonymousField.somePrimitive",
                            new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                            0,
                            "1",
                            "Old description",
                            "Old definition",
                            "Old comment"
                        )
                    )
                )
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "TestComplex",
                "http://test.url/for/TestComplex",
                "draft",
                "Structure Definition for test TestComplex",
                "complex-type",
                false,
                "TestComplex",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplex.anonymousField",
                            new ArrayList<>(List.of(new ElementDefinitionType("Element"))),
                            1,
                            "*",
                            "New description",
                            "New definition",
                            "New comment"
                        ),
                        new ElementDefinition(
                            "TestComplex.anonymousField.somePrimitive",
                            new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                            1,
                            "*",
                            "New description",
                            "New definition",
                            "New comment"
                        )
                    )
                )
            )
        );
        var actual = this.structureDefinitionToSSMapper.map(definitions);
        this.thenObjectApproved(actual);
    }

    @Test
    void itCanMapComplexWithAnonymousType() {
        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
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
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "TestComplexWithAnonymousType",
                "http://test.url/for/TestComplexWithAnonymousType",
                "draft",
                "Structure Definition with anonymous type",
                "complex-type",
                false,
                "TestComplexWithAnonymousType",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplexWithAnonymousType.testedType",
                            new ArrayList<>(List.of(new ElementDefinitionType("Element"))),
                            0,
                            "1",
                            "Anonymous type subjected to test",
                            "",
                            ""
                        ),
                        new ElementDefinition(
                            "TestComplexWithAnonymousType.testedType.field1",
                            new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                            0,
                            "1",
                            "Random primitive field",
                            "",
                            ""
                        )
                    )
                )
            )
        );
        definitions.add(
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
                new ArrayList<>()
            )
        );
        var actual = this.structureDefinitionToSSMapper.map(definitions);
        this.thenObjectApproved(actual);
    }


    @Test
    void itCanMapComplexWithDeepAnonymousType() {
        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
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
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "TestComplexWithAnonymousType",
                "http://test.url/for/TestComplexWithAnonymousType",
                "draft",
                "Structure Definition with anonymous type",
                "complex-type",
                false,
                "TestComplexWithAnonymousType",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplexWithAnonymousType.firstLevelAnonymous",
                            new ArrayList<>(List.of(new ElementDefinitionType("Element"))),
                            0,
                            "1",
                            "First level anonymous",
                            "",
                            ""
                        ),
                        new ElementDefinition(
                            "TestComplexWithAnonymousType.firstLevelAnonymous.secondLevelAnonymous",
                            new ArrayList<>(List.of(new ElementDefinitionType("Element"))),
                            0,
                            "1",
                            "Second level anonymous",
                            "",
                            ""
                        ),
                        new ElementDefinition(
                            "TestComplexWithAnonymousType.firstLevelAnonymous.secondLevelAnonymous.field1",
                            new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                            0,
                            "1",
                            "Random primitive field",
                            "",
                            ""
                        )
                    )
                )
            )
        );
        definitions.add(
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
                new ArrayList<>()
            )
        );
        var actual = this.structureDefinitionToSSMapper.map(definitions);
        this.thenObjectApproved(actual);
    }

    @Test
    void itCanMapComplexWith15InchDeepAnonymousType() {
        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
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
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "TestComplexWithAnonymousType",
                "http://test.url/for/TestComplexWithAnonymousType",
                "draft",
                "Structure Definition with anonymous type",
                "complex-type",
                false,
                "TestComplexWithAnonymousType",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplexWithAnonymousType.firstLevelAnonymous",
                            new ArrayList<>(List.of(new ElementDefinitionType("Element"))),
                            0,
                            "1",
                            "First level anonymous",
                            "",
                            ""
                        ),
                        new ElementDefinition(
                            "TestComplexWithAnonymousType.firstLevelAnonymous.secondLevelAnonymous",
                            new ArrayList<>(List.of(new ElementDefinitionType("Element"))),
                            0,
                            "1",
                            "Second level anonymous",
                            "",
                            ""
                        ),
                        new ElementDefinition(
                            "TestComplexWithAnonymousType.firstLevelAnonymous.secondLevelAnonymous.field1",
                            new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                            0,
                            "1",
                            "Random primitive field",
                            "",
                            ""
                        ),
                        new ElementDefinition(
                            "TestComplexWithAnonymousType.firstLevelAnonymous.secondLevelAnonymous.thirdLevelAnonymous",
                            new ArrayList<>(List.of(new ElementDefinitionType("Element"))),
                            0,
                            "1",
                            "Third level anonymous",
                            "",
                            ""
                        ),
                        new ElementDefinition(
                            "TestComplexWithAnonymousType.firstLevelAnonymous.secondLevelAnonymous.thirdLevelAnonymous.field1",
                            new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                            0,
                            "1",
                            "Random primitive field",
                            "",
                            ""
                        )
                    )
                )
            )
        );
        definitions.add(
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
                new ArrayList<>()
            )
        );
        var actual = this.structureDefinitionToSSMapper.map(definitions);
        this.thenObjectApproved(actual);
    }

    @Test
    void itDoesNotCreateComplexTypeWhenContainedAnonymousTypeFails() {
        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
            new StructureDefinitionData(
                "TestComplexWithAnonymousType",
                "http://test.url/for/TestComplexWithAnonymousType",
                "draft",
                "Structure Definition with anonymous type",
                "complex-type",
                false,
                "TestComplexWithAnonymousType",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplexWithAnonymousType.testedType",
                            new ArrayList<>(List.of(new ElementDefinitionType("Element"))),
                            0,
                            "1",
                            "Anonymous type subjected to test",
                            "",
                            ""
                        ),
                        new ElementDefinition(
                            "TestComplexWithAnonymousType.testedType.field1",
                            new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                            0,
                            "1",
                            "Random primitive field",
                            "",
                            ""
                        )
                    )
                )
            )
        );
        definitions.add(
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
                new ArrayList<>()
            )
        );
        var actual = this.structureDefinitionToSSMapper.map(definitions);
        this.thenObjectApproved(actual);
    }

    @Test
    void itDoesNotCreateComplexTypeWhenContainedAnonymousTypeFailsIndirectly() {
        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
            new StructureDefinitionData(
                "TestComplexWithAnonymousType",
                "http://test.url/for/TestComplexWithAnonymousType",
                "draft",
                "Structure Definition with anonymous type",
                "complex-type",
                false,
                "TestComplexWithAnonymousType",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplexWithAnonymousType.testedType",
                            new ArrayList<>(List.of(new ElementDefinitionType("Element"))),
                            0,
                            "1",
                            "Anonymous type subjected to test",
                            "",
                            ""
                        ),
                        new ElementDefinition(
                            "TestComplexWithAnonymousType.testedType.complexField",
                            new ArrayList<>(
                                List.of(new ElementDefinitionType("ComplexTypeWithMissingPrimitive"))),
                            0,
                            "1",
                            "Field with complex type which has missing dependency",
                            "",
                            ""
                        )
                    )
                )
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "ComplexTypeWithMissingPrimitive",
                "http://test.url/for/ComplexTypeWithMissingPrimitive",
                "draft",
                "Structure Definition for complex type with missing primitive",
                "complex-type",
                false,
                "ComplexTypeWithMissingPrimitive",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "ComplexTypeWithMissingPrimitive.primitiveField",
                            new ArrayList<>(List.of(new ElementDefinitionType("missingPrimitive"))),
                            0,
                            "1",
                            "Primitive field which definition is missing",
                            "",
                            ""
                        )
                    )
                )
            )
        );
        definitions.add(
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
                new ArrayList<>()
            )
        );
        var actual = this.structureDefinitionToSSMapper.map(definitions);
        this.thenObjectApproved(actual);
    }

    @Test
    void itDoesNotCreateComplexTypeWhenContainedAnonymousTypeFailsIndirectlyDueToUnresolvableParent() {
        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
            new StructureDefinitionData(
                "TestComplexWithAnonymousType",
                "http://test.url/for/TestComplexWithAnonymousType",
                "draft",
                "Structure Definition with anonymous type",
                "complex-type",
                false,
                "TestComplexWithAnonymousType",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplexWithAnonymousType.testedType",
                            new ArrayList<>(List.of(new ElementDefinitionType("BackboneElement"))),
                            0,
                            "1",
                            "Anonymous type subjected to test",
                            "",
                            ""
                        ),
                        new ElementDefinition(
                            "TestComplexWithAnonymousType.testedType.complexField",
                            new ArrayList<>(
                                List.of(new ElementDefinitionType("ComplexTypeWithPrimitive"))),
                            0,
                            "1",
                            "Field with complex type which has missing dependency",
                            "",
                            ""
                        )
                    )
                )
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "ComplexTypeWithPrimitive",
                "http://test.url/for/ComplexTypeWithMissingPrimitive",
                "draft",
                "Structure Definition for complex type with missing primitive",
                "complex-type",
                false,
                "ComplexTypeWithMissingPrimitive",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "ComplexTypeWithMissingPrimitive.primitiveField",
                            new ArrayList<>(
                                List.of(new ElementDefinitionType("primitiveWithMissingParent"))),
                            0,
                            "1",
                            "Primitive field which definition is missing",
                            "",
                            ""
                        )
                    )
                )
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "primitiveWithMissingParent",
                "http://test.url/for/primitiveWithMissingParent",
                "draft",
                "Structure Definition for test primitive",
                "primitive-type",
                false,
                "primitiveWithMissingParent",
                "http://test.url/for/missinElement",
                new UniqueIdentifier("missinElement"),
                new ArrayList<>()
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "BackboneElement",
                "http://test.url/for/Element",
                "draft",
                "Structure Definition for Element",
                "complex-type",
                false,
                "BackboneElement",
                null,
                null,
                new ArrayList<>()
            )
        );
        definitions.add(
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
                new ArrayList<>()
            )
        );
        var actual = this.structureDefinitionToSSMapper.map(definitions);
        this.thenObjectApproved(actual);
    }

    @Test
    void itCanMapComplexAndItsParentComplexAndTheyReferenceEachOther() {
        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
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
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "TestComplex1",
                "http://test.url/for/complex",
                "draft",
                "Structure Definition for test complex 1",
                "complex-type",
                false,
                "TestComplex1",
                "http://test.url/for/TestComplex2",
                new UniqueIdentifier("TestComplex2"),
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplex1.somePrimitiveField",
                            new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                            0,
                            "1",
                            "Primitive Example Field on Test Complex 1",
                            "",
                            ""
                        )
                    )
                )
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "TestComplex2",
                "http://test.url/for/complex2",
                "draft",
                "Structure Definition for test complex 2",
                "complex-type",
                false,
                "TestComplex2",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplex2.someParentPrimitiveField",
                            new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                            0,
                            "1",
                            "Primitive Example Field on Test Complex 2",
                            "",
                            ""
                        ),
                        new ElementDefinition(
                            "TestComplex2.otherParentComplexTypeField",
                            new ArrayList<>(List.of(new ElementDefinitionType("TestComplex1"))),
                            0,
                            "1",
                            "Other Complex Type Field on Test Complex 2",
                            "",
                            ""
                        )
                    )
                )
            )
        );
        definitions.add(
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
                new ArrayList<>()
            )
        );
        var actual = this.structureDefinitionToSSMapper.map(definitions);
        this.thenObjectApproved(actual);
    }

    @Test
    void itCanAddToUnresolvableFieldsFromComplexParentOfPrimitiveParent() {
        var actual = this.structureDefinitionToSSMapper.map(
            new ArrayList<>(List.of(
                new StructureDefinitionData(
                    "primitiveTop",
                    "http://test.url/for/primitiveTopParent",
                    "draft",
                    "Structure Definition for top primitive type with no parent",
                    "primitive-type",
                    false,
                    "primitiveTop",
                    "http://test.url/for/Element",
                    new UniqueIdentifier("Element"),
                    new ArrayList<>()
                ),
                new StructureDefinitionData(
                    "primitiveMiddle",
                    "http://test.url/for/primitiveMiddle",
                    "draft",
                    "Structure Definition for primitive which parent and also has parent",
                    "primitive-type",
                    false,
                    "primitiveMiddle",
                    "http://test.url/for/primitiveTop",
                    new UniqueIdentifier("primitiveTop"),
                    new ArrayList<>()
                ),
                new StructureDefinitionData(
                    "primitiveBottom",
                    "http://test.url/for/primitiveBottom",
                    "draft",
                    "Structure Definition for primitive",
                    "primitive-type",
                    false,
                    "primitiveBottom",
                    "http://test.url/for/primitiveMiddle",
                    new UniqueIdentifier("primitiveMiddle"),
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
                                "TestComplex1.missingPrimitiveField",
                                new ArrayList<>(
                                    List.of(new ElementDefinitionType("missingPrimitiveType"))),
                                0,
                                "1",
                                "Missing Primitive Example Field on Test Complex 1",
                                "",
                                ""
                            )
                        )
                    )
                )
            ))
        );
        this.thenObjectApproved(actual);
    }

    @Test
    void itCanAddToUnresolvableFieldsFromComplexParentOfComplexParent() {
        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
            new StructureDefinitionData(
                "TestComplex3",
                "http://test.url/for/complex3",
                "draft",
                "Structure Definition for test complex 3",
                "complex-type",
                false,
                "TestComplex3",
                "http://test.url/for/TestComplex2",
                new UniqueIdentifier("TestComplex2"),
                new ArrayList<>()
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "TestComplex2",
                "http://test.url/for/complex2",
                "draft",
                "Structure Definition for test complex 2",
                "complex-type",
                false,
                "TestComplex2",
                "http://test.url/for/TestComplex1",
                new UniqueIdentifier("TestComplex1"),
                new ArrayList<>()
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "TestComplex1",
                "http://test.url/for/complex",
                "draft",
                "Structure Definition for test complex 1",
                "complex-type",
                false,
                "TestComplex1",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplex1.missingPrimitiveField",
                            new ArrayList<>(List.of(new ElementDefinitionType("missingPrimitiveType"))),
                            0,
                            "1",
                            "Missing Primitive Example Field on Test Complex 1",
                            "",
                            ""
                        )
                    )
                )
            )
        );
        definitions.add(
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
                new ArrayList<>()
            )
        );
        var actual = this.structureDefinitionToSSMapper.map(definitions);
        this.thenObjectApproved(actual);
    }

    @Test
    void itAddsToUnresolvableTypesNotDirectlyDependentOnMissingType() {
        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
            new StructureDefinitionData(
                "TestComplex1",
                "http://test.url/for/complex",
                "draft",
                "This type should be unresolvable",
                "complex-type",
                false,
                "TestComplex1",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplex1.someUnresolvableField",
                            new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                            0,
                            "1",
                            "Field with missing dependency",
                            "",
                            ""
                        )
                    )
                )
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "TestComplex2",
                "http://test.url/for/complex2",
                "draft",
                "This type should be consequently also unresolvable",
                "complex-type",
                false,
                "TestComplex2",
                null,
                null,
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplex2.otherComplexTypeField",
                            new ArrayList<>(List.of(new ElementDefinitionType("TestComplex1"))),
                            0,
                            "1",
                            "Dependency on indirectly unresolvable type",
                            "",
                            ""
                        )
                    )
                )
            )
        );
        var actual = this.structureDefinitionToSSMapper.map(definitions);
        this.thenObjectApproved(actual);
    }

    @Test
    void itAddsTypeToUnresolvableWhenParentIsMissing() {
        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
            new StructureDefinitionData(
                "testPrimitive",
                "http://test.url/for/testPrimitive",
                "draft",
                "Structure Definition for test primitive",
                "primitive-type",
                false,
                "testPrimitive",
                null,
                null,
                new ArrayList<>()
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "TestComplex1",
                "http://test.url/for/complex",
                "draft",
                "Structure Definition for test complex 1",
                "complex-type",
                false,
                "TestComplex1",
                "http://test.url/for/TestComplex2",
                new UniqueIdentifier("TestComplex2"),
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplex1.somePrimitiveField",
                            new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                            0,
                            "1",
                            "Primitive Example Field on Test Complex 1",
                            "",
                            ""
                        )
                    )
                )
            )
        );
        definitions.add(
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
                new ArrayList<>()
            )
        );
        var actual = this.structureDefinitionToSSMapper.map(definitions);
        this.thenObjectApproved(actual);
    }

    @Test
    void itAddsOnlyNewlyMappedTypes_WithExistingContext() {
        var existingSchema = new StructureSchema(
            new PrimitiveStructureType(
                "primitive",
                "",
                false,
                ""
            ),
            new ComplexStructureType(
                "TestComplex2",
                new HashMap<>(),
                "",
                "",
                false
            ),
            new ComplexStructureType(
                "Element",
                new HashMap<>(),
                "",
                "",
                false
            )
        );

        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
            new StructureDefinitionData(
                "TestComplex1",
                "http://test.url/for/complex",
                "draft",
                "Structure Definition for test complex 1",
                "complex-type",
                false,
                "TestComplex1",
                "http://test.url/for/TestComplex2",
                new UniqueIdentifier("TestComplex2"),
                new ArrayList<>(
                    List.of(
                        new ElementDefinition(
                            "TestComplex1.somePrimitiveField",
                            new ArrayList<>(List.of(new ElementDefinitionType("primitive"))),
                            0,
                            "1",
                            "Primitive Example Field on Test Complex 1",
                            "",
                            ""
                        )
                    )
                )
            )
        );
        var actual = this.structureDefinitionToSSMapper.map(
            definitions,
            existingSchema
        );
        this.thenObjectApproved(actual);
    }

    @Test
    void itAddNewPrimitiveTypeCorrectly_WithExistingContext() {
        var existingSchema = new StructureSchema(
            new PrimitiveStructureType(
                "primitive",
                "",
                false,
                ""
            ),
            new ComplexStructureType(
                "Element",
                new HashMap<>(),
                "",
                "",
                false
            )
        );

        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
            new StructureDefinitionData(
                "newPrimitive",
                "http://test.url/for/newPrimitive",
                "draft",
                "Structure Definition for test complex 1",
                "primitive-type",
                false,
                "newPrimitive",
                "http://test.url/for/primitive",
                new UniqueIdentifier("primitive"),
                new ArrayList<>()
            )
        );
        var actual = this.structureDefinitionToSSMapper.map(definitions, existingSchema);
        this.thenObjectApproved(actual);
    }

    @Test
    void itAddsExistingInheritedPrimitiveTypeCorrectly_WithExistingContext() {
        var existingSchema = new StructureSchema(
            new PrimitiveStructureType(
                "primitive",
                "",
                false,
                "parentPrimitive"
            ),
            new PrimitiveStructureType(
                "parentPrimitive",
                "",
                false,
                ""
            ),
            new ComplexStructureType(
                "Element",
                new HashMap<>(),
                "",
                "",
                false
            )
        );

        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
            new StructureDefinitionData(
                "primitive",
                "http://test.url/for/primitive",
                "draft",
                "Overridden primitive",
                "primitive-type",
                false,
                "primitive",
                "http://test.url/for/parentPrimitive",
                new UniqueIdentifier("parentPrimitive"),
                new ArrayList<>()
            )
        );
        var actual = this.structureDefinitionToSSMapper.map(definitions, existingSchema);
        this.thenObjectApproved(actual);
    }

    @Test
    void itCanMapSameTypeIntoContextWithInheritedPrimitives() {
        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
            new StructureDefinitionData(
                "primitive",
                "http://test.url/for/primitive",
                "draft",
                "Just primitive",
                "primitive-type",
                false,
                "primitive",
                "http://test.url/for/parentPrimitive",
                new UniqueIdentifier("parentPrimitive"),
                new ArrayList<>()
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "parentPrimitive",
                "http://test.url/for/parentPrimitive",
                "draft",
                "Parent of primitive primitive",
                "primitive-type",
                false,
                "parentPrimitive",
                "http://test.url/for/secondParentPrimitive",
                new UniqueIdentifier("secondParentPrimitive"),
                new ArrayList<>()
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "secondParentPrimitive",
                "http://test.url/for/secondParentPrimitive",
                "draft",
                "Parent of parent of primitive primitive",
                "primitive-type",
                false,
                "secondParentPrimitive",
                null,
                null,
                new ArrayList<>()
            )
        );
        definitions.add(
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
                new ArrayList<>()
            )
        );

        var intermediate = this.structureDefinitionToSSMapper.map(definitions);

        var actual = this.structureDefinitionToSSMapper.map(new StructureDefinitionData(
            "Element",
            "http://test.url/for/Element",
            "draft",
            "Structure Definition for Element",
            "complex-type",
            false,
            "Element",
            null,
            null,
            new ArrayList<>()
        ), intermediate.structureSchema());

        this.thenObjectApproved(actual);
    }

    @Test
    void itMapsInheritedBoxedPrimitives() {
        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
            new StructureDefinitionData(
                "primitive",
                "http://test.url/for/primitive",
                "draft",
                "Just primitive",
                "primitive-type",
                false,
                "primitive",
                "http://test.url/for/parentPrimitive",
                new UniqueIdentifier("parentPrimitive"),
                new ArrayList<>()
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "parentPrimitive",
                "http://test.url/for/parentPrimitive",
                "draft",
                "Parent of primitive primitive",
                "primitive-type",
                false,
                "parentPrimitive",
                null,
                null,
                new ArrayList<>()
            )
        );
        definitions.add(
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
                new ArrayList<>()
            )
        );

        var actual = this.structureDefinitionToSSMapper.map(definitions);

        this.thenObjectApproved(actual);
    }

    @Test
    void itFailsWhenMissingElementWhenMappingPrimitive() {
        var definitions = new ArrayList<StructureDefinitionData>();
        definitions.add(
            new StructureDefinitionData(
                "primitive",
                "http://test.url/for/primitive",
                "draft",
                "Just primitive",
                "primitive-type",
                false,
                "primitive",
                "http://test.url/for/parentPrimitive",
                new UniqueIdentifier("parentPrimitive"),
                new ArrayList<>()
            )
        );
        definitions.add(
            new StructureDefinitionData(
                "parentPrimitive",
                "http://test.url/for/parentPrimitive",
                "draft",
                "Parent of primitive primitive",
                "primitive-type",
                false,
                "parentPrimitive",
                null,
                null,
                new ArrayList<>()
            )
        );
        var actual = this.structureDefinitionToSSMapper.map(definitions);

        this.thenObjectApproved(actual);
    }

    @Test
    void itFailsToMapPrimitiveTypeWhenElementIsUnresolvable() {
        var actual = this.structureDefinitionToSSMapper.map(
            new ArrayList<>(List.of(
                new StructureDefinitionData(
                    "testPrimitive",
                    "http://test.url/for/testPrimitive",
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
                    new ArrayList<>(List.of(
                        new ElementDefinition(
                            "Element.failingField",
                            new ArrayList<>(List.of(new ElementDefinitionType("DeeperType"))),
                            0,
                            "1",
                            "Field with failing type",
                            "",
                            ""
                        ))
                    )
                ),
                new StructureDefinitionData(
                    "DeeperType",
                    "http://test.url/for/DeeperType",
                    "draft",
                    "Structure Definition for DeeperType",
                    "complex-type",
                    false,
                    "DeeperType",
                    null,
                    null,
                    new ArrayList<>(List.of(
                        new ElementDefinition(
                            "DeeperType.failingField",
                            new ArrayList<>(List.of(new ElementDefinitionType("SuperDeepType"))),
                            0,
                            "1",
                            "Field with failing type",
                            "",
                            ""
                        ))
                    )
                ),
                new StructureDefinitionData(
                    "SuperDeepType",
                    "http://test.url/for/SuperDeepType",
                    "draft",
                    "Structure Definition for SuperDeepType",
                    "complex-type",
                    false,
                    "SuperDeepType",
                    null,
                    null,
                    new ArrayList<>(List.of(
                        new ElementDefinition(
                            "SuperDeepType.failingField",
                            new ArrayList<>(List.of(new ElementDefinitionType("HorseCockDeepType"))),
                            0,
                            "1",
                            "Field with failing type",
                            "",
                            ""
                        ))
                    )
                ),
                new StructureDefinitionData(
                    "HorseCockDeepType",
                    "http://test.url/for/HorseCockDeepType",
                    "draft",
                    "Structure Definition for HorseCockDeepType",
                    "complex-type",
                    false,
                    "HorseCockDeepType",
                    null,
                    null,
                    new ArrayList<>(List.of(
                        new ElementDefinition(
                            "HorseCockDeepType.failingField",
                            new ArrayList<>(List.of(new ElementDefinitionType("missingType"))),
                            0,
                            "1",
                            "Field with failing type",
                            "",
                            ""
                        ))
                    )
                )
            ))
        );
        this.thenObjectApproved(actual);
    }


    @Test
    void itCanMergeSameTypesInFailedQueue() {
        var actual = this.structureDefinitionToSSMapper.map(
            new ArrayList<>(List.of(
                new StructureDefinitionData(
                    "TestComplex",
                    "http://test.url/for/TestComplex",
                    "draft",
                    "Structure Definition for test TestComplex",
                    "complex-type",
                    false,
                    "TestComplex",
                    null,
                    null,
                    new ArrayList<>(
                        List.of(
                            new ElementDefinition(
                                "TestComplex.someOtherPrimitiveField",
                                new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                                0,
                                "1",
                                "Primitive Example Field on Test Complex 1",
                                "",
                                ""
                            )
                        )
                    )
                ),
                new StructureDefinitionData(
                    "TestComplex",
                    "http://test.url/for/TestComplex",
                    "draft",
                    "Structure Definition for test TestComplex 2",
                    "complex-type",
                    false,
                    "TestComplex",
                    null,
                    null,
                    new ArrayList<>(
                        List.of(
                            new ElementDefinition(
                                "TestComplex.someOtherPrimitiveField2",
                                new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                                0,
                                "1",
                                "Primitive Example Field on Test Complex 1",
                                "",
                                ""
                            )
                        )
                    )
                ),
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
                    "http://test.url/for/element",
                    "draft",
                    "Element test structure",
                    "complex-type",
                    false,
                    "Element",
                    null,
                    null,
                    new ArrayList<>()
                )
            ))
        );

        this.thenObjectApproved(actual);
    }

    @Test
    void itFailsToMapComplexTypeWhenParentIsUnresolvable() {
        var actual = this.structureDefinitionToSSMapper.map(
            new ArrayList<>(List.of(
                new StructureDefinitionData(
                    "FailingComplexThroughParent",
                    "http://test.url/for/FailingComplexThroughParent",
                    "draft",
                    "Structure Definition for test primitive",
                    "complex-type",
                    false,
                    "FailingComplexThroughParent",
                    "http://test.url/for/Element",
                    new UniqueIdentifier("Element"),
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
                    new ArrayList<>(List.of(
                        new ElementDefinition(
                            "Element.failingField",
                            new ArrayList<>(List.of(new ElementDefinitionType("DeeperType"))),
                            0,
                            "1",
                            "Field with failing type",
                            "",
                            ""
                        ))
                    )
                ),
                new StructureDefinitionData(
                    "DeeperType",
                    "http://test.url/for/DeeperType",
                    "draft",
                    "Structure Definition for DeeperType",
                    "complex-type",
                    false,
                    "DeeperType",
                    null,
                    null,
                    new ArrayList<>(List.of(
                        new ElementDefinition(
                            "DeeperType.failingField",
                            new ArrayList<>(List.of(new ElementDefinitionType("SuperDeepType"))),
                            0,
                            "1",
                            "Field with failing type",
                            "",
                            ""
                        ))
                    )
                ),
                new StructureDefinitionData(
                    "SuperDeepType",
                    "http://test.url/for/SuperDeepType",
                    "draft",
                    "Structure Definition for SuperDeepType",
                    "complex-type",
                    false,
                    "SuperDeepType",
                    null,
                    null,
                    new ArrayList<>(List.of(
                        new ElementDefinition(
                            "SuperDeepType.failingField",
                            new ArrayList<>(List.of(new ElementDefinitionType("HorseCockDeepType"))),
                            0,
                            "1",
                            "Field with failing type",
                            "",
                            ""
                        ))
                    )
                ),
                new StructureDefinitionData(
                    "HorseCockDeepType",
                    "http://test.url/for/HorseCockDeepType",
                    "draft",
                    "Structure Definition for HorseCockDeepType",
                    "complex-type",
                    false,
                    "HorseCockDeepType",
                    null,
                    null,
                    new ArrayList<>(List.of(
                        new ElementDefinition(
                            "HorseCockDeepType.failingField",
                            new ArrayList<>(List.of(new ElementDefinitionType("missingType"))),
                            0,
                            "1",
                            "Field with failing type",
                            "",
                            ""
                        ))
                    )
                )
            ))
        );
        this.thenObjectApproved(actual);
    }

    @Test
    void itCanGenerateMetaSchemaWithSchemaContext() {
        var existingStructureDefinitions = this.getStructureDefinitionsForSystem();
        var metaDefinition = existingStructureDefinitions.stream()
            .filter(definition -> definition.getId().equals("Meta"))
            .findAny()
            .orElseThrow(() -> new RuntimeException(
                "There should be structure definition for Meta"
            ));
        var existingSchema =
            this.structureDefinitionToSSMapper.map(existingStructureDefinitions).structureSchema();

        var expected =
            this.structureDefinitionToSSMapper.map(existingStructureDefinitions).structureSchema();
        var actual = this.structureDefinitionToSSMapper.map(
            metaDefinition,
            existingSchema
        ).structureSchema();
        this.thenObjectsEquals(
            expected,
            actual
        );
    }

    @Test
    void itThrowsWhenFieldTypeDefinitionIsMissing() {
        var existingStructureDefinitions = this.getStructureDefinitionsForSystem();
        Assertions.assertEquals(
            55,
            existingStructureDefinitions.size()
        );
        var incompleteExistingStructureDefinitions = existingStructureDefinitions.stream()
            .filter(structure -> !structure.getId().equals("string"))
            .toList();
        var actual = this.structureDefinitionToSSMapper.map(incompleteExistingStructureDefinitions);
        Assertions.assertEquals(
            54,
            actual.unresolvableTypes().size()
        );
        Assertions.assertEquals(
            0,
            actual.successfullyMappedTypes().size()
        );
    }

    @Test
    void itAddsToFailedTypesWhenParentCanNotBeResolved() {
        var existingStructureDefinitions = this.getStructureDefinitionsForSystem();
        var incompleteExistingStructureDefinitions = existingStructureDefinitions.stream()
            .filter(structure -> !structure.getId().equals("DomainResource"))
            .toList();
        var outcome = this.structureDefinitionToSSMapper.map(incompleteExistingStructureDefinitions);
        Assertions.assertEquals(
            8,
            outcome.unresolvableTypes().size()
        );
    }

    @Test
    void itCanGenerateAddNewStructureDefinitionToExistingSchema() {
        var structureDefinitionSerializationType = "StructureDefinition";
        var existingStructureDefinitions = this.getStructureDefinitionsForSystem();
        var incompleteExistingStructureDefinitions = existingStructureDefinitions.stream()
            .filter(structure -> !structure.getId().equals(structureDefinitionSerializationType))
            .toList();
        var structureDefinitionDefinition = existingStructureDefinitions.stream()
            .filter(structureDefinitionDTO -> structureDefinitionDTO.getId()
                .equals(structureDefinitionSerializationType))
            .findAny();
        var firstOutcome = this.structureDefinitionToSSMapper.map(
            incompleteExistingStructureDefinitions
        );
        var context = firstOutcome.structureSchema();
        Assertions.assertFalse(context.containsDefinition(structureDefinitionSerializationType));
        var structureWithFailed = new ArrayList<>(
            firstOutcome.unresolvableTypes().stream()
                .map(UnresolvableType::structureDefinitionData)
                .toList()
        );
        structureWithFailed.add(structureDefinitionDefinition.get());
        var actual = this.structureDefinitionToSSMapper.map(
            structureWithFailed,
            context
        ).structureSchema();
        Assertions.assertTrue(actual.containsDefinition(structureDefinitionSerializationType));
    }

    @Test
    void itCanGenerateAddNewStructureDefinitionWithStructuresInAnyOrder() {
        var structureDefinitionSerializationType = "StructureDefinition";
        var existingStructureDefinitions = this.getStructureDefinitionsForSystem();
        var incompleteExistingStructureDefinitions = existingStructureDefinitions.stream()
            .filter(structure -> !structure.getId().equals(structureDefinitionSerializationType))
            .sorted(Comparator.comparing(StructureDefinitionData::getId))
            .toList();
        var structureDefinitionDefinition = existingStructureDefinitions.stream()
            .filter(structureDefinitionDTO -> structureDefinitionDTO.getId()
                .equals(structureDefinitionSerializationType))
            .findAny();
        var firstOutcome = this.structureDefinitionToSSMapper.map(
            incompleteExistingStructureDefinitions
        );
        var context = firstOutcome.structureSchema();
        Assertions.assertFalse(context.containsDefinition(structureDefinitionSerializationType));
        var structureWithFailed = new ArrayList<>(
            firstOutcome.unresolvableTypes().stream()
                .map(UnresolvableType::structureDefinitionData)
                .toList()
        );
        structureWithFailed.add(structureDefinitionDefinition.get());
        var mappingOutcome = this.structureDefinitionToSSMapper.map(
            structureWithFailed,
            context
        );
        var actual = mappingOutcome.structureSchema();
        Assertions.assertTrue(actual.containsDefinition(structureDefinitionSerializationType));
    }

    @Test
    void itCanMapSystemModuleInRandomOrder() {
        var ordered = this.getStructureDefinitionsForSystem()
            .stream()
            .sorted(Comparator.comparing(StructureDefinitionData::getId))
            .toList();
        var actual = this.structureDefinitionToSSMapper.map(ordered);
        this.thenObjectApproved(actual.successfullyMappedTypes());
    }

    @Test
    void itCanGenerateCoreSchema() {
        var actual = this.structureDefinitionToSSMapper
            .map(this.getStructureDefinitionsForSystem())
            .structureSchema();
        this.thenObjectApproved(actual);
    }

    @Test
    void itCanMapStructureDefinitionWithContentReference() {
        var structureDefinitions = new ArrayList<>(
            this.getStructureDefinitionsForSystem()
        );
        structureDefinitions.add(
            new StructureDefinitionData(
                "ComplexTypeWithContentReference",
                "http://test.url/for/ComplexTypeWithContentReference",
                "draft",
                "Structure Definition for ComplexTypeWithContentReference",
                "complex-type",
                false,
                "ComplexTypeWithContentReference",
                null,
                null,
                new ArrayList<>(List.of(
                    new ElementDefinition(
                        "ComplexTypeWithContentReference.backBone",
                        new ArrayList<>(List.of(new ElementDefinitionType("BackboneElement"))),
                        0,
                        "1",
                        "Backbone element on ComplexTypeWithContentReference",
                        "",
                        ""
                    ),
                    new ElementDefinition(
                        "ComplexTypeWithContentReference.backBone.stringField",
                        new ArrayList<>(List.of(new ElementDefinitionType("string"))),
                        1,
                        "1",
                        "String field of backbone element on ComplexTypeWithContentReference",
                        "",
                        ""
                    ),
                    new ElementDefinition(
                        "ComplexTypeWithContentReference.referenced",
                        new ArrayList<>(),
                        0,
                        "1",
                        "Backbone element on ComplexTypeWithContentReference",
                        "",
                        "",
                        "#ComplexTypeWithContentReference.backBone"
                    )
                ))
            )
        );
        var actual = this.structureDefinitionToSSMapper
            .map(structureDefinitions)
            .structureSchema();

        this.thenObjectApproved(actual);
    }

    private List<StructureDefinitionData> getStructureDefinitionsForSystem() {
        if (this.structureDefinitionDataList != null) {
            return this.structureDefinitionDataList;
        }
        this.structureDefinitionDataList = this.structureDefinitionLoader.load();
        return this.structureDefinitionDataList;
    }
}