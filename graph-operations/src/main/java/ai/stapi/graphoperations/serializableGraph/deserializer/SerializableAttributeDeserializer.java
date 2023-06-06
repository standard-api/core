package ai.stapi.graphoperations.serializableGraph.deserializer;

import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.ListAttribute;
import ai.stapi.graph.attribute.MetaData;
import ai.stapi.graph.attribute.attributeFactory.AttributeValueFactoryInput;
import ai.stapi.graph.attribute.attributeFactory.GenericAttributeFactory;
import ai.stapi.graph.attribute.attributeFactory.attributeValueFactory.GenericAttributeValueFactory;
import ai.stapi.graph.versionedAttributes.ImmutableVersionedAttribute;
import ai.stapi.graph.versionedAttributes.ImmutableVersionedAttributeGroup;
import ai.stapi.graph.versionedAttributes.VersionedAttribute;
import ai.stapi.graphoperations.serializableGraph.SerializableAttributeValue;
import ai.stapi.graphoperations.serializableGraph.SerializableAttributeVersion;
import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

public class SerializableAttributeDeserializer {

  private final StructureSchemaFinder structureSchemaFinder;
  private final GenericAttributeFactory genericAttributeFactory;
  private final GenericAttributeValueFactory genericAttributeValueFactory;

  public SerializableAttributeDeserializer(
      StructureSchemaFinder structureSchemaFinder,
      GenericAttributeFactory genericAttributeFactory,
      GenericAttributeValueFactory genericAttributeValueFactory
  ) {
    this.structureSchemaFinder = structureSchemaFinder;
    this.genericAttributeFactory = genericAttributeFactory;
    this.genericAttributeValueFactory = genericAttributeValueFactory;
  }

  public VersionedAttribute<?> deserialize(
      String graphElementType,
      String attributeName,
      List<SerializableAttributeVersion> attribute
  ) {
    var fieldDefinition = this.structureSchemaFinder.getFieldDefinitionOrFallback(
        graphElementType,
        attributeName
    );
    return new ImmutableVersionedAttribute<>(
        attribute.stream().map(
            serializableAttributeVersion -> {
              var metaData = new HashMap<String, MetaData>();
              serializableAttributeVersion.getMetaData().forEach((name, meta) -> metaData.put(
                  name,
                  new MetaData(
                      name,
                      meta.stream().map(value -> this.genericAttributeValueFactory.create(
                          name,
                          new AttributeValueFactoryInput(
                              value.getValue(),
                              value.getSerializationType()
                          )
                      )).collect(Collectors.toList())
                  )
              ));
              return this.genericAttributeFactory.create(
                  attributeName,
                  this.getStructureType(fieldDefinition),
                  serializableAttributeVersion.getValues().stream()
                      .map(SerializableAttributeValue::toAttributeFactoryInput)
                      .toList(),
                  metaData
              );
            }
        ).toList()
    );
  }

  public ImmutableVersionedAttributeGroup deserializeGroup(
      String graphElementType,
      Map<String, List<SerializableAttributeVersion>> attributeMap
  ) {
    var rawMap = new HashMap<String, VersionedAttribute<?>>();
    attributeMap.forEach(
        (attributeName, attribute) -> rawMap.put(
            attributeName,
            this.deserialize(
                graphElementType,
                attributeName,
                attribute
            )
        )
    );
    return new ImmutableVersionedAttributeGroup(rawMap);
  }

  @NotNull
  private String getStructureType(FieldDefinition fieldDefinition) {
    return fieldDefinition.getStructureType().equals(FieldDefinition.LEAF_STRUCTURE_TYPE) ?
        LeafAttribute.DATA_STRUCTURE_TYPE :
        ListAttribute.DATA_STRUCTURE_TYPE;
  }
}
