package ai.stapi.graphsystem.aggregatedefinition.infrastructure;

import ai.stapi.graphsystem.aggregatedefinition.model.AggregateDefinitionDTO;
import ai.stapi.graphsystem.aggregatedefinition.model.AggregateDefinitionProvider;
import ai.stapi.graphsystem.aggregatedefinition.model.exceptions.CannotProvideAggregateDefinition;
import java.util.List;
import org.springframework.context.annotation.Primary;

@Primary
public class NullAggregateDefinitionProvider implements AggregateDefinitionProvider {

  @Override
  public List<AggregateDefinitionDTO> provideAll() {
    return List.of();
  }

  @Override
  public AggregateDefinitionDTO provide(String aggregateType)
      throws CannotProvideAggregateDefinition {
    throw CannotProvideAggregateDefinition.becauseItDoesNotExist(aggregateType);
  }
}
