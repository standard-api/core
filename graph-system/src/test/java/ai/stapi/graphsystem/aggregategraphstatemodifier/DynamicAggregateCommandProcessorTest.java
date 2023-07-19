package ai.stapi.graphsystem.aggregategraphstatemodifier;

import ai.stapi.graph.Graph;
import ai.stapi.graphoperations.objectGraphMapper.model.MissingFieldResolvingStrategy;
import ai.stapi.graphsystem.messaging.command.DynamicCommand;
import ai.stapi.graphsystem.systemfixtures.model.SystemModelDefinitionsLoader;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.test.schemaintegration.SchemaIntegrationTestCase;
import ai.stapi.test.schemaintegration.StructureDefinitionScope;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@StructureDefinitionScope(SystemModelDefinitionsLoader.SCOPE)
class DynamicAggregateCommandProcessorTest extends SchemaIntegrationTestCase {

  @Autowired
  private DynamicAggregateCommandProcessor dynamicAggregateCommandProcessor;

  @Test
  public void itWillCreateMainNodeOnly() {
    var actualEvent = this.dynamicAggregateCommandProcessor.processCommand(
        new DynamicCommand(
            new UniqueIdentifier("SomeId"),
            "CreateParameters",
            Map.of()
        ),
        new Graph()
    );
    Assertions.assertEquals(1, actualEvent.size());
    this.thenGraphApproved(actualEvent.get(0).getSynchronizedGraph());
  }

  @Test
  public void itWillCreateWithPrimitiveParameters() {
    var actualEvent = this.dynamicAggregateCommandProcessor.processCommand(
        new DynamicCommand(
            new UniqueIdentifier("SomeId"),
            "CreateParameters",
            Map.of(
                "language", "cz",
                "implicitRules", "http://some.uri.com"
            )
        ),
        new Graph()
    );
    Assertions.assertEquals(1, actualEvent.size());
    this.thenGraphApproved(actualEvent.get(0).getSynchronizedGraph());
  }

  @Test
  public void itWillCreateWithComplexParameter() {
    var actualEvent = this.dynamicAggregateCommandProcessor.processCommand(
        new DynamicCommand(
            new UniqueIdentifier("SomeId"),
            "CreateParameters",
            Map.of(
                "language", "cz",
                "meta", Map.of(
                    "source", "http://some.source.uri"
                )
            )
        ),
        new Graph(),
        MissingFieldResolvingStrategy.LENIENT
    );
    Assertions.assertEquals(1, actualEvent.size());
    this.thenGraphApproved(actualEvent.get(0).getSynchronizedGraph());
  }

  @Test
  public void itWillCreateWithListParameter() {
    var actualEvent = this.dynamicAggregateCommandProcessor.processCommand(
        new DynamicCommand(
            new UniqueIdentifier("SomeId"),
            "CreateParameters",
            Map.of(
                "language", "cz",
                "parameter", List.of(
                    Map.of(
                        "name", "someParameterName1"
                    ),
                    Map.of(
                        "name", "someParameterName2"
                    ),
                    Map.of(
                        "name", "someParameterName3"
                    )
                )
            )
        ),
        new Graph(),
        MissingFieldResolvingStrategy.LENIENT
    );
    Assertions.assertEquals(1, actualEvent.size());
    this.thenGraphApproved(actualEvent.get(0).getSynchronizedGraph());
  }

  @Test
  public void itWillCreateWithContentReferenceParameter() {
    var actualEvent = this.dynamicAggregateCommandProcessor.processCommand(
        new DynamicCommand(
            new UniqueIdentifier("SomeId"),
            "CreateParameters",
            Map.of(
                "language", "cz",
                "parameter", List.of(
                    Map.of(
                        "name", "basicParameterName"
                    ),
                    Map.of(
                        "name", "compositeParameter",
                        "part", List.of(
                            Map.of(
                                "name", "childParameter"
                            )
                        )
                    )
                )
            )
        ),
        new Graph(),
        MissingFieldResolvingStrategy.LENIENT
    );
    Assertions.assertEquals(1, actualEvent.size());
    this.thenGraphApproved(actualEvent.get(0).getSynchronizedGraph());
  }

  @Test
  public void itWillCreateWithUnionParameter() {
    var actualEvent = this.dynamicAggregateCommandProcessor.processCommand(
        new DynamicCommand(
            new UniqueIdentifier("SomeId"),
            "CreateParameters",
            Map.of(
                "language", "cz",
                "parameter", List.of(
                    Map.of(
                        "name", "basicParameterName"
                    ),
                    Map.of(
                        "name", "parameterWithUnionName",
                        "value", Map.of(
                            "serializationType", "BoxedString",
                            "value", "SomeValue"
                        )
                    ),
                    Map.of(
                        "name", "anotherParameterWithUnionName",
                        "value", Map.of(
                            "serializationType", "Coding",
                            "code", "SomeCode"
                        )
                    )
                )
            )
        ),
        new Graph(),
        MissingFieldResolvingStrategy.LENIENT
    );
    Assertions.assertEquals(1, actualEvent.size());
    this.thenGraphApproved(actualEvent.get(0).getSynchronizedGraph());
  }
}