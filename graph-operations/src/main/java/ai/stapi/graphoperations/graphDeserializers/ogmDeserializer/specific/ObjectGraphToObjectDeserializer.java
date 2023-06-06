package ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.specific;

import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.GenericGraphToObjectDeserializer;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.MissingTraversalTargetResolvingStrategy;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.exception.GenericGraphOgmDeserializerException;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.specific.exception.SpecificGraphOgmDeserializerException;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.graphReader.GraphReader;
import ai.stapi.graphoperations.graphReader.mappingPartReadResolvers.exception.GraphDescriptionReadResolverException;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectObjectGraphMapping;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import ai.stapi.graphoperations.serializationTypeProvider.GenericSerializationTypeByNodeProvider;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ObjectGraphToObjectDeserializer extends AbstractSpecificGraphToObjectDeserializer {

  public ObjectGraphToObjectDeserializer(
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
    if (elements.size() == 0) {
      return null;
    }
    if (elements.size() > 1) {
      throw SpecificGraphOgmDeserializerException.becauseThereIsMultipleGraphBranches(
          graphMapping,
          elements
      );
    }
    elements = this.ensureElementsAreNodes(elements, lastGraphDescription);
    var objectGraphMapping = (ObjectObjectGraphMapping) graphMapping;
    var map = new HashMap<String, Object>();
    var traversedObject = this.traverseSingleGraphBranch(
        elements.get(0).getId(),
        graphMapping.getGraphDescription(),
        contextualGraph
    );
    objectGraphMapping.getFields().forEach(
        (fieldName, fieldDefinition) -> {
          var traversedField = this.traverseMultipleGraphBranch(
              traversedObject.getId(),
              fieldDefinition.getRelation(),
              contextualGraph
          );
          if (
              missingTraversalTargetResolvingStrategy.equals(
                  MissingTraversalTargetResolvingStrategy.LENIENT
              )
          ) {
            try {
              var object = this.genericDeserializer.resolveInternally(
                  traversedField,
                  this.updateLastGraphDescription(
                      lastGraphDescription,
                      (GraphDescription) fieldDefinition.getRelation()
                  ),
                  fieldDefinition.getFieldObjectGraphMapping(),
                  contextualGraph,
                  missingTraversalTargetResolvingStrategy
              );
              map.put(fieldName, object);
            } catch (GraphDescriptionReadResolverException |
                     GenericGraphOgmDeserializerException exception) {
              map.put(fieldName, null);
            }
          } else {
            var object = this.genericDeserializer.resolveInternally(
                traversedField,
                this.updateLastGraphDescription(
                    lastGraphDescription,
                    (GraphDescription) fieldDefinition.getRelation()
                ),
                fieldDefinition.getFieldObjectGraphMapping(),
                contextualGraph,
                missingTraversalTargetResolvingStrategy
            );
            map.put(fieldName, object);
          }
        }
    );
    return map;
  }

  @Override
  public boolean supports(
      ObjectGraphMapping objectGraphMapping,
      List<TraversableGraphElement> elements
  ) {
    return objectGraphMapping instanceof ObjectObjectGraphMapping;
  }
}
