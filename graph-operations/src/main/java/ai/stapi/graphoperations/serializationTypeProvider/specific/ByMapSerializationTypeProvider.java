package ai.stapi.graphoperations.serializationTypeProvider.specific;

import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.BooleanAttributeValueDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.IngoingEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.IntegerAttributeValueDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.LeafAttributeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.OutgoingEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.StringAttributeValueDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal.RemovalEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal.RemovalNodeDescription;
import ai.stapi.graphoperations.objectGraphLanguage.InterfaceObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.LeafObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ListObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.MapObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectFieldDefinition;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectObjectGraphMapping;
import ai.stapi.graphoperations.objectLanguage.EntityIdentifier;
import ai.stapi.graphoperations.ogmProviders.specific.objectGraphMappingMappingProviders.OgmGraphElementTypes;
import ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders.GraphDescriptionGraphElementTypes;
import ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders.ObjectDeclarationGraphElementTypes;
import ai.stapi.schema.structuredefinition.ElementDefinition;
import ai.stapi.schema.structuredefinition.StructureDefinitionData;
import java.util.HashMap;
import java.util.Map;

public class ByMapSerializationTypeProvider implements SpecificSerializationTypeProvider {

  @Override
  public String provideSerializationType(TraversableGraphElement element) {
    return this.mapOfSerializableNodeTypes().get(element.getType());
  }

  @Override
  public boolean supports(TraversableGraphElement element) {
    return this.mapOfSerializableNodeTypes().containsKey(element.getType());
  }

  private Map<String, String> mapOfSerializableNodeTypes() {
    var map = new HashMap<String, String>();
    map.put(OgmGraphElementTypes.OGM_OBJECT_NODE, ObjectObjectGraphMapping.SERIALIZATION_TYPE);
    map.put(OgmGraphElementTypes.OGM_LEAF_NODE, LeafObjectGraphMapping.SERIALIZATION_TYPE);
    map.put(OgmGraphElementTypes.OGM_MAP_NODE, MapObjectGraphMapping.SERIALIZATION_TYPE);
    map.put(OgmGraphElementTypes.OGM_LIST_NODE, ListObjectGraphMapping.SERIALIZATION_TYPE);
    map.put(OgmGraphElementTypes.OGM_OBJECT_FIELD_NODE, ObjectFieldDefinition.SERIALIZATION_TYPE);
    map.put(
        OgmGraphElementTypes.OGM_INTERFACE_NODE,
        InterfaceObjectGraphMapping.SERIALIZATION_TYPE
    );
    map.put(
        GraphDescriptionGraphElementTypes.NODE_DESCRIPTION_NODE,
        NodeDescription.SERIALIZATION_TYPE
    );
    map.put(
        GraphDescriptionGraphElementTypes.INCOMING_EDGE_DESCRIPTION_NODE,
        IngoingEdgeDescription.SERIALIZATION_TYPE
    );
    map.put(
        GraphDescriptionGraphElementTypes.OUTGOING_EDGE_DESCRIPTION_NODE,
        OutgoingEdgeDescription.SERIALIZATION_TYPE
    );
    map.put(
        GraphDescriptionGraphElementTypes.UUID_DESCRIPTION,
        UuidIdentityDescription.SERIALIZATION_TYPE
    );
    map.put(
        GraphDescriptionGraphElementTypes.BOOLEAN_ATTRIBUTE_DESCRIPTION,
        BooleanAttributeValueDescription.SERIALIZATION_TYPE
    );
    map.put(
        GraphDescriptionGraphElementTypes.STRING_ATTRIBUTE_DESCRIPTION,
        StringAttributeValueDescription.SERIALIZATION_TYPE
    );
    map.put(
        GraphDescriptionGraphElementTypes.INTEGER_ATTRIBUTE_DESCRIPTION,
        IntegerAttributeValueDescription.SERIALIZATION_TYPE
    );
    map.put(
        GraphDescriptionGraphElementTypes.LEAF_ATTRIBUTE_DESCRIPTION,
        LeafAttributeDescription.SERIALIZATION_TYPE
    );
    map.put(
        GraphDescriptionGraphElementTypes.REMOVAL_EDGE_DESCRIPTION,
        RemovalEdgeDescription.SERIALIZATION_TYPE
    );
    map.put(
        GraphDescriptionGraphElementTypes.REMOVAL_NODE_DESCRIPTION,
        RemovalNodeDescription.SERIALIZATION_TYPE
    );
    map.put(
        ObjectDeclarationGraphElementTypes.ENTITY_IDENTIFIER_DECLARATION,
        EntityIdentifier.SERIALIZATION_TYPE
    );
    map.put(StructureDefinitionData.SERIALIZATION_TYPE, StructureDefinitionData.SERIALIZATION_TYPE);
    map.put(ElementDefinition.SERIALIZATION_TYPE, ElementDefinition.SERIALIZATION_TYPE);
    return map;
  }
}
