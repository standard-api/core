package ai.stapi.graphsystem.aggregategraphstatemodifier;

import ai.stapi.graph.Graph;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.attribute.attributeValue.UriAttributeValue;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graphoperations.objectGraphMapper.model.MissingFieldResolvingStrategy;
import ai.stapi.graphsystem.aggregategraphstatemodifier.exceptions.CannotAddToAggregateState;
import ai.stapi.graphsystem.messaging.command.DynamicCommand;
import ai.stapi.graphsystem.systemfixtures.model.SystemModelDefinitionsLoader;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.test.schemaintegration.SchemaIntegrationTestCase;
import ai.stapi.test.schemaintegration.StructureDefinitionScope;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
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

  @Test
  public void itWillCreateTargetNodeWhenAddingItemAndTargetNodeDoesntExist() {
    var actualEvent = this.dynamicAggregateCommandProcessor.processCommand(
        new DynamicCommand(
            new UniqueIdentifier("SomeId"),
            "AddParameterOnParameters",
            Map.of(
                "parameter", List.of(
                    Map.of("name", "someNewParameter")
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
  public void itWillAddItemOnAlreadyExistingGraphAndEventWillOnlyContainNewData() {
    var someId = new UniqueIdentifier("SomeId");
    var mainNode = new Node(someId, "Parameters");
    var alreadyExistingParameterNode = new Node(
        "ParametersParameter",
        new LeafAttribute<>("name", new StringAttributeValue("alreadyExistingParameter"))
    );
    var edge = new Edge(mainNode, "parameter", alreadyExistingParameterNode);
    var actualEvent = this.dynamicAggregateCommandProcessor.processCommand(
        new DynamicCommand(
            someId,
            "AddParameterOnParameters",
            Map.of(
                "parameter", List.of(
                    Map.of("name", "someNewParameter")
                )
            )
        ),
        new Graph(mainNode, alreadyExistingParameterNode, edge),
        MissingFieldResolvingStrategy.LENIENT
    );
    Assertions.assertEquals(1, actualEvent.size());
    this.thenGraphApproved(actualEvent.get(0).getSynchronizedGraph());
  }

  @Test
  public void itWillThrowExceptionWhenStartIdParameterIsNotPresent() {
    var someId = new UniqueIdentifier("SomeId");
    Executable throwable = () -> this.dynamicAggregateCommandProcessor.processCommand(
        new DynamicCommand(
            someId,
            "AddPartOnParametersParameter",
            Map.of(
                "part", List.of(
                    Map.of("name", "someNewDeeperParameter")
                )
            )
        ),
        new Graph(),
        MissingFieldResolvingStrategy.LENIENT
    );
    this.thenExceptionMessageApproved(CannotAddToAggregateState.class, throwable);
  }

  @Test
  public void itWillThrowExceptionWhenStartIdIsNotOfCorrectFormat() {
    var someId = new UniqueIdentifier("SomeId");
    Executable throwable = () -> this.dynamicAggregateCommandProcessor.processCommand(
        new DynamicCommand(
            someId,
            "AddPartOnParametersParameter",
            Map.of(
                "parameterId", "BadStartId",
                "part", List.of(
                    Map.of("name", "someNewDeeperParameter")
                )
            )
        ),
        new Graph(),
        MissingFieldResolvingStrategy.LENIENT
    );
    this.thenExceptionMessageApproved(CannotAddToAggregateState.class, throwable);
  }

  @Test
  public void itWillNotAddItemOnDeeperNodeWhenStartNodeDoesntExist() {
    var someId = new UniqueIdentifier("SomeId");
    Executable throwable = () -> this.dynamicAggregateCommandProcessor.processCommand(
        new DynamicCommand(
            someId,
            "AddPartOnParametersParameter",
            Map.of(
                "parameterId", "ParametersParameter/NonExistingParameterId",
                "part", List.of(
                    Map.of("name", "someNewDeeperParameter")
                )
            )
        ),
        new Graph(),
        MissingFieldResolvingStrategy.LENIENT
    );
    this.thenExceptionMessageApproved(CannotAddToAggregateState.class, throwable);
  }

  @Test
  public void itWillAddItemOnDeeperNodeAndOnlyIncludeNewDataInEvent() {
    var someId = new UniqueIdentifier("SomeId");
    var someParameterId = "SomeParameterId";
    var mainNode = new Node(someId, "Parameters");
    var startNode = new Node(
        new UniqueIdentifier(someParameterId),
        "ParametersParameter",
        new LeafAttribute<>("name", new StringAttributeValue("alreadyExistingParameter"))
    );
    var edge = new Edge(mainNode, "parameter", startNode);
    var actualEvent = this.dynamicAggregateCommandProcessor.processCommand(
        new DynamicCommand(
            someId,
            "AddPartOnParametersParameter",
            Map.of(
                "parameterId", String.format("ParametersParameter/%s", someParameterId),
                "part", List.of(
                    Map.of("name", "someNewDeeperParameter")
                )
            )
        ),
        new Graph(mainNode, startNode, edge),
        MissingFieldResolvingStrategy.LENIENT
    );
    Assertions.assertEquals(1, actualEvent.size());
    this.thenGraphApproved(actualEvent.get(0).getSynchronizedGraph());
  }

  @Test
  public void itWillThrowExceptionWhenAddingParameterWhichAlreadyExists() {
    var someId = new UniqueIdentifier("SomeId");
    var mainNode = new Node(
        someId,
        "Parameters",
        new LeafAttribute<>("language", new StringAttributeValue("cz"))
    );
    Executable throwable = () -> this.dynamicAggregateCommandProcessor.processCommand(
        new DynamicCommand(
            someId,
            "CreateParameters",
            Map.of(
                "language", "eng"
            )
        ),
        new Graph(mainNode)
    );
    this.thenExceptionMessageApproved(CannotAddToAggregateState.class, throwable);
  }

  @Test
  public void itWillThrowExceptionWhenAddingComplexTypeWhichAlreadyExists() {
    var someId = new UniqueIdentifier("SomeId");
    var mainNode = new Node(someId, "Parameters");
    var alreadyExistingComplex = new Node(
        "Meta",
        new LeafAttribute<>("source", new UriAttributeValue("http://some.already.existing.url"))
    );
    var edge = new Edge(mainNode, "meta", alreadyExistingComplex);
    Executable throwable = () -> this.dynamicAggregateCommandProcessor.processCommand(
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
        new Graph(mainNode, alreadyExistingComplex, edge)
    );
    this.thenExceptionMessageApproved(CannotAddToAggregateState.class, throwable);
  }
}