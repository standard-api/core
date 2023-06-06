package ai.stapi.graphoperations.objectGraphMapper.model.specific;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.objectGraphMapper.model.GenericObjectGraphMapper;
import ai.stapi.graphoperations.objectGraphMapper.model.MissingFieldResolvingStrategy;
import ai.stapi.graphoperations.objectGraphMapper.model.specific.exceptions.SpecificObjectGraphMapperException;
import ai.stapi.graphoperations.objectGraphLanguage.MapObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class MapSpecificObjectGraphMapper extends AbstractSpecificObjectGraphMapper {

  public MapSpecificObjectGraphMapper(GenericObjectGraphMapper genericGraphMapper) {
    super(genericGraphMapper);
  }

  @Override
  public GraphDescriptionBuilder createGraphDescriptionWithValues(
      ObjectGraphMapping objectGraphMapping,
      Map.Entry<String, Object> fieldEntry,
      GraphDescriptionBuilder builder,
      MissingFieldResolvingStrategy missingFieldResolvingStrategy
  ) {
    var mapMapping = (MapObjectGraphMapping) objectGraphMapping;
    var objectMap = this.convertObjectToMap(fieldEntry.getValue(), fieldEntry.getKey());
    objectMap.forEach(
        (key, value) -> {
          var resolvedMapBuilderBranch = this.addGraphDescriptionCompositeToBuilder(
              mapMapping.getGraphDescription(),
              builder
          );
          this.genericGraphMapper.resolveInternally(
              mapMapping.getKeyObjectGraphMapping(),
              Map.entry("", key),
              resolvedMapBuilderBranch,
              missingFieldResolvingStrategy
          );
          this.genericGraphMapper.resolveInternally(
              mapMapping.getValueObjectGraphMapping(),
              Map.entry("", value),
              resolvedMapBuilderBranch,
              missingFieldResolvingStrategy
          );
        }
    );
    return builder;
  }

  private Map<String, Object> convertObjectToMap(Object object, String fieldName) {
    try {
      return this.jsonObjectMapper.convertValue(object, new TypeReference<>() {
      });
    } catch (IllegalArgumentException error) {
      throw SpecificObjectGraphMapperException.becauseObjectCouldNotBeConverted(this,
          fieldName,
          object,
          error);
    }
  }

  @Override
  public boolean supports(ObjectGraphMapping objectGraphMapping) {
    return objectGraphMapping instanceof MapObjectGraphMapping;
  }
}
