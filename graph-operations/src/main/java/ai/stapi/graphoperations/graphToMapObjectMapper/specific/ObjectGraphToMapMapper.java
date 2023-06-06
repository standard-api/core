package ai.stapi.graphoperations.graphToMapObjectMapper.specific;

import ai.stapi.graph.Graph;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectObjectGraphMapping;
import ai.stapi.graphoperations.graphToMapObjectMapper.exception.GraphToMapObjectMapperException;
import ai.stapi.graphoperations.graphToMapObjectMapper.GraphToMapObjectMapper;
import ai.stapi.graphoperations.graphReader.GraphReader;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ObjectGraphToMapMapper extends AbstractSpecificGraphToMapMapper {

  public ObjectGraphToMapMapper(
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
    if (elements.size() > 1) {
      throw GraphToMapObjectMapperException.becauseObjectCannotBeResolvedFromMultipleStartingElements(elements);
    }
    var objectMap = new HashMap<String, Object>();
    objectMap.put(
        "serializationType",
        this.resolveSerializationType(elements.get(0))
    );
    var objectOgm = (ObjectObjectGraphMapping) mapping;
    var elementsAfterTraversingObjectOgm = this.traverseGraph(
        objectOgm.getGraphDescription(),
        elements,
        graph
    );
    if (elementsAfterTraversingObjectOgm.size() > 1) {
      throw GraphToMapObjectMapperException.becauseObjectsCannotSplitIntoMultipleBranchedInsideObjectOgm();
    }
    objectOgm.getFields().forEach(
        (fieldName, field) -> {
          var traversedField = this.traverseGraph(
              field.getRelation(),
              elementsAfterTraversingObjectOgm,
              graph
          );
          if (traversedField.isEmpty()) {
            return;
          }
          var fieldValue = this.graphToMapObjectMapper.resolveInternally(
              traversedField,
              field.getFieldObjectGraphMapping(),
              graph
          );
          if (fieldValue != null) {
            objectMap.put(fieldName, fieldValue);
          }
        }
    );
    return objectMap;
  }

  @Override
  public boolean supports(ObjectGraphMapping mapping) {
    return mapping instanceof ObjectObjectGraphMapping;
  }
}
