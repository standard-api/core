package ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.specific;

import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.GenericGraphToObjectDeserializer;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.MissingTraversalTargetResolvingStrategy;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.graphReader.GraphReader;
import ai.stapi.graphoperations.objectGraphLanguage.LeafObjectGraphMapping;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import ai.stapi.graphoperations.serializationTypeProvider.GenericSerializationTypeByNodeProvider;
import java.util.List;

public class LeafGraphToObjectDeserializer extends AbstractSpecificGraphToObjectDeserializer {

  public LeafGraphToObjectDeserializer(
      GraphReader graphReader,
      GenericGraphToObjectDeserializer genericDeserializer,
      GenericSerializationTypeByNodeProvider serializationTypeProvider,
      GenericGraphMappingProvider mappingProvider
  ) {
    super(graphReader, genericDeserializer, serializationTypeProvider, mappingProvider);
  }

  @Override
  public Object deserialize(
      List<TraversableGraphElement> elements,
      GraphDescription lastGraphDescription,
      ObjectGraphMapping graphMapping,
      InMemoryGraphRepository contextualGraph,
      MissingTraversalTargetResolvingStrategy missingTraversalTargetResolvingStrategy
  ) {
    var leafMapping = (LeafObjectGraphMapping) graphMapping;
    return this.traverseSingleGraphBranchToValue(
        elements.get(0).getId(),
        leafMapping.getGraphDescription(),
        contextualGraph,
        missingTraversalTargetResolvingStrategy
    );

  }

  @Override
  public boolean supports(
      ObjectGraphMapping objectGraphMapping,
      List<TraversableGraphElement> elements
  ) {
    return objectGraphMapping instanceof LeafObjectGraphMapping;
  }
}
