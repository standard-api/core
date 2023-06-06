package ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.specific;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.graphReader.GraphReader;
import ai.stapi.graphoperations.objectGraphLanguage.MapObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.GenericGraphToObjectDeserializer;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.MissingTraversalTargetResolvingStrategy;
import ai.stapi.graphoperations.serializationTypeProvider.GenericSerializationTypeByNodeProvider;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MapGraphToObjectDeserializer extends AbstractSpecificGraphToObjectDeserializer {

  public MapGraphToObjectDeserializer(
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
    var mapMapping = (MapObjectGraphMapping) graphMapping;
    var map = new HashMap<>();
    elements.stream()
        .map(element -> this.traverseMultipleGraphBranch(
                element.getId(),
                mapMapping.getGraphDescription(),
                contextualGraph
            )
        ).flatMap(List::stream)
        .forEach(
            element -> map.put(
                this.genericDeserializer.resolveInternally(
                    List.of(element),
                    this.updateLastGraphDescription(
                        lastGraphDescription,
                        mapMapping.getGraphDescription()
                    ),
                    mapMapping.getKeyObjectGraphMapping(),
                    contextualGraph,
                    missingTraversalTargetResolvingStrategy
                ),
                this.genericDeserializer.resolveInternally(
                    List.of(element),
                    this.updateLastGraphDescription(
                        lastGraphDescription,
                        mapMapping.getGraphDescription()
                    ),
                    mapMapping.getValueObjectGraphMapping(),
                    contextualGraph,
                    missingTraversalTargetResolvingStrategy
                )
            )
        );
    return map;
  }

  @Override
  public boolean supports(
      ObjectGraphMapping objectGraphMapping,
      List<TraversableGraphElement> elements
  ) {
    return objectGraphMapping instanceof MapObjectGraphMapping;
  }
}
