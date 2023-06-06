package ai.stapi.graphoperations.objectGraphMapper.model.specific;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.LeafObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphMapper.model.specific.exceptions.SpecificObjectGraphMapperException;
import ai.stapi.graphoperations.objectGraphMapper.model.GenericObjectGraphMapper;
import ai.stapi.graphoperations.objectGraphMapper.model.MissingFieldResolvingStrategy;
import ai.stapi.utils.Classifier;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class LeafSpecificObjectGraphMapper extends AbstractSpecificObjectGraphMapper {

  public LeafSpecificObjectGraphMapper(GenericObjectGraphMapper genericGraphMapper) {
    super(genericGraphMapper);
  }

  @Override
  public GraphDescriptionBuilder createGraphDescriptionWithValues(
      ObjectGraphMapping objectGraphMapping,
      Map.Entry<String, Object> fieldEntry,
      GraphDescriptionBuilder builder,
      MissingFieldResolvingStrategy missingFieldResolvingStrategy
  ) {
    if (!Classifier.isPrimitiveOrString(fieldEntry.getValue())) {
      throw SpecificObjectGraphMapperException.becauseProvidedValueIsNotPrimitiveType(
          fieldEntry.getKey(),
          fieldEntry.getValue()
      );
    }
    var leafMapping = (LeafObjectGraphMapping) objectGraphMapping;
    var leafFinishedBranch = this.addGraphDescriptionCompositeToBuilder(
        leafMapping.getGraphDescription(),
        builder
    );
    leafFinishedBranch.addConstantDescription(fieldEntry.getValue());
    return leafFinishedBranch;
  }

  @Override
  public boolean supports(ObjectGraphMapping objectGraphMapping) {
    return objectGraphMapping instanceof LeafObjectGraphMapping;
  }
}
