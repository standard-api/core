package ai.stapi.schema.structureSchema.exception;


import ai.stapi.schema.structureSchemaMapper.UnresolvableType;
import java.util.List;

public class FieldsNotFoundException extends RuntimeException {

  private FieldsNotFoundException(
      String message,
      String serializationType,
      List<String> fieldNames
  ) {
    super(
        String.format(
            "%s not found at structure with serializationType '%s', %s",
            fieldNames.size() == 1 ? String.format("Field '%s'", fieldNames.get(0)) :
                String.format("Fields '%s'", fieldNames),
            serializationType,
            message
        )
    );
  }

  private FieldsNotFoundException(
      String message,
      String serializationType
  ) {
    super(
        String.format(
            "Some fields not found at structure with serializationType '%s', %s",
            serializationType,
            message
        )
    );
  }

  public static FieldsNotFoundException becauseSerializationTypeDoesNotExistInSchema(
      String serializationType,
      List<String> fieldNames
  ) {
    return new FieldsNotFoundException(
        "because serializationType does not exist in schema.",
        serializationType,
        fieldNames
    );
  }

  public static FieldsNotFoundException becauseSerializationTypeIsPrimitive(
      String serializationType,
      List<String> fieldNames
  ) {
    return new FieldsNotFoundException(
        "because serializationType is primitive.",
        serializationType,
        fieldNames
    );
  }

  public static FieldsNotFoundException becauseSerializationTypeIsOfUnknownType(
      String serializationType
  ) {
    return new FieldsNotFoundException(
        "because serializationType '%s' is of unknown type.",
        serializationType
    );
  }

  public static FieldsNotFoundException becauseSerializationTypeDoesNotExistInSchema(
      String serializationType
  ) {
    return new FieldsNotFoundException(
        "because serializationType does not exist in schema.",
        serializationType
    );
  }

  public static FieldsNotFoundException becauseSerializationTypeDoesNotExistInSchema(
      String serializationType,
      UnresolvableType unresolvableType
  ) {
    return new FieldsNotFoundException(
        String.format(
            "because serializationType does not exist in schema, but is unresolvable.%nMissing dependencies: '%s'",
            unresolvableType.missingDependencies()
        ),
        serializationType
    );
  }

  public static FieldsNotFoundException becauseSerializationTypeIsPrimitive(
      String serializationType
  ) {
    return new FieldsNotFoundException(
        "because serializationType is primitive.",
        serializationType
    );
  }

  public static FieldsNotFoundException becauseSerializationTypeIsOfUnknownType(
      String serializationType,
      List<String> fieldNames
  ) {
    return new FieldsNotFoundException(
        "because serializationType '%s' is of unknown type.",
        serializationType,
        fieldNames
    );
  }

  public static FieldsNotFoundException becauseFinderIsRestricted(
      String serializationType,
      List<String> fieldNames
  ) {
    return new FieldsNotFoundException(
        "because Structure Schema Finder is restricted. You should use Test Case supporting Structure Schema.",
        serializationType,
        fieldNames
    );
  }

  public static FieldsNotFoundException becauseFinderIsRestricted(
      String serializationType
  ) {
    return new FieldsNotFoundException(
        "because Structure Schema Finder is restricted. You should use Test Case supporting Structure Schema.",
        serializationType
    );
  }

  public static FieldsNotFoundException becauseSomeFieldsAreMissing(
      String serializationType,
      List<String> fieldNames
  ) {
    return new FieldsNotFoundException(
        "because they are missing in found Complex Structure Type.",
        serializationType,
        fieldNames
    );
  }
}
