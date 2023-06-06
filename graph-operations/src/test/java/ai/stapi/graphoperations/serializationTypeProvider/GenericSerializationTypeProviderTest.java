package ai.stapi.graphoperations.serializationTypeProvider;

import ai.stapi.test.integration.IntegrationTestCase;
import ai.stapi.graph.Graph;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectObjectGraphMapping;
import ai.stapi.graphoperations.ogmProviders.specific.objectGraphMappingMappingProviders.OgmGraphElementTypes;
import ai.stapi.graphoperations.serializationTypeProvider.exception.GenericSerializationTypeProviderException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

class GenericSerializationTypeProviderTest extends IntegrationTestCase {

  @Autowired
  private GenericSerializationTypeByNodeProvider serializationTypeProvider;

  @Test
  public void itThrowsErrorWhenNodeIsNotSupported() {
    var randomGraph = new Graph(
        new Node(
            UniversallyUniqueIdentifier.fromString("daa23bf3-4e73-42eb-9b57-1d3433902ac5"),
            "not_supported_node_type"
        )
    );
    Executable throwable = () -> this.serializationTypeProvider.getSerializationType(
        randomGraph.traversable().loadAllNodes("not_supported_node_type").get(0)
    );
    this.thenExceptionMessageApproved(GenericSerializationTypeProviderException.class, throwable);
  }

  @Test
  public void itCanProvideSerializationTypeForSupportedNode() {
    var graph = new Graph(new Node(OgmGraphElementTypes.OGM_OBJECT_NODE));
    var serializationType = this.serializationTypeProvider.getSerializationType(
        graph.traversable().loadAllNodes(OgmGraphElementTypes.OGM_OBJECT_NODE).get(0)
    );
    Assertions.assertEquals(ObjectObjectGraphMapping.SERIALIZATION_TYPE, serializationType);
  }

}