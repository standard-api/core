package ai.stapi.graphoperations.objectGraphMaper.ExampleImplementations;

import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;

public class ExampleCreateConceptCommand {

  public static final String SERIALIZATION_TYPE = "782b6f57-46e9-4cea-9ebf-0f892ec50f92";
  
  private final ExampleEntityId exampleEntityId;
  private final String fallbackName;
  private final ObjectGraphMapping conceptDefinition;

  public ExampleCreateConceptCommand(
      ExampleEntityId exampleEntityId,
      String fallbackName,
      ObjectGraphMapping conceptDefinition
  ) {
    this.exampleEntityId = exampleEntityId;
    this.fallbackName = fallbackName;
    this.conceptDefinition = conceptDefinition;
  }

  public ExampleEntityId getExampleEntityId() {
    return exampleEntityId;
  }

  public String getFallbackName() {
    return fallbackName;
  }

  public ObjectGraphMapping getConceptDefinition() {
    return conceptDefinition;
  }
}
