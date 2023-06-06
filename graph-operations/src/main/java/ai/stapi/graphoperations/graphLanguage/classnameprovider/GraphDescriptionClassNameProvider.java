package ai.stapi.graphoperations.graphLanguage.classnameprovider;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AllAttributesDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.BooleanAttributeValueDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.ConstantDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.IngoingEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.IntegerAttributeValueDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.LeafAttributeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.ListAttributeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NullGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.OutgoingEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.SetAttributeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.StringAttributeValueDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.AttributeQueryDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.IngoingEdgeQueryDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.NodeQueryGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.OutgoingEdgeQueryDescription;
import ai.stapi.graphoperations.objectGraphLanguage.InterfaceObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.LeafObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ListObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.MapObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ReferenceObjectGraphMapping;
import ai.stapi.serialization.classNameProvider.specific.AbstractSerializableObjectClassNameProvider;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class GraphDescriptionClassNameProvider extends AbstractSerializableObjectClassNameProvider {
  @Override
  protected Map<String, Class<?>> getClassMap() {
    var map = new HashMap<String, Class<?>>();
    map.put(NodeDescription.SERIALIZATION_TYPE, NodeDescription.class);
    map.put(OutgoingEdgeDescription.SERIALIZATION_TYPE, OutgoingEdgeDescription.class);
    map.put(IngoingEdgeDescription.SERIALIZATION_TYPE, IngoingEdgeDescription.class);
    map.put(StringAttributeValueDescription.SERIALIZATION_TYPE, StringAttributeValueDescription.class);
    map.put(IntegerAttributeValueDescription.SERIALIZATION_TYPE, IntegerAttributeValueDescription.class);
    map.put(BooleanAttributeValueDescription.SERIALIZATION_TYPE, BooleanAttributeValueDescription.class);
    map.put(UuidIdentityDescription.SERIALIZATION_TYPE, UuidIdentityDescription.class);
    map.put(ConstantDescription.SERIALIZATION_TYPE, ConstantDescription.class);
    map.put(NullGraphDescription.SERIALIZATION_TYPE, NullGraphDescription.class);
    map.put(NodeQueryGraphDescription.SERIALIZATION_TYPE, NodeQueryGraphDescription.class);
    map.put(OutgoingEdgeQueryDescription.SERIALIZATION_TYPE, OutgoingEdgeQueryDescription.class);
    map.put(IngoingEdgeQueryDescription.SERIALIZATION_TYPE, IngoingEdgeQueryDescription.class);
    map.put(AllAttributesDescription.SERIALIZATION_TYPE, AllAttributesDescription.class);
    map.put(LeafAttributeDescription.SERIALIZATION_TYPE, LeafAttributeDescription.class);
    map.put(ListAttributeDescription.SERIALIZATION_TYPE, ListAttributeDescription.class);
    map.put(SetAttributeDescription.SERIALIZATION_TYPE, SetAttributeDescription.class);
    map.put(AttributeQueryDescription.SERIALIZATION_TYPE, AttributeQueryDescription.class);
    return map;
  }
}
