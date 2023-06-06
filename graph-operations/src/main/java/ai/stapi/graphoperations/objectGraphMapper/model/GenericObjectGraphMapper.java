package ai.stapi.graphoperations.objectGraphMapper.model;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphWriter.GenericGraphWriter;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphMapper.model.exceptions.ObjectGraphMapperException;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import ai.stapi.serialization.SerializableObject;
import java.util.List;
import java.util.Map;

public class GenericObjectGraphMapper {

  private final List<SpecificObjectGraphMapper> specificObjectGraphMappers;
  private final GenericGraphWriter writer;
  private final GenericGraphMappingProvider mappingProvider;

  public GenericObjectGraphMapper(
      List<SpecificObjectGraphMapper> specificObjectGraphMappers,
      GenericGraphWriter writer,
      GenericGraphMappingProvider mappingProvider
  ) {
    this.specificObjectGraphMappers = specificObjectGraphMappers;
    this.writer = writer;
    this.mappingProvider = mappingProvider;
  }

  public GraphMappingResult mapToGraph(
      ObjectGraphMapping objectGraphMapping,
      Object object
  ) {
    return this.mapToGraph(objectGraphMapping, object, MissingFieldResolvingStrategy.STRICT);
  }

  public GraphMappingResult mapToGraph(
      ObjectGraphMapping objectGraphMapping,
      Object object,
      MissingFieldResolvingStrategy missingFieldResolvingStrategy
  ) {
    if (object == null) {
      throw ObjectGraphMapperException.becausegivenObjectIsNull(objectGraphMapping);
    }
    if (!(objectGraphMapping instanceof ObjectObjectGraphMapping)) {
      throw ObjectGraphMapperException.becauseFirstObjectGraphMappingIsNotObject(
          objectGraphMapping);
    }
    var builder = new GraphDescriptionBuilder();
    this.resolveInternally(
        objectGraphMapping,
        Map.entry("", object),
        builder,
        missingFieldResolvingStrategy
    );
    var graph = this.writer.createGraph(
        builder.getOnlyPositiveGraphDescriptions()
    );

    var elementsForRemoval = this.writer.createElementsForRemoval(
        builder.getOnlyRemovalGraphDescriptions()
    );
    return new GraphMappingResult(graph, elementsForRemoval);
  }

  public GraphMappingResult mapToGraph(SerializableObject object) {
    return this.mapToGraph(object, MissingFieldResolvingStrategy.STRICT);
  }

  public GraphMappingResult mapToGraph(
      SerializableObject object,
      MissingFieldResolvingStrategy missingFieldResolvingStrategy
  ) {
    var objectGraphMapping = this.mappingProvider.provideGraphMapping(object.getSerializationType());
    return this.mapToGraph(objectGraphMapping, object, missingFieldResolvingStrategy);
  }

  public GraphMappingResult mapToGraph(
      Object object,
      String serializationType,
      MissingFieldResolvingStrategy missingFieldResolvingStrategy
  ) {
    var objectGraphMapping = this.mappingProvider.provideGraphMapping(serializationType);
    return this.mapToGraph(objectGraphMapping, object, missingFieldResolvingStrategy);
  }

  public void resolveInternally(
      ObjectGraphMapping graphMapping,
      Map.Entry<String, Object> fieldEntry,
      GraphDescriptionBuilder builder,
      MissingFieldResolvingStrategy missingFieldResolvingStrategy
  ) {
    var supportingResolver = this.getSupportingSpecificMapper(graphMapping);
    supportingResolver.createGraphDescriptionWithValues(
        graphMapping,
        fieldEntry,
        builder,
        missingFieldResolvingStrategy
    );
  }

  private SpecificObjectGraphMapper getSupportingSpecificMapper(
      ObjectGraphMapping objectGraphMapping) {
    var supportingGraphWriters = this.specificObjectGraphMappers.stream()
        .filter(
            specificObjectGraphMapper -> specificObjectGraphMapper.supports(objectGraphMapping));

    var listOfSupportingResolvers = supportingGraphWriters.toList();
    if (listOfSupportingResolvers.size() == 0) {
      throw ObjectGraphMapperException.becauseNoSupportingSpecificResolverForGivenDeclaration(
          objectGraphMapping);
    }
    if (listOfSupportingResolvers.size() > 1) {
      throw ObjectGraphMapperException.becauseMoreThanOneSpecificResolverForGivenDeclaration(
          objectGraphMapping,
          listOfSupportingResolvers
      );
    }
    return listOfSupportingResolvers.get(0);
  }
}
