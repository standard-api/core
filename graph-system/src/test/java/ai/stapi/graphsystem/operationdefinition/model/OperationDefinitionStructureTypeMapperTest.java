package ai.stapi.graphsystem.operationdefinition.model;

import ai.stapi.graphsystem.operationdefinition.model.testImplementations.TestOperationDefinitionProvider;
import ai.stapi.graphsystem.aggregatedefinition.infrastructure.NullAggregateDefinitionProvider;
import ai.stapi.graphsystem.operationdefinition.exceptions.CannotProvideOperationDefinition;
import ai.stapi.test.SystemSchemaIntegrationTestCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({TestOperationDefinitionProvider.class, NullAggregateDefinitionProvider.class})
class OperationDefinitionStructureTypeMapperTest extends SystemSchemaIntegrationTestCase {

  @Autowired
  private OperationDefinitionStructureTypeMapper mapper;

  @Autowired
  private OperationDefinitionProvider provider;

  @Test
  void itCanMap() throws CannotProvideOperationDefinition {
    var givenOperation = this.provider.provide(
        TestOperationDefinitionProvider.EXAMPLE_TEST_COMMAND
    );
    var actual = this.mapper.map(givenOperation);
    this.thenObjectApproved(actual);
  }
}