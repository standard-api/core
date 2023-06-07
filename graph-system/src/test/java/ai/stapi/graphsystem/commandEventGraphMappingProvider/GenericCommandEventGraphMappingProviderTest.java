package ai.stapi.graphsystem.commandEventGraphMappingProvider;

import ai.stapi.graphsystem.commandEventGraphMappingProvider.exampleImplementations.ExampleNotSupportedCommand;
import ai.stapi.graphsystem.commandEventGraphMappingProvider.exception.GraphMappingProviderException;
import ai.stapi.test.integration.IntegrationTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

public class GenericCommandEventGraphMappingProviderTest extends IntegrationTestCase {

  @Autowired
  private GenericCommandEventGraphMappingProvider commandMappingProvider;

  @Test
  public void itThrowsErrorWhenCommandIsNotSupported() {
    Executable throwable =
        () -> this.commandMappingProvider.provideGraphMappings(new ExampleNotSupportedCommand());
    this.thenExceptionMessageApproved(GraphMappingProviderException.class, throwable);
  }
}