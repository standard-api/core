package ai.stapi.graphsystem.commandEventGraphMappingProvider.exampleImplementations;

import ai.stapi.graphsystem.messaging.command.AbstractCommand;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;

public class ExampleCommand extends AbstractCommand<ExampleIdentity> {

  public static final String SERIALIZATION_TYPE = "2c7643f1-d4bb-4654-9c96-6fd75136133c";
  private final String name;
  private final ObjectGraphMapping conceptDefinition;

  public ExampleCommand(
      ExampleIdentity targetIdentifier,
      String name,
      ObjectGraphMapping conceptDefinition
  ) {
    super(targetIdentifier, SERIALIZATION_TYPE);
    this.name = name;
    this.conceptDefinition = conceptDefinition;
  }

  public String getName() {
    return name;
  }


  public ObjectGraphMapping getConceptDefinition() {
    return conceptDefinition;
  }
}
