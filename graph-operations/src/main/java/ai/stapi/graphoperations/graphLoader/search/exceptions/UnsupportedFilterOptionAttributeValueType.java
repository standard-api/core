package ai.stapi.graphoperations.graphLoader.search.exceptions;

public class UnsupportedFilterOptionAttributeValueType extends RuntimeException {

  public UnsupportedFilterOptionAttributeValueType(
      Class<?> attributeValueType,
      Class<?> optionClass
  ) {
    super(
        String.format(
            "Attribute value of type: \"%s\" cannot be used in: \"%s\"",
            attributeValueType.getCanonicalName(),
            optionClass.getCanonicalName()
        )
    );
  }

  public UnsupportedFilterOptionAttributeValueType(
      Class<?> optionClass
  ) {
    super(
        String.format(
            "Null attribute value cannot be used in: \"%s\"",
            optionClass.getCanonicalName()
        )
    );
  }
}
