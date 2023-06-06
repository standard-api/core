package ai.stapi.graphoperations.serializationTypeProvider.specific;

import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;

public interface SpecificSerializationTypeProvider {

  String provideSerializationType(TraversableGraphElement element);

  boolean supports(TraversableGraphElement element);
}
