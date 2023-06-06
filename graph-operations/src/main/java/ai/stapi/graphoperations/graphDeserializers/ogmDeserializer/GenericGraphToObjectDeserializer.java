package ai.stapi.graphoperations.graphDeserializers.ogmDeserializer;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NullGraphDescription;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.exception.GenericGraphOgmDeserializerException;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.specific.InterfaceGraphToObjectDeserializer;
import ai.stapi.graphoperations.graphDeserializers.ogmDeserializer.specific.SpecificGraphToObjectDeserializer;
import ai.stapi.graphoperations.graphReader.GraphReader;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.serialization.SerializableObject;
import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class GenericGraphToObjectDeserializer {

  protected final GraphReader graphReader;
  protected final GenericGraphMappingProvider mappingProvider;
  private final List<SpecificGraphToObjectDeserializer> specificDeserializers;
  private final ObjectMapper objectMapper;

  public GenericGraphToObjectDeserializer(
      @Lazy List<SpecificGraphToObjectDeserializer> specificDeserializers,
      GraphReader graphReader,
      ObjectMapper objectMapper,
      GenericGraphMappingProvider mappingProvider
  ) {
    this.specificDeserializers = specificDeserializers;
    this.graphReader = graphReader;
    this.objectMapper = objectMapper;
    this.mappingProvider = mappingProvider;
  }

  @SuppressWarnings("unchecked")
  public <T extends SerializableObject> T deserialize(
      TraversableGraphElement element,
      Class<T> classType,
      InMemoryGraphRepository contextualGraph,
      MissingTraversalTargetResolvingStrategy missingTraversalTargetResolvingStrategy
  ) {
    var anonymousObject = this.getInterfaceDeserializer().deserialize(
        List.of(element),
        new NullGraphDescription(),
        null,
        contextualGraph,
        missingTraversalTargetResolvingStrategy
    );
    return (T) this.objectMapper.convertValue(anonymousObject, SerializableObject.class);
  }

  @SuppressWarnings("unchecked")
  public <T extends SerializableObject> T deserialize(
      TraversableGraphElement element,
      Class<T> classType,
      InMemoryGraphRepository contextualGraph
  ) {
    var anonymousObject = this.getInterfaceDeserializer().deserialize(
        List.of(element),
        new NullGraphDescription(),
        null,
        contextualGraph,
        MissingTraversalTargetResolvingStrategy.STRICT
    );
    return (T) this.objectMapper.convertValue(anonymousObject, SerializableObject.class);
  }

  public Object resolveInternally(
      List<TraversableGraphElement> elements,
      GraphDescription parentGraphDescription,
      ObjectGraphMapping objectGraphMapping,
      InMemoryGraphRepository contextualGraph,
      MissingTraversalTargetResolvingStrategy missingTraversalTargetResolvingStrategy
  ) {
    var deserializer = this.getSupportingDeserializer(
        elements,
        objectGraphMapping
    );
    return deserializer.deserialize(
        elements,
        parentGraphDescription,
        objectGraphMapping,
        contextualGraph,
        missingTraversalTargetResolvingStrategy
    );
  }

  private SpecificGraphToObjectDeserializer getSupportingDeserializer(
      List<TraversableGraphElement> elements,
      ObjectGraphMapping objectGraphMapping
  ) {
    var supportingDeserializers = this.specificDeserializers.stream()
        .filter(deserializer -> deserializer.supports(objectGraphMapping, elements))
        .toList();
    if (supportingDeserializers.isEmpty()) {
      throw GenericGraphOgmDeserializerException.becauseNodeTypeIsNotSupported(
          elements.get(0).getType());
    }
    if (supportingDeserializers.size() > 1) {
      throw GenericGraphOgmDeserializerException.becauseNodeTypeIsSupportedByMultipleDeserializers(
          elements.get(0).getType(),
          supportingDeserializers.stream()
              .map(SpecificGraphToObjectDeserializer::getClass)
              .map(Class::getSimpleName)
              .toList()
      );
    }
    return supportingDeserializers.get(0);
  }

  private SpecificGraphToObjectDeserializer getInterfaceDeserializer() {
    return this.specificDeserializers.stream()
        .filter(InterfaceGraphToObjectDeserializer.class::isInstance)
        .toList()
        .get(0);
  }

}
