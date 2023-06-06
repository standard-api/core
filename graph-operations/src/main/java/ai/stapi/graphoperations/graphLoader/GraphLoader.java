package ai.stapi.graphoperations.graphLoader;

import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.GraphElementQueryDescription;
import ai.stapi.identity.UniqueIdentifier;

import java.util.List;

public interface GraphLoader {

  <T> GraphLoaderFindAsObjectOutput<T> find(
      GraphElementQueryDescription graphElementQueryDescription,
      Class<T> objectClass,
      GraphLoaderReturnType... returnTypes
  );

  <T> GraphLoaderGetAsObjectOutput<T> get(
      UniqueIdentifier elementId,
      GraphElementQueryDescription graphElementQueryDescription,
      Class<T> objectClass,
      GraphLoaderReturnType... returnTypes
  );

  List<TraversableGraphElement> findAsTraversable(GraphElementQueryDescription graphDescription);

  TraversableGraphElement getAsTraversable(UniqueIdentifier elementId,
                                           GraphElementQueryDescription graphDescription);
}
