package ai.stapi.graphoperations.objectGraphMapper.model.specific.exceptions;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.objectGraphMapper.model.SpecificObjectGraphMapper;
import ai.stapi.graphoperations.objectLanguage.EntityIdentifier;
import ai.stapi.graphoperations.objectGraphLanguage.InterfaceObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectFieldDefinition;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ai.stapi.graphoperations.declaration.Declaration;
import ai.stapi.serialization.AbstractSerializableObject;
import ai.stapi.serialization.SerializableObject;
import ai.stapi.utils.Stringifier;
import java.util.Map;

public class SpecificObjectGraphMapperException extends RuntimeException {

  private SpecificObjectGraphMapperException(String message) {
    super(message);
  }

  public static SpecificObjectGraphMapperException becauseGraphDescriptionHasMultipleNonConstantChildren(
      GraphDescription graphDescription,
      Integer childrenCount
  ) {
    return new SpecificObjectGraphMapperException(
        "Each " + GraphDescription.class.getSimpleName()
            + " inside " + ObjectGraphMapping.class.getSimpleName()
            + " is allowed to have only one non-constant child. Provided "
            + GraphDescription.class.getSimpleName() + " of type '"
            + graphDescription.getClass().getSimpleName()
            + "' contains " + childrenCount + "."
    );
  }

  public static SpecificObjectGraphMapperException becauseSerializationTypeWasNotFound(
      String fieldName,
      Object object
  ) {
    try {
      var objectMap = new ObjectMapper().writerWithDefaultPrettyPrinter()
          .writeValueAsString(object);
      return new SpecificObjectGraphMapperException(
          "Serialization type of object on field '" +
              fieldName +
              "' could not be determined." +
              System.lineSeparator() +
              "Problematic object: " +
              System.lineSeparator() +
              objectMap

      );
    } catch (JsonProcessingException e) {
      throw new SpecificObjectGraphMapperException(
          "Serialization type of object on field '" +
              fieldName +
              "' could not be determined."
      );
    }
  }

  public static SpecificObjectGraphMapperException becauseFieldDoesNotContainGraphDescription(
      String fieldName,
      Declaration declaration
  ) {
    return new SpecificObjectGraphMapperException(
        "Declaration inside field '" + fieldName + "' was of unexpected type. Expected: '"
            + GraphDescription.class.getSimpleName()
            + "', Actual: '"
            + declaration.getClass().getSimpleName()
            + "'."
    );
  }

  public static SpecificObjectGraphMapperException becauseThereAreMultipleIdentifyingFieldsOnObject() {
    return new SpecificObjectGraphMapperException(
        "There are multiple fields containing '" + EntityIdentifier.class.getSimpleName()
            + "' on object. That is not allowed."
    );
  }

  public static SpecificObjectGraphMapperException becauseProvidedValueIsNotPrimitiveType(
      String fieldName, 
      Object value
  ) {
    return new SpecificObjectGraphMapperException(
        "Cannot create attribute with value other than primitive." +
            System.lineSeparator() +
            "Field: " +
            fieldName +
            System.lineSeparator() +
            "Value: " +
            System.lineSeparator() +
            Stringifier.convertToString(value)
    );
  }

  public static SpecificObjectGraphMapperException becauseThereIsNoIdentifyingFieldOnObject(
      String parentObjectFieldName) {
    return new SpecificObjectGraphMapperException(
        "There is no " + ObjectFieldDefinition.class.getSimpleName()
            + " containing '"
            + EntityIdentifier.class.getSimpleName()
            + "' on object inside private field '"
            + parentObjectFieldName + "'."
    );
  }

  public static SpecificObjectGraphMapperException becauseObjectCouldNotBeConverted(
      SpecificObjectGraphMapper graphMapper,
      String fieldName,
      Object object,
      RuntimeException error
  ) {
    return new SpecificObjectGraphMapperException(
        "Provided object from field '" + fieldName + "' could not be converted by '"
            + graphMapper.getClass().getSimpleName()
            + "'. Object probably doesn't correspond to "
            + ObjectGraphMapping.class.getSimpleName() + " definition attached to it."
            + System.lineSeparator()
            + "Cause: " + error.getMessage()
            + System.lineSeparator()
            + "Provided object: " + Stringifier.convertToString(object)
    );
  }

  public static SpecificObjectGraphMapperException becauseProvidedObjectDoesNotContainFieldWithGivenName(
      Object object,
      String fieldName,
      Map<String, ObjectFieldDefinition> fields
  ) {
    String stringObject = null;
    try {
      stringObject = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      stringObject = "[unable to deserialize]";
    }
    return new SpecificObjectGraphMapperException(
        String.format(
            "Provided object:\n%s\n does not contain field with name:\n%s\nAll required:\n%s",
            stringObject,
            fieldName,
            String.join(
                ",",
                fields.keySet()
            )
        )
    );
  }

  public static SpecificObjectGraphMapperException becauseObjectIsNotSerializable(
      RuntimeException error) {
    return new SpecificObjectGraphMapperException(
        "Object described by '" + InterfaceObjectGraphMapping.class.getSimpleName()
            + "' does not inherit '" + AbstractSerializableObject.class.getSimpleName()
            + "' or can not be serialize."
            + System.lineSeparator() + "Cause :" + error.getMessage()
    );
  }

  public static SpecificObjectGraphMapperException becauseObjectIsNotSerializable() {
    return new SpecificObjectGraphMapperException(
        "Object described by '" + InterfaceObjectGraphMapping.class.getSimpleName()
            + "' does not inherit '" + AbstractSerializableObject.class.getSimpleName() + "'."
    );
  }

  public static SpecificObjectGraphMapperException becauseActualEntityBehindInterfaceIsNotObject(
      Object object) {
    return new SpecificObjectGraphMapperException(
        "Interface implementation can not be of type '"
            + object.getClass().getSimpleName()
            + "'. It has to be object and implement interface '"
            + SerializableObject.class.getSimpleName() + "'."
    );
  }

  public static SpecificObjectGraphMapperException becauseProvidedObjectMapDoesNotContainTypeField() {
    return new SpecificObjectGraphMapperException(
        "Provided object map does not contain field '"
            + AbstractSerializableObject.NAME_OF_FIELD_WITH_SERIALIZATION_TYPE
            + "' with type information."
    );
  }
}
