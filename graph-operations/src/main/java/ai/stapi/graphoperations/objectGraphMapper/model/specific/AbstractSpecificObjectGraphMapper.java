package ai.stapi.graphoperations.objectGraphMapper.model.specific;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.ConstantDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NullGraphDescription;
import ai.stapi.graphoperations.objectGraphMapper.model.GenericObjectGraphMapper;
import ai.stapi.graphoperations.objectGraphMapper.model.MissingFieldResolvingStrategy;
import ai.stapi.graphoperations.objectGraphMapper.model.SpecificObjectGraphMapper;
import ai.stapi.graphoperations.objectGraphMapper.model.specific.exceptions.SpecificObjectGraphMapperException;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.Map;

public abstract class AbstractSpecificObjectGraphMapper implements SpecificObjectGraphMapper {

  protected final GenericObjectGraphMapper genericGraphMapper;
  protected final ObjectMapper jsonObjectMapper;

  public AbstractSpecificObjectGraphMapper(GenericObjectGraphMapper genericGraphMapper) {
    this.genericGraphMapper = genericGraphMapper;
    this.jsonObjectMapper = new ObjectMapper();
    this.jsonObjectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
  }

  @Override
  public abstract GraphDescriptionBuilder createGraphDescriptionWithValues(
      ObjectGraphMapping objectGraphMapping,
      Map.Entry<String, Object> fieldEntry,
      GraphDescriptionBuilder builder,
      MissingFieldResolvingStrategy missingFieldResolvingStrategy);

  @Override
  public abstract boolean supports(ObjectGraphMapping objectGraphMapping);

  protected GraphDescriptionBuilder addGraphDescriptionCompositeToBuilder(
      GraphDescription graphDescription,
      GraphDescriptionBuilder rootBuilderBranch
  ) {
    if (graphDescription instanceof NullGraphDescription) {
      return rootBuilderBranch;
    }
    var newBranch =
        rootBuilderBranch.addBuilderCopyOfGraphDescriptionWithNoChildrenToBuilder(graphDescription);
    this.ensureGraphDescriptionChildrenAreValid(graphDescription);
    for (GraphDescription child : graphDescription.getChildGraphDescriptions()) {
      newBranch = this.addGraphDescriptionCompositeToBuilder(child, newBranch);
    }
    return newBranch;
  }

  private void ensureGraphDescriptionChildrenAreValid(GraphDescription graphDescription) {
    var nonConstantChildren = graphDescription.getChildGraphDescriptions().stream()
        .filter(child -> (!(child instanceof ConstantDescription)))
        .toList();
    if (nonConstantChildren.size() > 1) {
      throw SpecificObjectGraphMapperException.becauseGraphDescriptionHasMultipleNonConstantChildren(
          graphDescription, nonConstantChildren.size());
    }
  }
}
