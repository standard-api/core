package ai.stapi.schema.structureSchemaProvider;

import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.schema.fixtures.TestSystemModelDefinitionsLoader;
import ai.stapi.schema.structuredefinition.loader.AdHocStructureDefinitionLoader;
import ai.stapi.schema.scopeProvider.ScopeCacher;
import ai.stapi.schema.scopeProvider.ScopeOptions;
import ai.stapi.schema.scopeProvider.ScopeProvider;
import ai.stapi.schema.structureSchemaMapper.StructureDefinitionToSSMapper;
import ai.stapi.schema.structureSchemaProvider.exception.CannotProvideStructureSchema;
import ai.stapi.schema.structuredefinition.ElementDefinition;
import ai.stapi.schema.structuredefinition.ElementDefinitionType;
import ai.stapi.schema.structuredefinition.StructureDefinitionData;
import ai.stapi.schema.structuredefinition.loader.StructureDefinitionLoader;
import ai.stapi.schema.test.integration.IntegrationTestCase;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

class DefaultStructureSchemaProviderTest extends IntegrationTestCase {

  public static final ScopeOptions SCOPE_OPTIONS = new ScopeOptions(
      "DefaultStructureSchemaProviderTest",
      ScopeOptions.TEST_TAG
  );
  @Autowired
  private StructureDefinitionToSSMapper structureDefinitionToSSMapper;

  @Autowired
  private StructureDefinitionLoader structureDefinitionLoader;

  @Autowired
  private AdHocStructureDefinitionLoader adHocStructureDefinitionLoader;

  @Autowired
  private ScopeProvider scopeProvider;

  private DefaultStructureSchemaProvider structureSchemaProvider;


  @BeforeAll
  public static void beforeAll(
      @Autowired ScopeProvider scopeProvider
  ) {
    scopeProvider.set(SCOPE_OPTIONS);
  }

  @BeforeEach
  public void beforeEach() {
    this.structureSchemaProvider = new DefaultStructureSchemaProvider(
        this.structureDefinitionToSSMapper,
        this.structureDefinitionLoader,
        new ScopeCacher(this.scopeProvider)
    );
  }

  @Test
  void itCanMapEverything() {
    this.scopeProvider.set(
        new ScopeOptions(
            TestSystemModelDefinitionsLoader.SCOPE,
            TestSystemModelDefinitionsLoader.TAG
        )
    );
    var definitions = this.adHocStructureDefinitionLoader.load();
    this.scopeProvider.set(SCOPE_OPTIONS);
    definitions.forEach(def -> this.structureSchemaProvider.add(def));
    var schema = this.structureSchemaProvider.provideSchema();
    this.thenObjectApproved(schema);
  }

  @Test
  void itThrowsCorrectExceptionWhenDesiredTypeIsMissingInProvider() {
    this.structureSchemaProvider.add(
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
    Executable throwable = () -> this.structureSchemaProvider.provideSpecific("testPrimitive");
    this.thenExceptionMessageApproved(CannotProvideStructureSchema.class, throwable);
  }

  @Test
  void itCanAddPrimitiveStructureDefinition() throws CannotProvideStructureSchema {
    this.structureSchemaProvider.add(
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
            "New description",
            "complex-type",
            true,
            "Element",
            null,
            null,
            new ArrayList<>()
        )
    );
    var actual = this.structureSchemaProvider.provideSpecific("testPrimitive");
    this.thenObjectApproved(actual);
  }

  @Test
  void itCanResolveAnonymousElement() {
    this.structureSchemaProvider.add(
        new StructureDefinitionData(
            "ComplexContainingAnonymous",
            "http://test.url/for/complex",
            "draft",
            "Structure Definition for complex containing anonymous",
            "complex-type",
            false,
            "ComplexContainingAnonymous",
            null,
            null,
            new ArrayList<>(
                List.of(
                    new ElementDefinition(
                        "TestComplex.testAnonymous.test1",
                        new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                        1,
                        "1",
                        "Example Field A",
                        "",
                        ""
                    ),
                    new ElementDefinition(
                        "TestComplex.testAnonymous",
                        new ArrayList<>(List.of(new ElementDefinitionType("Element"))),
                        0,
                        "1",
                        "Test anonymous object",
                        "",
                        ""
                    ),
                    new ElementDefinition(
                        "TestComplex.testAnonymous.test2",
                        new ArrayList<>(List.of(new ElementDefinitionType("testPrimitive"))),
                        1,
                        "1",
                        "Example Field B",
                        "",
                        ""
                    )
                )
            )
        )
    );
    this.structureSchemaProvider.add(
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
    );
    this.structureSchemaProvider.add(
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
    var actual = this.structureSchemaProvider.provideSchema();
    this.thenObjectApproved(actual);
  }

  @Test
  void itCanAddComplexAndPrimitiveLater() throws CannotProvideStructureSchema {
    this.structureSchemaProvider.add(
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
    this.structureSchemaProvider.add(
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
    );
    var actual = this.structureSchemaProvider.provideSpecific("TestComplex");
    this.thenObjectApproved(actual);
  }

  @Test
  void itCanAddTwoComplexWhichReferenceEachOtherThroughField() {
    this.structureSchemaProvider.add(
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
    this.structureSchemaProvider.add(
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
    this.structureSchemaProvider.add(
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
    );
    var actual = this.structureSchemaProvider.provideSchema();
    this.thenObjectApproved(actual);
  }

  @Test
  void itCanAddTwoSameComplexTypesAndMergedFieldsIncludingAnonymous() {
    var definitions = new ArrayList<StructureDefinitionData>();
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
    definitions.forEach(
        structureDefinitionDTO -> structureSchemaProvider.add(structureDefinitionDTO)
    );
    this.thenObjectApproved(structureSchemaProvider.provideSchema());
  }

  @Test
  void itCanAddTwoSameComplexTypesWhichExtendItsFields() {
    this.structureSchemaProvider.add(
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
    this.structureSchemaProvider.add(
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
        )
    );
    this.structureSchemaProvider.add(
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
    );
    var actual = this.structureSchemaProvider.provideSchema();
    this.thenObjectApproved(actual);
  }

  @Test
  void itCanMergeSameTypesInFailedQueue() {
    this.structureSchemaProvider.add(
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
        )
    );
    this.structureSchemaProvider.add(
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
        )
    );
    this.structureSchemaProvider.add(
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
    this.structureSchemaProvider.add(
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
    );
    var actual = this.structureSchemaProvider.provideSchema();
    this.thenObjectApproved(actual);
  }

  @Test
  void itCanAddComplexAndItsParentComplexLater() {
    this.structureSchemaProvider.add(
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
    this.structureSchemaProvider.add(
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
    this.structureSchemaProvider.add(
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
                    )
                )
            )
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
    );
    var actual = this.structureSchemaProvider.provideSchema();
    this.thenObjectApproved(actual);
  }

  @Test
  void itCanAddComplexAndItsParentComplexLaterAndTheyReferenceEachOther() {
    this.structureSchemaProvider.add(
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
    this.structureSchemaProvider.add(
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
    this.structureSchemaProvider.add(
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
    );
    var actual = this.structureSchemaProvider.provideSchema();
    this.thenObjectApproved(actual);
  }

  @Test
  void itCanAddComplexAndItsParentComplexLaterAndTheyReferenceEachOtherInOppositeOrder() {
    this.structureSchemaProvider.add(
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
    this.structureSchemaProvider.add(
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
    this.structureSchemaProvider.add(
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
    );
    var actual = this.structureSchemaProvider.provideSchema();
    this.thenObjectApproved(actual);
  }
}