package ai.stapi.graphsystem.operationdefinition.model.testImplementations;

import ai.stapi.graphsystem.operationdefinition.exceptions.CannotProvideOperationDefinition;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO.ParameterDTO;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO.ParameterDTO.ReferencedFrom;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionProvider;
import ai.stapi.schema.structuredefinition.StructureDefinitionId;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Primary;

@Primary
public class TestOperationDefinitionProvider implements OperationDefinitionProvider {

  public static final String EXAMPLE_TEST_COMMAND = "ExampleTestCommand";
  public static final String ANOTHER_EXAMPLE_TEST_COMMAND = "AnotherExampleTestCommand";
  public static final String EXAMPLE_REQUIRED_STRING_SINGLE_VALUE_FIELD = "name";
  public static final String EXAMPLE_INTEGER_LIST_FIELD = "quantities";
  public static final String EXAMPLE_REFERENCE_FIELD = "exampleReferenceField";
  public static final String EXAMPLE_UNION_FIELD = "exampleUnion";
  public static final String EXAMPLE_COMPLEX_FIELD = "complexQuantity";
  public static final String EXAMPLE_LIST_COMPLEX_FIELD = "periods";
  public static final String EXAMPLE_LIST_UNION_FIELD = "exampleListUnion";
  public static final String EXAMPLE_FIELD_WITH_ANONYMOUS_TYPE = "fieldWithAnonymous";
  public static final String EXAMPLE_REQUIRED_STRING_LIST_FIELD = "tags";

  private static final String EXAMPLE_RESOURCE = "ExampleResource";
  private static Map<String, OperationDefinitionDTO> OPERATION_DEFINITION_MAP = Map.of(
      EXAMPLE_TEST_COMMAND, new OperationDefinitionDTO(
          EXAMPLE_TEST_COMMAND,
          "A Example Test Command",
          "draft",
          "operation",
          "Command for testing various services",
          EXAMPLE_TEST_COMMAND,
          List.of(EXAMPLE_RESOURCE),
          false,
          true,
          true,
          List.of(
              new ParameterDTO(
                  EXAMPLE_REQUIRED_STRING_SINGLE_VALUE_FIELD,
                  "in",
                  1,
                  "1",
                  "string",
                  new ReferencedFrom(EXAMPLE_RESOURCE),
                  List.of()
              ),
              new ParameterDTO(
                  EXAMPLE_INTEGER_LIST_FIELD,
                  "in",
                  0,
                  "3",
                  "integer",
                  new ReferencedFrom(EXAMPLE_RESOURCE),
                  List.of()
              ),
              new ParameterDTO(
                  EXAMPLE_REFERENCE_FIELD,
                  "in",
                  0,
                  "1",
                  "Reference",
                  new ReferencedFrom(EXAMPLE_RESOURCE),
                  List.of(
                      new StructureDefinitionId("OperationDefinition")
                  )
              ),
              new ParameterDTO(
                  String.format("%s%s", EXAMPLE_UNION_FIELD, "String"),
                  "in",
                  0,
                  "1",
                  "string",
                  new ReferencedFrom(EXAMPLE_RESOURCE),
                  List.of()
              ),
              new ParameterDTO(
                  String.format("%s%s", EXAMPLE_UNION_FIELD, "Integer"),
                  "in",
                  0,
                  "1",
                  "integer",
                  new ReferencedFrom(EXAMPLE_RESOURCE),
                  List.of()
              ),
              new ParameterDTO(
                  String.format("%s%s", EXAMPLE_UNION_FIELD, "Boolean"),
                  "in",
                  0,
                  "1",
                  "boolean",
                  new ReferencedFrom(EXAMPLE_RESOURCE),
                  List.of()
              ),
              new ParameterDTO(
                  EXAMPLE_COMPLEX_FIELD,
                  "in",
                  0,
                  "1",
                  "Quantity",
                  new ReferencedFrom(EXAMPLE_RESOURCE),
                  List.of()
              ),
              new ParameterDTO(
                  EXAMPLE_LIST_COMPLEX_FIELD,
                  "in",
                  0,
                  "3",
                  "Period",
                  new ReferencedFrom(EXAMPLE_RESOURCE),
                  List.of()
              ),
              new ParameterDTO(
                  String.format("%s%s", EXAMPLE_LIST_UNION_FIELD, "Period"),
                  "in",
                  0,
                  "*",
                  "Period",
                  new ReferencedFrom(EXAMPLE_RESOURCE),
                  List.of()
              ),
              new ParameterDTO(
                  String.format("%s%s", EXAMPLE_LIST_UNION_FIELD, "Quantity"),
                  "in",
                  0,
                  "*",
                  "Quantity",
                  new ReferencedFrom(EXAMPLE_RESOURCE),
                  List.of()
              ),
              new ParameterDTO(
                  String.format("%s%s", EXAMPLE_LIST_UNION_FIELD, "Decimal"),
                  "in",
                  0,
                  "*",
                  "decimal",
                  new ReferencedFrom(EXAMPLE_RESOURCE),
                  List.of()
              ),
              new ParameterDTO(
                  EXAMPLE_FIELD_WITH_ANONYMOUS_TYPE,
                  "in",
                  0,
                  "1",
                  "StructureDefinition",
                  new ReferencedFrom(EXAMPLE_RESOURCE),
                  List.of()
              )
          )
      ),
      ANOTHER_EXAMPLE_TEST_COMMAND, new OperationDefinitionDTO(
          ANOTHER_EXAMPLE_TEST_COMMAND,
          "A Another Example Test Command",
          "draft",
          "operation",
          "Command for testing various services",
          ANOTHER_EXAMPLE_TEST_COMMAND,
          List.of(),
          false,
          true,
          true,
          List.of(
              new ParameterDTO(
                  EXAMPLE_REQUIRED_STRING_LIST_FIELD,
                  "in",
                  3,
                  "*",
                  "string",
                  new ReferencedFrom(EXAMPLE_RESOURCE),
                  List.of()
              )
          )
      )
  );

  @Override
  public List<OperationDefinitionDTO> provideAll() {
    return TestOperationDefinitionProvider.OPERATION_DEFINITION_MAP.values().stream().toList();
  }

  @Override
  public OperationDefinitionDTO provide(String serializationType)
      throws CannotProvideOperationDefinition {
    var operation = TestOperationDefinitionProvider.OPERATION_DEFINITION_MAP.get(serializationType);
    if (operation == null) {
      throw new CannotProvideOperationDefinition(serializationType);
    }
    return operation;
  }

  @Override
  public boolean contains(String operationId) {
    return operationId.equals(EXAMPLE_TEST_COMMAND);
  }
}
