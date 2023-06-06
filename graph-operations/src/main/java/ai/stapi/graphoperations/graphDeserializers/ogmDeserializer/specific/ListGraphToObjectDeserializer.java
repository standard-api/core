package ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.specific;

import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.GenericGraphToObjectDeserializer;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.MissingTraversalTargetResolvingStrategy;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.ListAttributeDescription;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.graphReader.GraphReader;
import ai.stapi.graphoperations.objectGraphLanguage.LeafObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ListObjectGraphMapping;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import ai.stapi.graphoperations.serializationTypeProvider.GenericSerializationTypeByNodeProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ListGraphToObjectDeserializer extends AbstractSpecificGraphToObjectDeserializer {

  public ListGraphToObjectDeserializer(
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
    var listMapping = (ListObjectGraphMapping) graphMapping;
    if (elements.isEmpty()) {
      return new ArrayList<>();
    }
    if (listMapping.getChildObjectGraphMapping() instanceof LeafObjectGraphMapping) {
      var values = elements.stream().map(element -> this.traverseSingleGraphBranchToValue(
              element.getId(),
              listMapping.getGraphDescription(),
              contextualGraph,
              missingTraversalTargetResolvingStrategy
          )
      ).collect(Collectors.toList());
      if (listMapping.getGraphDescription() instanceof ListAttributeDescription) {
        return values.stream()
            .filter(List.class::isInstance)
            .map(List.class::cast)
            .flatMap(List::stream)
            .collect(Collectors.toList());
      }
      return values;
    }
    return elements.stream().map(element -> this.traverseMultipleGraphBranch(
            element.getId(),
            listMapping.getGraphDescription(),
            contextualGraph
        )
    ).flatMap(List::stream).map(element -> this.genericDeserializer.resolveInternally(
            List.of(element),
            this.updateLastGraphDescription(
                lastGraphDescription,
                listMapping.getGraphDescription()
            ),
            listMapping.getChildObjectGraphMapping(),
            contextualGraph,
            missingTraversalTargetResolvingStrategy
        )
    ).toList();
  }

  @Override
  public boolean supports(
      ObjectGraphMapping objectGraphMapping,
      List<TraversableGraphElement> elements
  ) {
    return objectGraphMapping instanceof ListObjectGraphMapping;
  }
}
