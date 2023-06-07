package ai.stapi.graphsystem.commandvalidation.infrastructure.operationdefinition;

import ai.stapi.graphsystem.aggregatedefinition.infrastructure.NullAggregateDefinitionProvider;
import ai.stapi.graphsystem.commandvalidation.model.CommandConstrainViolation;
import ai.stapi.graphsystem.commandvalidation.model.exceptions.CannotValidateCommand;
import ai.stapi.graphsystem.messaging.command.DynamicCommand;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import ai.stapi.graphsystem.operationdefinition.model.testImplementations.TestOperationDefinitionProvider;
import ai.stapi.test.SystemSchemaIntegrationTestCase;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({TestOperationDefinitionProvider.class, NullAggregateDefinitionProvider.class})
class OperationDefinitionCommandValidatorTest extends SystemSchemaIntegrationTestCase {

  @Autowired
  private OperationDefinitionCommandValidator commandValidator;

  @Test
  void itWillThrowExceptionWhenOperationDefinitionDoesNotExist() {
    Executable throwable = () -> this.commandValidator.validate(
        new DynamicCommand(
            UniversallyUniqueIdentifier.randomUUID(),
            "InvalidCommandName",
            Map.of()
        )
    );

    this.thenExceptionMessageApproved(CannotValidateCommand.class, throwable);
  }

  @Test
  void itShouldReturnExtraFieldViolationWhenThereIsFieldNotDefinedInOperationDefinition() {
    var actual = this.commandValidator.validate(
        new DynamicCommand(
            UniversallyUniqueIdentifier.randomUUID(),
            TestOperationDefinitionProvider.EXAMPLE_TEST_COMMAND,
            Map.of(
                "extraField", "Some Irrelevant Value",
                TestOperationDefinitionProvider.EXAMPLE_REQUIRED_STRING_SINGLE_VALUE_FIELD,
                "Example Name"
            )
        )
    );

    this.thenViolationsApproved(actual);
  }

  @Test
  void itShouldReturnMissingRequiredFieldViolationWhenFieldIsCompletelyMissing() {
    var actual = this.commandValidator.validate(
        new DynamicCommand(
            UniversallyUniqueIdentifier.randomUUID(),
            TestOperationDefinitionProvider.EXAMPLE_TEST_COMMAND,
            Map.of()
        )
    );

    this.thenViolationsApproved(actual);
  }

  @Test
  void itShouldReturnMissingRequiredFieldViolationWhenItHasNullValue() {
    var payload = new HashMap<String, Object>();
    payload.put(TestOperationDefinitionProvider.EXAMPLE_REQUIRED_STRING_SINGLE_VALUE_FIELD, null);
    var actual = this.commandValidator.validate(
        new DynamicCommand(
            UniversallyUniqueIdentifier.randomUUID(),
            TestOperationDefinitionProvider.EXAMPLE_TEST_COMMAND,
            payload
        )
    );

    this.thenViolationsApproved(actual);
  }

  @Test
  void itShouldReturnFieldStructureTypeViolationWhenExpectedSingleValueButWasList() {
    var actual = this.commandValidator.validate(
        new DynamicCommand(
            UniversallyUniqueIdentifier.randomUUID(),
            TestOperationDefinitionProvider.EXAMPLE_TEST_COMMAND,
            Map.of(
                TestOperationDefinitionProvider.EXAMPLE_REQUIRED_STRING_SINGLE_VALUE_FIELD,
                List.of("some", "values")
            )
        )
    );

    this.thenViolationsApproved(actual);
  }

  @Test
  void itShouldReturnFieldStructureTypeViolationWhenExpectedListButWasSingleValue() {
    var actual = this.commandValidator.validate(
        new DynamicCommand(
            UniversallyUniqueIdentifier.randomUUID(),
            TestOperationDefinitionProvider.EXAMPLE_TEST_COMMAND,
            Map.of(
                TestOperationDefinitionProvider.EXAMPLE_INTEGER_LIST_FIELD, 10,
                TestOperationDefinitionProvider.EXAMPLE_REQUIRED_STRING_SINGLE_VALUE_FIELD,
                "Example Name"
            )
        )
    );

    this.thenViolationsApproved(actual);
  }

  @Test
  void itShouldNotReturnFieldStructureTypeViolationWhenExpectedOptionalListButWasNull() {
    var payload = new HashMap<String, Object>();
    payload.put(TestOperationDefinitionProvider.EXAMPLE_INTEGER_LIST_FIELD, null);
    payload.put(TestOperationDefinitionProvider.EXAMPLE_REQUIRED_STRING_SINGLE_VALUE_FIELD,
        "Example Name");
    var actual = this.commandValidator.validate(
        new DynamicCommand(
            UniversallyUniqueIdentifier.randomUUID(),
            TestOperationDefinitionProvider.EXAMPLE_TEST_COMMAND,
            payload
        )
    );

    this.thenViolationsApproved(actual);
  }

  @Test
  void itShouldReturnFieldCardinalityViolationWhenActualSizeIsBiggerThanMax() {
    var actual = this.commandValidator.validate(
        new DynamicCommand(
            UniversallyUniqueIdentifier.randomUUID(),
            TestOperationDefinitionProvider.EXAMPLE_TEST_COMMAND,
            Map.of(
                TestOperationDefinitionProvider.EXAMPLE_INTEGER_LIST_FIELD,
                List.of(0, 1, 2, 3, 4, 5),
                TestOperationDefinitionProvider.EXAMPLE_REQUIRED_STRING_SINGLE_VALUE_FIELD,
                "Example Name"
            )
        )
    );

    this.thenViolationsApproved(actual);
  }

  @Test
  void itShouldReturnFieldCardinalityViolationWhenActualSizeIsLowerThanMin() {
    var actual = this.commandValidator.validate(
        new DynamicCommand(
            UniversallyUniqueIdentifier.randomUUID(),
            TestOperationDefinitionProvider.ANOTHER_EXAMPLE_TEST_COMMAND,
            Map.of(
                TestOperationDefinitionProvider.EXAMPLE_REQUIRED_STRING_LIST_FIELD,
                List.of("tag1", "tag2")
            )
        )
    );

    this.thenViolationsApproved(actual);
  }

  @Test
  void itShouldReturnInvalidDataTypeViolation() {
    var actual = this.commandValidator.validate(
        new DynamicCommand(
            UniversallyUniqueIdentifier.randomUUID(),
            TestOperationDefinitionProvider.EXAMPLE_TEST_COMMAND,
            Map.of(
                TestOperationDefinitionProvider.EXAMPLE_REQUIRED_STRING_SINGLE_VALUE_FIELD, 50
            )
        )
    );

    this.thenViolationsApproved(actual);
  }

  @Test
  void itShouldReturnInvalidDataTypeViolationForValuesInList() {
    var actual = this.commandValidator.validate(
        new DynamicCommand(
            UniversallyUniqueIdentifier.randomUUID(),
            TestOperationDefinitionProvider.EXAMPLE_TEST_COMMAND,
            Map.of(
                TestOperationDefinitionProvider.EXAMPLE_REQUIRED_STRING_SINGLE_VALUE_FIELD, "name",
                TestOperationDefinitionProvider.EXAMPLE_INTEGER_LIST_FIELD, List.of("A", 2, "B")
            )
        )
    );

    this.thenViolationsApproved(actual);
  }

  @Test
  void itShouldReturnInvalidReferenceTypeWhenItsNotObject() {
    var actual = this.commandValidator.validate(
        new DynamicCommand(
            UniversallyUniqueIdentifier.randomUUID(),
            TestOperationDefinitionProvider.EXAMPLE_TEST_COMMAND,
            Map.of(
                TestOperationDefinitionProvider.EXAMPLE_REQUIRED_STRING_SINGLE_VALUE_FIELD, "name",
                TestOperationDefinitionProvider.EXAMPLE_REFERENCE_FIELD, "Invalid Value"
            )
        )
    );

    this.thenViolationsApproved(actual);
  }

  @Test
  void itShouldReturnInvalidReferenceTypeWhenItHasMoreFields() {
    var invalidReferenceMap = new LinkedHashMap<String, Object>();
    invalidReferenceMap.put("id", "ExampleId");
    invalidReferenceMap.put("extraField", "Irrelevant Value");

    var actual = this.commandValidator.validate(
        new DynamicCommand(
            UniversallyUniqueIdentifier.randomUUID(),
            TestOperationDefinitionProvider.EXAMPLE_TEST_COMMAND,
            Map.of(
                TestOperationDefinitionProvider.EXAMPLE_REQUIRED_STRING_SINGLE_VALUE_FIELD, "name",
                TestOperationDefinitionProvider.EXAMPLE_REFERENCE_FIELD, invalidReferenceMap
            )
        )
    );

    this.thenViolationsApproved(actual);
  }

  @Test
  void itShouldReturnInvalidReferenceTypeWhenItIsMissingIdField() {
    var actual = this.commandValidator.validate(
        new DynamicCommand(
            UniversallyUniqueIdentifier.randomUUID(),
            TestOperationDefinitionProvider.EXAMPLE_TEST_COMMAND,
            Map.of(
                TestOperationDefinitionProvider.EXAMPLE_REQUIRED_STRING_SINGLE_VALUE_FIELD, "name",
                TestOperationDefinitionProvider.EXAMPLE_REFERENCE_FIELD, Map.of(
                    "irrelevantOtherField", "Irrelevant Value"
                )
            )
        )
    );

    this.thenViolationsApproved(actual);
  }

  @Test
  void itShouldReturnInvalidReferenceTypeWhenIdFieldIsInvalidType() {
    var actual = this.commandValidator.validate(
        new DynamicCommand(
            UniversallyUniqueIdentifier.randomUUID(),
            TestOperationDefinitionProvider.EXAMPLE_TEST_COMMAND,
            Map.of(
                TestOperationDefinitionProvider.EXAMPLE_REQUIRED_STRING_SINGLE_VALUE_FIELD, "name",
                TestOperationDefinitionProvider.EXAMPLE_REFERENCE_FIELD, Map.of(
                    "id", 53
                )
            )
        )
    );

    this.thenViolationsApproved(actual);
  }

  @Test
  void itShouldReturnInvalidUnionMemberWhenItIsNotObject() {
    var actual = this.commandValidator.validate(
        new DynamicCommand(
            UniversallyUniqueIdentifier.randomUUID(),
            TestOperationDefinitionProvider.EXAMPLE_TEST_COMMAND,
            Map.of(
                TestOperationDefinitionProvider.EXAMPLE_REQUIRED_STRING_SINGLE_VALUE_FIELD, "name",
                TestOperationDefinitionProvider.EXAMPLE_UNION_FIELD, "Invalid Value"
            )
        )
    );

    this.thenViolationsApproved(actual);
  }

  @Test
  void itShouldReturnInvalidUnionMemberWhenItIsMissingSerializationTypeField() {
    var actual = this.commandValidator.validate(
        new DynamicCommand(
            UniversallyUniqueIdentifier.randomUUID(),
            TestOperationDefinitionProvider.EXAMPLE_TEST_COMMAND,
            Map.of(
                TestOperationDefinitionProvider.EXAMPLE_REQUIRED_STRING_SINGLE_VALUE_FIELD, "name",
                TestOperationDefinitionProvider.EXAMPLE_UNION_FIELD, Map.of(
                    "irrelevantField", "Irrelevant Value"
                )
            )
        )
    );

    this.thenViolationsApproved(actual);
  }

  @Test
  void itShouldReturnInvalidUnionMemberTypeViolation() {
    var actual = this.commandValidator.validate(
        new DynamicCommand(
            UniversallyUniqueIdentifier.randomUUID(),
            TestOperationDefinitionProvider.EXAMPLE_TEST_COMMAND,
            Map.of(
                TestOperationDefinitionProvider.EXAMPLE_REQUIRED_STRING_SINGLE_VALUE_FIELD, "name",
                TestOperationDefinitionProvider.EXAMPLE_UNION_FIELD, Map.of(
                    DynamicCommand.SERIALIZATION_TYPE_FIELD_NAME, "BoxedUrl",
                    "value", "http://some.url"
                )
            )
        )
    );

    this.thenViolationsApproved(actual);
  }

  @Test
  void itShouldReturnInvalidComplexTypeViolationWhenItIsNotObject() {
    var actual = this.commandValidator.validate(
        new DynamicCommand(
            UniversallyUniqueIdentifier.randomUUID(),
            TestOperationDefinitionProvider.EXAMPLE_TEST_COMMAND,
            Map.of(
                TestOperationDefinitionProvider.EXAMPLE_REQUIRED_STRING_SINGLE_VALUE_FIELD, "name",
                TestOperationDefinitionProvider.EXAMPLE_COMPLEX_FIELD, "Invalid Value"
            )
        )
    );

    this.thenViolationsApproved(actual);
  }

  @Test
  void itShouldValidateSingleValueNonUnionComplexType() {
    var actual = this.commandValidator.validate(
        new DynamicCommand(
            UniversallyUniqueIdentifier.randomUUID(),
            TestOperationDefinitionProvider.EXAMPLE_TEST_COMMAND,
            Map.of(
                TestOperationDefinitionProvider.EXAMPLE_REQUIRED_STRING_SINGLE_VALUE_FIELD, "name",
                TestOperationDefinitionProvider.EXAMPLE_COMPLEX_FIELD, Map.of(
                    "extraField", "Extra Value",
                    "value", "Wrong Value Type",
                    "unit", "Kg",
                    "extension", "Wrong Extension Type"
                )
            )
        )
    );

    this.thenViolationsApproved(actual);
  }

  @Test
  void itShouldValidateListNonUnionComplexType() {
    var actual = this.commandValidator.validate(
        new DynamicCommand(
            UniversallyUniqueIdentifier.randomUUID(),
            TestOperationDefinitionProvider.EXAMPLE_TEST_COMMAND,
            Map.of(
                TestOperationDefinitionProvider.EXAMPLE_REQUIRED_STRING_SINGLE_VALUE_FIELD, "name",
                TestOperationDefinitionProvider.EXAMPLE_LIST_COMPLEX_FIELD, List.of(
                    "Completely Wrong Period Value",
                    Map.of(
                        "start", "Bad Start",
                        "end", "Bad End"
                    ),
                    Map.of(
                        "start", "2021-09-01 12:30:45.123456789",
                        "end", "Bad End"
                    ),
                    Map.of(
                        "start", "Bad Start",
                        "end", "2021-09-01 12:30:45.123456789"
                    ),
                    Map.of(
                        "start", "2021-09-01 12:30:45.123456789",
                        "end", "2022-03-07 08:15:30.000000001"
                    )
                )
            )
        )
    );

    this.thenViolationsApproved(actual);
  }

  @Test
  void itShouldValidateSingleValueUnionComplexType() {
    var actual = this.commandValidator.validate(
        new DynamicCommand(
            UniversallyUniqueIdentifier.randomUUID(),
            TestOperationDefinitionProvider.EXAMPLE_TEST_COMMAND,
            Map.of(
                TestOperationDefinitionProvider.EXAMPLE_REQUIRED_STRING_SINGLE_VALUE_FIELD, "name",
                TestOperationDefinitionProvider.EXAMPLE_UNION_FIELD, Map.of(
                    DynamicCommand.SERIALIZATION_TYPE_FIELD_NAME, "BoxedString",
                    "value", 250,
                    "extension", List.of(
                        "Completely Wrong Extension",
                        Map.of(
                            "url", 50,
                            "value", Map.of(
                                DynamicCommand.SERIALIZATION_TYPE_FIELD_NAME, "Period",
                                "end", "2022-03-07 08:15:30.000000001"
                            )
                        ),
                        Map.of(
                            "value", Map.of(
                                DynamicCommand.SERIALIZATION_TYPE_FIELD_NAME, "BoxedDecimal",
                                "value", 15
                            )
                        ),
                        Map.of(
                            "url", "http://some.url",
                            "value", Map.of(
                                DynamicCommand.SERIALIZATION_TYPE_FIELD_NAME, "BoxedString",
                                "value", "Example Extension Value"
                            )
                        )
                    )
                )
            )
        )
    );

    this.thenViolationsApproved(actual);
  }

  @Test
  void itShouldValidateListUnionComplexType() {
    var actual = this.commandValidator.validate(
        new DynamicCommand(
            UniversallyUniqueIdentifier.randomUUID(),
            TestOperationDefinitionProvider.EXAMPLE_TEST_COMMAND,
            Map.of(
                TestOperationDefinitionProvider.EXAMPLE_REQUIRED_STRING_SINGLE_VALUE_FIELD, "name",
                TestOperationDefinitionProvider.EXAMPLE_LIST_UNION_FIELD, List.of(
                    "Completely Wrong Union Member",
                    Map.of(
                        "irrelevantField", "Missing Serialization Type Union Member"
                    ),
                    Map.of(
                        DynamicCommand.SERIALIZATION_TYPE_FIELD_NAME, "Period",
                        "start", "Bad Start",
                        "end", "2021-09-01 12:30:45.123456789"
                    ),
                    Map.of(
                        DynamicCommand.SERIALIZATION_TYPE_FIELD_NAME, "Quantity",
                        "extraField", "Extra Value",
                        "value", "Wrong Value Type",
                        "unit", "Kg",
                        "extension", "Wrong Extension Type"
                    ),
                    Map.of(
                        DynamicCommand.SERIALIZATION_TYPE_FIELD_NAME, "BoxedDecimal",
                        "value", 20.5
                    )
                )
            )
        )
    );

    this.thenViolationsApproved(actual);
  }

  @Test
  void itShouldNotAddTypeToFieldPathOfAnonymousObject() {
    var actual = this.commandValidator.validate(
        new DynamicCommand(
            UniversallyUniqueIdentifier.randomUUID(),
            TestOperationDefinitionProvider.EXAMPLE_TEST_COMMAND,
            Map.of(
                TestOperationDefinitionProvider.EXAMPLE_REQUIRED_STRING_SINGLE_VALUE_FIELD, "name",
                TestOperationDefinitionProvider.EXAMPLE_FIELD_WITH_ANONYMOUS_TYPE, Map.of(
                    "url", "http://some.url",
                    "name", "Example Structure Definition",
                    "status", "draft",
                    "abstract", false,
                    "kind", "complex-type",
                    "type", "ExampleStructureDefinition",
                    "differential", Map.of(
                        "element", List.of(
                            Map.of(
                                "path", "ExampleStructureDefinition.exampleElement",
                                "label", 50
                            )
                        ),
                        "parent", "ExampleStructureDefinition"
                    )
                )
            )
        )
    );

    this.thenViolationsApproved(actual);
  }

  private void thenViolationsApproved(List<CommandConstrainViolation> actual) {
    this.thenStringApproved(
        actual.stream()
            .map(CommandConstrainViolation::getMessage)
            .sorted()
            .collect(Collectors.joining("\n\n"))
    );
  }
}