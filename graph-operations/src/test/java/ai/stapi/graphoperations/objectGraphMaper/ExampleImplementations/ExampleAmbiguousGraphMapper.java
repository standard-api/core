package ai.stapi.graphoperations.objectGraphMaper.ExampleImplementations;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphMapper.model.MissingFieldResolvingStrategy;
import ai.stapi.graphoperations.objectGraphMapper.model.SpecificObjectGraphMapper;
import java.util.Map.Entry;
import org.springframework.stereotype.Service;

@Service
public class ExampleAmbiguousGraphMapper implements SpecificObjectGraphMapper {

  @Override
  public GraphDescriptionBuilder createGraphDescriptionWithValues(
      ObjectGraphMapping objectGraphMapping,
      Entry<String, Object> fieldEntry,
      GraphDescriptionBuilder builder,
      MissingFieldResolvingStrategy missingFieldResolvingStrategy
  ) {
    return null;
  }

  @Override
  public boolean supports(ObjectGraphMapping objectGraphMapping) {
    return false;
  }
}
