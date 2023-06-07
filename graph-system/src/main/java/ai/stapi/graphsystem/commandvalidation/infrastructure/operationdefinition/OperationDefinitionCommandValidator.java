package ai.stapi.graphsystem.commandvalidation.infrastructure.operationdefinition;

import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.GenericAttributeValueFactory;
import ai.stapi.graph.attribute.attributeValue.IdAttributeValue;
import ai.stapi.graphsystem.commandvalidation.model.CommandConstrainViolation;
import ai.stapi.graphsystem.commandvalidation.model.CommandValidator;
import ai.stapi.graphsystem.commandvalidation.model.exceptions.CannotValidateCommand;
import ai.stapi.graphsystem.commandvalidation.model.fieldPath.CommandViolationFieldPath;
import ai.stapi.graphsystem.messaging.command.DynamicCommand;
import ai.stapi.graphsystem.messaging.command.AbstractPayloadCommand;
import ai.stapi.graphsystem.messaging.command.Command;
import ai.stapi.graphsystem.operationdefinition.exceptions.CannotProvideOperationDefinition;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionDTO;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionProvider;
import ai.stapi.graphsystem.operationdefinition.model.OperationDefinitionStructureTypeMapper;
import ai.stapi.schema.structureSchema.AbstractStructureType;
import ai.stapi.schema.structureSchema.ComplexStructureType;
import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structureSchema.FieldType;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaProvider;
import ai.stapi.schema.structureSchemaProvider.exception.CannotProvideStructureSchema;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class OperationDefinitionCommandValidator implements CommandValidator {

  private final OperationDefinitionProvider operationDefinitionProvider;
  private final StructureSchemaProvider structureSchemaProvider;
  private final OperationDefinitionStructureTypeMapper operationDefinitionStructureTypeMapper;
  private final GenericAttributeValueFactory genericAttributeValueFactory;
  private final ObjectMapper objectMapper;

  public OperationDefinitionCommandValidator(
      OperationDefinitionProvider operationDefinitionProvider,
      StructureSchemaProvider structureSchemaProvider,
      OperationDefinitionStructureTypeMapper operationDefinitionStructureTypeMapper,
      GenericAttributeValueFactory genericAttributeValueFactory,
      ObjectMapper objectMapper
  ) {
    this.operationDefinitionProvider = operationDefinitionProvider;
    this.structureSchemaProvider = structureSchemaProvider;
    this.operationDefinitionStructureTypeMapper = operationDefinitionStructureTypeMapper;
    this.genericAttributeValueFactory = genericAttributeValueFactory;
    this.objectMapper = objectMapper;
  }

  @Override
  public List<CommandConstrainViolation> validate(Command command) {
    var commandName = command.getSerializationType();

    OperationDefinitionDTO operationDefinition;
    try {
      operationDefinition = this.operationDefinitionProvider.provide(commandName);
    } catch (CannotProvideOperationDefinition e) {
      throw CannotValidateCommand.becauseThereWasNoOperationDefinition(commandName, e);
    }
    var fakedStructureType = this.operationDefinitionStructureTypeMapper.map(operationDefinition);

    var commandPayload = this.extractCommandPayload(command);
    return this.validateRecursively(
        commandPayload,
        fakedStructureType,
        new CommandViolationFieldPath(),
        commandName
    );
  }

  private List<CommandConstrainViolation> validateRecursively(
      Map<String, Object> dataObject,
      ComplexStructureType structureType,
      CommandViolationFieldPath fieldPath,
      String commandName
  ) {
    var violations = new ArrayList<CommandConstrainViolation>();

    var extraFieldsViolations = this.validateExtraFields(
        dataObject,
        structureType,
        fieldPath,
        commandName
    );
    violations.addAll(extraFieldsViolations);

    var requiredFieldsViolations = this.validateRequiredFields(
        dataObject,
        structureType,
        fieldPath,
        commandName
    );
    violations.addAll(requiredFieldsViolations);

    structureType.getAllFields().values().stream().map(
        fieldDefinition -> this.validateField(
            fieldDefinition,
            dataObject.get(fieldDefinition.getName()),
            fieldPath.add(fieldDefinition.getName()),
            commandName
        )
    ).forEach(violations::addAll);

    return violations;
  }

  private List<CommandConstrainViolation> validateExtraFields(
      Map<String, Object> commandPayload,
      ComplexStructureType structureType,
      CommandViolationFieldPath fieldPath,
      String commandName
  ) {
    var definedFields = structureType.getAllFields().keySet();

    return commandPayload.keySet().stream()
        .filter(fieldName -> !definedFields.contains(fieldName))
        .map(fieldPath::add)
        .map(newFieldPath -> new ExtraFieldCommandViolation(commandName, newFieldPath))
        .map(CommandConstrainViolation.class::cast)
        .toList();
  }

  private List<CommandConstrainViolation> validateRequiredFields(
      Map<String, Object> commandPayload,
      ComplexStructureType structureType,
      CommandViolationFieldPath fieldPath,
      String commandName
  ) {
    var requiredFields = structureType.getAllFields().entrySet().stream()
        .filter(entry -> entry.getValue().isRequired())
        .map(Map.Entry::getKey)
        .collect(Collectors.toSet());

    return requiredFields.stream()
        .filter(fieldName -> commandPayload.get(fieldName) == null)
        .map(fieldPath::add)
        .map(newFieldPath -> new MissingRequiredFieldCommandViolation(commandName, newFieldPath))
        .map(CommandConstrainViolation.class::cast)
        .toList();
  }

  private List<CommandConstrainViolation> validateField(
      FieldDefinition fieldDefinition,
      Object fieldValue,
      CommandViolationFieldPath fieldPath,
      String commandName
  ) {
    //Required fields had already been checked, if null no further validation needed
    if (fieldValue == null) {
      return List.of();
    }
    if (fieldDefinition.isList()) {
      return this.validateListField(fieldDefinition, fieldValue, fieldPath, commandName);
    } else {
      return this.validateSingleValueField(fieldDefinition, fieldValue, fieldPath, commandName);
    }
  }

  private List<CommandConstrainViolation> validateListField(
      FieldDefinition fieldDefinition,
      Object fieldValue,
      CommandViolationFieldPath fieldPath,
      String commandName
  ) {
    if (!(fieldValue instanceof List<?> listFieldValue)) {
      return List.of(
          FieldStructureTypeCommandViolation.notList(
              commandName,
              fieldPath,
              fieldValue
          )
      );
    }
    var violations = new ArrayList<CommandConstrainViolation>();

    var actualSize = listFieldValue.size();
    if (actualSize < fieldDefinition.getMin() || actualSize > fieldDefinition.getFloatMax()) {
      violations.add(
          new FieldCardinalityCommandViolation(
              commandName,
              fieldPath,
              actualSize,
              fieldDefinition.getMin(),
              fieldDefinition.getMax()
          )
      );
    }
    for (int index = 0; index < actualSize; index++) {
      var itemViolations = this.validateValue(
          fieldDefinition,
          listFieldValue.get(index),
          fieldPath.add("" + index),
          commandName
      );
      violations.addAll(itemViolations);
    }
    return violations;
  }

  private List<CommandConstrainViolation> validateSingleValueField(
      FieldDefinition fieldDefinition,
      Object fieldValue,
      CommandViolationFieldPath fieldPath,
      String commandName
  ) {
    if (fieldValue instanceof List<?>) {
      return List.of(
          FieldStructureTypeCommandViolation.notSingle(
              commandName,
              fieldPath,
              fieldValue
          )
      );
    }
    return this.validateValue(
        fieldDefinition,
        fieldValue,
        fieldPath,
        commandName
    );
  }

  private List<CommandConstrainViolation> validateValue(
      FieldDefinition fieldDefinition,
      Object value,
      CommandViolationFieldPath fieldPath,
      String commandName
  ) {
    //If it is any type, there are no validations to be done
    if (fieldDefinition.isAnyType()) {
      return List.of();
    }
    if (fieldDefinition.isUnionType()) {
      return this.validateUnionMember(fieldDefinition, value, fieldPath, commandName);
    }
    var type = fieldDefinition.getTypes().get(0);
    if (type.isReference()) {
      return this.validateReference(value, fieldPath, commandName);
    }
    if (type.isPrimitiveType()) {
      return validatePrimitive(type.getType(), value, fieldPath, commandName);
    }
    return this.validateComplex(type, value, fieldPath, commandName);
  }

  @NotNull
  private List<CommandConstrainViolation> validateUnionMember(
      FieldDefinition fieldDefinition,
      Object value,
      CommandViolationFieldPath fieldPath,
      String commandName
  ) {
    Map<String, Object> map;
    try {
      map = this.objectMapper.convertValue(value, new TypeReference<>() {
      });
    } catch (IllegalArgumentException cause) {
      return List.of(
          new InvalidUnionMemberCommandViolation(commandName, fieldPath, value, cause)
      );
    }
    var memberType = map.get(DynamicCommand.SERIALIZATION_TYPE_FIELD_NAME);
    if (memberType == null) {
      return List.of(
          new InvalidUnionMemberCommandViolation(commandName, fieldPath, value)
      );
    }
    var possibleMemberTypes = fieldDefinition.getTypes().stream().map(FieldType::getType).toList();
    if (!(memberType instanceof String stringMemberType) || !possibleMemberTypes.contains(
        memberType)) {
      return List.of(
          new InvalidUnionMemberTypeCommandViolation(
              commandName,
              fieldPath,
              memberType,
              possibleMemberTypes
          )
      );
    }
    var strippedMap = new HashMap<String, Object>();
    map.entrySet().stream()
        .filter(entry -> !entry.getKey().equals(DynamicCommand.SERIALIZATION_TYPE_FIELD_NAME))
        .forEach(entry -> strippedMap.put(entry.getKey(), entry.getValue()));

    AbstractStructureType memberStructureType = null;
    try {
      memberStructureType = this.structureSchemaProvider.provideSpecific(stringMemberType);
    } catch (CannotProvideStructureSchema e) {
      throw new RuntimeException(e);
    }
    var memberFieldType = fieldDefinition.getTypes().stream()
        .filter(fieldType -> fieldType.getType().equals(stringMemberType))
        .findFirst()
        .orElseThrow();

    return this.validateRecursively(
        strippedMap,
        (ComplexStructureType) memberStructureType,
        memberFieldType.isAnonymous() ? fieldPath : fieldPath.setTypeNameToLast(stringMemberType),
        commandName
    );
  }

  private List<CommandConstrainViolation> validateReference(
      Object value,
      CommandViolationFieldPath fieldPath,
      String commandName
  ) {
    Map<String, Object> map;
    try {
      map = this.objectMapper.convertValue(value, new TypeReference<>() {
      });
    } catch (IllegalArgumentException cause) {
      return List.of(
          new InvalidReferenceTypeCommandViolation(commandName, fieldPath, value, cause)
      );
    }
    if (map.size() != 1) {
      return List.of(
          new InvalidReferenceTypeCommandViolation(commandName, fieldPath, value)
      );
    }
    var id = map.get("id");
    if (id == null) {
      return List.of(
          new InvalidReferenceTypeCommandViolation(commandName, fieldPath, value)
      );
    }
    if (!this.genericAttributeValueFactory.isValidValue(IdAttributeValue.SERIALIZATION_TYPE, id)) {
      return List.of(
          new InvalidReferenceTypeCommandViolation(commandName, fieldPath, value)
      );
    }
    return List.of();
  }

  @NotNull
  private List<CommandConstrainViolation> validatePrimitive(
      String type,
      Object value,
      CommandViolationFieldPath fieldPath,
      String commandName
  ) {
    if (this.genericAttributeValueFactory.isValidValue(type, value)) {
      return List.of();
    }
    return List.of(
        new InvalidPrimitiveTypeCommandViolation(commandName, fieldPath, type, value)
    );
  }

  @NotNull
  private List<CommandConstrainViolation> validateComplex(
      FieldType type,
      Object value,
      CommandViolationFieldPath fieldPath,
      String commandName
  ) {
    var stringType = type.getType();
    Map<String, Object> map;
    try {
      map = this.objectMapper.convertValue(value, new TypeReference<>() {
      });
    } catch (IllegalArgumentException cause) {
      return List.of(
          new InvalidComplexTypeCommandViolation(commandName, fieldPath, value, stringType, cause)
      );
    }
    ComplexStructureType structureType;
    try {
      structureType =
          (ComplexStructureType) this.structureSchemaProvider.provideSpecific(stringType);
    } catch (CannotProvideStructureSchema e) {
      throw new RuntimeException(e);
    }
    return this.validateRecursively(
        map,
        structureType,
        type.isAnonymous() ? fieldPath : fieldPath.setTypeNameToLast(stringType),
        commandName
    );
  }

  private Map<String, Object> extractCommandPayload(Command command) {
    if (command instanceof AbstractPayloadCommand<?> createResourceCommand) {
      return createResourceCommand.getData();
    }
    var serializedCommand = this.objectMapper.convertValue(
        command,
        new TypeReference<Map<String, Object>>() {
        }
    );
    var commandPayload = new HashMap<String, Object>();
    var fieldsToFilterOut = List.of(
        DynamicCommand.TARGET_IDENTIFIER_FIELD_NAME,
        DynamicCommand.SERIALIZATION_TYPE_FIELD_NAME
    );
    serializedCommand.entrySet().stream()
        .filter(entry -> !fieldsToFilterOut.contains(entry.getKey()))
        .forEach(entry -> commandPayload.put(entry.getKey(), entry.getValue()));

    return commandPayload;
  }
}
