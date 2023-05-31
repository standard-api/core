package ai.stapi.graph.attribute.attributeFactory;

import ai.stapi.graph.attribute.Attribute;
import ai.stapi.graph.attribute.MetaData;
import java.util.List;
import java.util.Map;

public interface AttributeFactory {

  Attribute<?> create(
      String attributeName,
      List<AttributeValueFactoryInput> parameters,
      Map<String, MetaData> metaData
  );

  boolean supportsStructureType(String structureType);
}
