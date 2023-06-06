package ai.stapi.graphoperations.objectGraphMapper.model.specific;

import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.InterfaceObjectGraphMapping;
import java.util.Map;

public interface InterfaceSpecificObjectGraphMapperFixStrategy {

  InterfaceObjectGraphMapping fix(
      InterfaceObjectGraphMapping interfaceObjectGraphMapping,
      Map.Entry<String, Object> fieldEntry,
      GraphDescriptionBuilder builder,
      String serializationType
  );
}
