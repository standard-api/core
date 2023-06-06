package ai.stapi.graphoperations.objectGraphMapper.model.specific;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.AbstractAttributeValueDescription;
import ai.stapi.graphoperations.objectGraphLanguage.LeafObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ListObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphMapper.model.specific.exceptions.SpecificObjectGraphMapperException;
import com.fasterxml.jackson.core.type.TypeReference;
import ai.stapi.graphoperations.objectGraphMapper.model.GenericObjectGraphMapper;
import ai.stapi.graphoperations.objectGraphMapper.model.MissingFieldResolvingStrategy;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ListSpecificObjectGraphMapper extends AbstractSpecificObjectGraphMapper {

  public ListSpecificObjectGraphMapper(GenericObjectGraphMapper genericGraphMapper) {
    super(genericGraphMapper);
  }

  @Override
  public GraphDescriptionBuilder createGraphDescriptionWithValues(
      ObjectGraphMapping objectGraphMapping,
      Map.Entry<String, Object> fieldEntry,
      GraphDescriptionBuilder builder,
      MissingFieldResolvingStrategy missingFieldResolvingStrategy
  ) {
    var listMapping = (ListObjectGraphMapping) objectGraphMapping;
    var objectList = this.convertObjectIntoList(fieldEntry.getValue(), fieldEntry.getKey());
    var childObjectGraphMapping = listMapping.getChildObjectGraphMapping();
    if (childObjectGraphMapping instanceof LeafObjectGraphMapping leafObjectGraphMapping) {
      var resolvedListBuilderBranch = this.addGraphDescriptionCompositeToBuilder(
          listMapping.getGraphDescription(),
          builder
      );
      var childDescription = leafObjectGraphMapping.getGraphDescription();
      if (childDescription instanceof AbstractAttributeValueDescription) {
        objectList.forEach(value -> {
          var leafFinishedBranch = this.addGraphDescriptionCompositeToBuilder(
              childDescription,
              resolvedListBuilderBranch
          );
          leafFinishedBranch.addConstantDescription(value);
        });
        return resolvedListBuilderBranch;
      }
    }
    objectList.forEach(listObject -> {
      var resolvedListBuilderBranch = this.addGraphDescriptionCompositeToBuilder(
          listMapping.getGraphDescription(),
          builder
      );
      this.genericGraphMapper.resolveInternally(
          childObjectGraphMapping,
          Map.entry("", listObject),
          resolvedListBuilderBranch,
          missingFieldResolvingStrategy
      );
    });
    return builder;
  }

  private List<Object> convertObjectIntoList(Object object, String fieldName) {
    try {
      return this.jsonObjectMapper.convertValue(object, new TypeReference<>() {
      });
    } catch (IllegalArgumentException error) {
      throw SpecificObjectGraphMapperException.becauseObjectCouldNotBeConverted(
          this,
          fieldName,
          object,
          error
      );
    }
  }

  @Override
  public boolean supports(ObjectGraphMapping objectGraphMapping) {
    return objectGraphMapping instanceof ListObjectGraphMapping;
  }

}
