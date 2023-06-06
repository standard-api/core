package ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.specific;

import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.GenericGraphToObjectDeserializer;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.MissingTraversalTargetResolvingStrategy;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.exception.GenericGraphOgmDeserializerException;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NullGraphDescription;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.graphReader.GraphReader;
import ai.stapi.graphoperations.objectGraphLanguage.InterfaceObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectObjectGraphMapping;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import ai.stapi.serialization.AbstractSerializableObject;
import ai.stapi.graphoperations.serializationTypeProvider.GenericSerializationTypeByNodeProvider;
import java.util.List;
import java.util.Map;

public class InterfaceGraphToObjectDeserializer extends AbstractSpecificGraphToObjectDeserializer {

  public InterfaceGraphToObjectDeserializer(
      GraphReader graphReader,
      GenericGraphToObjectDeserializer genericDeserializer,
      GenericSerializationTypeByNodeProvider serializationTypeProvider,
      GenericGraphMappingProvider mappingProvider
  ) {
    super(graphReader, genericDeserializer, serializationTypeProvider, mappingProvider);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Object deserialize(
      List<TraversableGraphElement> elements,
      GraphDescription lastGraphDescription,
      ObjectGraphMapping graphMapping,
      InMemoryGraphRepository contextualGraph,
      MissingTraversalTargetResolvingStrategy missingTraversalTargetResolvingStrategy
  ) {
    if (elements.size() == 0) {
      return null;
    }
    if (elements.size() > 1) {
      throw GenericGraphOgmDeserializerException.becauseThereIsMultipleGraphBranchesButMappingIsNotForList();
    }
    elements = this.ensureElementsAreNodes(elements, lastGraphDescription);
    var rootElement = elements.get(0);
    if (!this.serializationTypeProvider.existsSerializationTypeForNode(rootElement)) {
      throw GenericGraphOgmDeserializerException.becauseNodeTypeIsNotSupported(
          rootElement.getType());
    }
    var serializationType = this.serializationTypeProvider.getSerializationType(rootElement);
    var objectGraphMapping =
        (ObjectObjectGraphMapping) this.mappingProvider.provideGraphMapping(serializationType);
    var objectMap = (Map<String, Object>) this.genericDeserializer.resolveInternally(
        elements,
        new NullGraphDescription(),
        objectGraphMapping,
        contextualGraph,
        missingTraversalTargetResolvingStrategy
    );
    objectMap.put(
        AbstractSerializableObject.NAME_OF_FIELD_WITH_SERIALIZATION_TYPE,
        serializationType
    );
    return objectMap;
  }

  @Override
  public boolean supports(
      ObjectGraphMapping objectGraphMapping,
      List<TraversableGraphElement> elements
  ) {
    return objectGraphMapping instanceof InterfaceObjectGraphMapping;
  }
}
