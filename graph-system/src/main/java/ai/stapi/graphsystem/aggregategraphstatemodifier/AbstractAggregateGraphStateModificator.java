package ai.stapi.graphsystem.aggregategraphstatemodifier;

import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.graphoperations.objectGraphMapper.model.GenericObjectGraphMapper;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractAggregateGraphStateModificator implements AggregateGraphStateModificator {
  
  protected final GenericObjectGraphMapper objectGraphMapper;
  protected final EventFactoryModificationTraverser eventFactoryModificationTraverser;
  
  protected final EventModificatorOgmProvider eventModificatorOgmProvider;

  public AbstractAggregateGraphStateModificator(
      GenericObjectGraphMapper objectGraphMapper,
      EventFactoryModificationTraverser eventFactoryModificationTraverser,
      EventModificatorOgmProvider eventModificatorOgmProvider
  ) {
    this.objectGraphMapper = objectGraphMapper;
    this.eventFactoryModificationTraverser = eventFactoryModificationTraverser;
    this.eventModificatorOgmProvider = eventModificatorOgmProvider;
  }

  @NotNull
  protected Map<String, Object> getMappedObject(
      Object inputValue,
      TraversableNode modifiedNode,
      String fieldName
  ) {
    return new HashMap<>(Map.of(
        "id", modifiedNode.getId().getId(),
        fieldName, inputValue
    ));
  }
}
