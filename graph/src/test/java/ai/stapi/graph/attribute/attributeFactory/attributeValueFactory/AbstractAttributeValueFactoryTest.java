package ai.stapi.graph.attribute.attributeFactory.attributeValueFactory;

import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.ListAttribute;
import ai.stapi.graph.attribute.SetAttribute;
import ai.stapi.graph.attribute.attributeFactory.AttributeValueFactoryInput;
import ai.stapi.graph.attribute.attributeFactory.GenericAttributeFactory;
import ai.stapi.graph.attribute.attributeFactory.exceptions.CannotCreateAttribute;
import ai.stapi.graph.attribute.attributeValue.NumberLikeAttributeValue;
import ai.stapi.graph.attribute.attributeValue.StringLikeAttributeValue;
import ai.stapi.graph.test.integration.IntegrationTestCase;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

abstract class AbstractAttributeValueFactoryTest extends IntegrationTestCase {

  @Autowired
  private GenericAttributeFactory genericAttributeFactory;
  
  protected void testItCanCorrectlyCreateAttributeOfVariousDataStructureType(
      Class<?> expectedAttributeValueClass,
      String testedAttributeSerializationType,
      Class<?> testedAttributeClass,
      List<Object> values
  ) {
    this.assertItCanCreateLeafValue(expectedAttributeValueClass, testedAttributeSerializationType, testedAttributeClass, values);
    this.assertItCanCreateListValue(expectedAttributeValueClass, testedAttributeSerializationType, testedAttributeClass, values);
    this.assertItCanCreateSetValue(expectedAttributeValueClass, testedAttributeSerializationType, testedAttributeClass, values);
  }


  protected void testItFailsToCreateIncorrectValueType(
      String serializationType,
      List<Object> values
  ) {
    Executable executable = () -> this.genericAttributeFactory.create(
        "exampleName",
        SetAttribute.DATA_STRUCTURE_TYPE,
        values.stream().map(value -> new AttributeValueFactoryInput(value, serializationType)).toList(),
        new HashMap<>()
    );
    
    Assertions.assertThrows(
        CannotCreateAttribute.class,
        executable
    );
  }

  private void assertItCanCreateSetValue(
      Class<?> expectedAttributeValueClass, 
      String testedAttributeSerializationType,
      Class<?> testedAttributeClass,
      List<Object> values
  ) {
    var actualSet = this.genericAttributeFactory.create(
        "exampleName",
        SetAttribute.DATA_STRUCTURE_TYPE,
        values.stream().map(value -> new AttributeValueFactoryInput(value, testedAttributeSerializationType)).toList(),
        new HashMap<>()
    );
    Assertions.assertInstanceOf(SetAttribute.class, actualSet);
    Assertions.assertInstanceOf(Set.class, actualSet.getValue());
    var setValue = (Set<?>) actualSet.getValue();
    Assertions.assertInstanceOf(expectedAttributeValueClass, setValue.iterator().next());
    Assertions.assertEquals(values.size(), setValue.size());
  }

  private void assertItCanCreateListValue(
      Class<?> expectedAttributeValueClass, 
      String testedAttributeSerializationType,
      Class<?> testedAttributeClass,
      List<Object> values
  ) {
    var actualList = this.genericAttributeFactory.create(
        "exampleName",
        ListAttribute.DATA_STRUCTURE_TYPE,
        values.stream().map(value -> new AttributeValueFactoryInput(value, testedAttributeSerializationType)).toList(),
        new HashMap<>()
    );
    Assertions.assertInstanceOf(ListAttribute.class, actualList);
    Assertions.assertInstanceOf(List.class, actualList.getValue());
    var listValue = (List<?>) actualList.getValue();
    Assertions.assertInstanceOf(
        expectedAttributeValueClass, 
        listValue.get(0), 
        String.format(
            "Expected that raw value will be instance of %s, but got %s",
            expectedAttributeValueClass.getName(),
            listValue.getClass().getName()
        )
    );
    Assertions.assertInstanceOf(expectedAttributeValueClass, listValue.get(0));
    Assertions.assertEquals(values.size(), listValue.size());
  }

  private void assertItCanCreateLeafValue(
      Class<?> expectedAttributeValueClass, 
      String testedAttributeSerializationType,
      Class<?> testedAttributeClass,
      List<Object> values
  ) {
    var actualLeaf = this.genericAttributeFactory.create(
        "exampleName",
        LeafAttribute.DATA_STRUCTURE_TYPE,
        List.of(
            new AttributeValueFactoryInput(
                values.get(0),
                testedAttributeSerializationType
            )
        ),
        new HashMap<>()
    );
    Assertions.assertInstanceOf(LeafAttribute.class, actualLeaf);
    var leafAttribute = (LeafAttribute) actualLeaf;
    var boxedValue = leafAttribute.getBoxedValue();
    Assertions.assertEquals(testedAttributeSerializationType, boxedValue.getDataType());
    Assertions.assertInstanceOf(testedAttributeClass, boxedValue);
    Assertions.assertInstanceOf(expectedAttributeValueClass, boxedValue.getValue());
    Assertions.assertEquals(values.get(0).toString(), boxedValue.getValue().toString());
    if(boxedValue instanceof StringLikeAttributeValue<?> stringLikeAttributeValue) {
      Assertions.assertEquals(values.get(0).toString(), stringLikeAttributeValue.toStringValue());
    }
    if(boxedValue instanceof NumberLikeAttributeValue<?> numberLikeAttributeValue) {
      Assertions.assertEquals(values.get(0).toString(), numberLikeAttributeValue.getValue().toString());
    }
    Assertions.assertEquals(values.get(0).toString(), boxedValue.getValue().toString());
    Assertions.assertEquals(values.get(0).toString(), actualLeaf.getValue().toString());



  }
  
}
