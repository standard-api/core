package ai.stapi.graphsystem.commandEventGraphMappingProvider.exception;

import ai.stapi.graphsystem.messaging.command.AbstractCommand;
import ai.stapi.graphsystem.commandEventGraphMappingProvider.specific.SpecificCommandEventGraphMappingProvider;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class GraphMappingProviderException extends RuntimeException {

  private GraphMappingProviderException(String message) {
    super(message);
  }

  public static GraphMappingProviderException becauseThereIsNoSupportingSpecificGraphMappingProvider(
      String deserializationType) {
    return new GraphMappingProviderException(
        String.format(
            "\nThere is no supporting %s"
                + " for object of type:\n%s",
            SpecificCommandEventGraphMappingProvider.class.getSimpleName(),
            deserializationType
        )
    );
  }

  public static GraphMappingProviderException becauseThereAreMoreThanOneSpecificGraphMappingProviders(
      Class<?> classType,
      List<SpecificCommandEventGraphMappingProvider> listOfSupportingProviders
  ) {
    return new GraphMappingProviderException(
        "Object of type '"
            + classType.getSimpleName()
            + "' is supported by multiple providers ("
            + StringUtils.join(listOfSupportingProviders, ", ")
            + ") and that is not allowed."
    );
  }

  public static GraphMappingProviderException becauseCommandDoesNotContainGivenField(
      Class<? extends AbstractCommand> classType,
      String fieldName
  ) {
    return new GraphMappingProviderException(
        "Command of type '" + classType.getSimpleName() + "' does not contain field with name '"
            + fieldName + "'."
    );
  }
}
