package ai.stapi.graphoperations.objectGraphMapper.model.specific;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.objectGraphMapper.model.GenericObjectGraphMapper;
import ai.stapi.graphoperations.objectGraphMapper.model.MissingFieldResolvingStrategy;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ReferenceObjectGraphMapping;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import java.util.Map;

public class ReferenceSpecificObjectGraphMapper extends AbstractSpecificObjectGraphMapper {

  private final GenericGraphMappingProvider genericGraphMappingProvider;

  public ReferenceSpecificObjectGraphMapper(
      GenericObjectGraphMapper genericGraphMapper,
      GenericGraphMappingProvider genericGraphMappingProvider
  ) {
    super(genericGraphMapper);
    this.genericGraphMappingProvider = genericGraphMappingProvider;
  }

  @Override
  public GraphDescriptionBuilder createGraphDescriptionWithValues(
      ObjectGraphMapping objectGraphMapping,
      Map.Entry<String, Object> fieldEntry,
      GraphDescriptionBuilder builder,
      MissingFieldResolvingStrategy missingFieldResolvingStrategy
  ) {
    var referenceOgm = (ReferenceObjectGraphMapping) objectGraphMapping;
    var fetchedOgm = genericGraphMappingProvider.provideGraphMapping(
        referenceOgm.getReferencedSerializationType(),
        fieldEntry.getKey()
    );
    var graphDescription = referenceOgm.getGraphDescription();
    var resolvedBranch = this.addGraphDescriptionCompositeToBuilder(graphDescription, builder);
    this.genericGraphMapper.resolveInternally(
        fetchedOgm,
        fieldEntry,
        resolvedBranch,
        missingFieldResolvingStrategy
    );
    return builder;
  }

  @Override
  public boolean supports(ObjectGraphMapping objectGraphMapping) {
    return objectGraphMapping instanceof ReferenceObjectGraphMapping;
  }
}
