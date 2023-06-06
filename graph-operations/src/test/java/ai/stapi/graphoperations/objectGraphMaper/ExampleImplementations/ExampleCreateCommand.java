package ai.stapi.graphoperations.objectGraphMaper.ExampleImplementations;

public class ExampleCreateCommand {

  private final ExampleEntityId exampleEntityId;
  private final String fallbackName;

  public ExampleCreateCommand(ExampleEntityId exampleEntityId, String fallbackName) {
    this.exampleEntityId = exampleEntityId;
    this.fallbackName = fallbackName;
  }

  public ExampleEntityId getExampleEntityId() {
    return exampleEntityId;
  }

  public String getFallbackName() {
    return fallbackName;
  }
}
