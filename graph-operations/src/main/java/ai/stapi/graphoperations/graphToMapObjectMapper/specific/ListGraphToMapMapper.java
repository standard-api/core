package ai.stapi.graphoperations.graphToMapObjectMapper.specific;

import ai.stapi.graph.Graph;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractEdgeDescription;
import ai.stapi.graphoperations.objectGraphLanguage.ListObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.graphToMapObjectMapper.GraphToMapObjectMapper;
import ai.stapi.graphoperations.graphReader.GraphReader;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class ListGraphToMapMapper extends AbstractSpecificGraphToMapMapper {

  public ListGraphToMapMapper(
      GraphReader graphReader,
      GraphToMapObjectMapper graphToMapObjectMapper,
      GenericGraphMappingProvider genericGraphMappingProvider
  ) {
    super(graphReader, graphToMapObjectMapper, genericGraphMappingProvider);
  }

  @Override
  public Object resolve(
      List<TraversableGraphElement> elements,
      ObjectGraphMapping mapping,
      Graph graph
  ) {
    var traversedList = this.traverseGraph(
        mapping.getGraphDescription(),
        elements,
        graph
    );
    var listOgm = (ListObjectGraphMapping) mapping;
    var childObjectGraphMapping = listOgm.getChildObjectGraphMapping();
    var childGraphDescription = childObjectGraphMapping.getGraphDescription();
    if (childGraphDescription instanceof AbstractEdgeDescription) {
      traversedList = this.traverseGraph(
          childGraphDescription,
          traversedList,
          graph
      );
    }
    var resultingList = traversedList.stream()
        .map(element -> this.graphToMapObjectMapper.resolveInternally(
                List.of(element),
                childObjectGraphMapping,
                graph
            )
        ).filter(Objects::nonNull)
        .toList();
    if (resultingList.isEmpty()) {
      return null;
    }
    return resultingList;
  }

  @Override
  public boolean supports(ObjectGraphMapping mapping) {
    return mapping instanceof ListObjectGraphMapping;
  }
}
