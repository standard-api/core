package ai.stapi.graphsystem.aggregatedefinition.model.exceptions;

public class CannotProvideAggregateDefinition extends RuntimeException {

  public CannotProvideAggregateDefinition(String message) {
    super(message);
  }

  private CannotProvideAggregateDefinition(
      String aggregateName,
      String becauseMessage,
      Throwable cause
  ) {
    super(
        String.format(
            "Cannot Provide Aggregate Definition for: '%s', because %s%nCause: %s",
            aggregateName,
            becauseMessage,
            cause.getMessage()
        )
    );
  }

  public static CannotProvideAggregateDefinition becauseItDoesNotExist(String aggregateName) {
    return new CannotProvideAggregateDefinition(
        String.format(
            "Cannot Provide Aggregate Definition for: '%s', because it does not exists.",
            aggregateName
        )
    );
  }

  public static CannotProvideAggregateDefinition becauseRelatedOperationDefinitionNotFound(
      String aggregateName,
      String operationName,
      Throwable cause
  ) {
    return new CannotProvideAggregateDefinition(
        aggregateName,
        String.format("related operation with name '%s' was not found.", operationName),
        cause
    );
  }

  public static CannotProvideAggregateDefinition becauseRelatedStructureSchemaNotFound(
      String aggregateName,
      Throwable cause
  ) {
    return new CannotProvideAggregateDefinition(
        aggregateName,
        "related Structure Schema was not found.",
        cause
    );
  }


  public static CannotProvideAggregateDefinition becauseThereIsNoCommandHandlerForOperation(
      String operationDefinitionId
  ) {
    return new CannotProvideAggregateDefinition(
        String.format(
            "Cannot Provide Aggregate Definition for: '%s', because there is no command handler for this operation.",
            operationDefinitionId
        )
    );
  }
}
