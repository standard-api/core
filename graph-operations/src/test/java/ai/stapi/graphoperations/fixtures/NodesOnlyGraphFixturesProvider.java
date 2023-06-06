package ai.stapi.graphoperations.fixtures;

import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.ListAttribute;
import ai.stapi.graph.attribute.attributeValue.IntegerAttributeValue;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.graph.Graph;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class NodesOnlyGraphFixturesProvider {

  public static final String EXAMPLE_ENTITY_NODE_TYPE = "ExampleEntity";
  
  public Graph getFixturesGraph() {
    return new Graph(
        this.createExampleNode(
            new UniversallyUniqueIdentifier(UUID.randomUUID()),
            NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE,
            "Example Name 1",
            654,
            "Attribute Value"
        ),
        this.createExampleNode(
            new UniversallyUniqueIdentifier(UUID.randomUUID()),
            NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE,
            "Example Name 2",
            0,
            "Attribute Value"
        ),
        this.createExampleNode(
            new UniversallyUniqueIdentifier(UUID.randomUUID()),
            NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE,
            "Example Name 3",
            1000,
            "Some Other Attribute Value"
        ),
        this.createExampleNode(
            new UniversallyUniqueIdentifier(UUID.randomUUID()),
            NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE,
            "Example Name 4",
            23,
            "Attribute Value"
        ),
        this.createExampleNode(
            new UniversallyUniqueIdentifier(UUID.randomUUID()),
            NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE,
            "Example Name 5",
            653,
            "Some Other Attribute Value"
        ),
        this.createExampleNode(
            new UniversallyUniqueIdentifier(UUID.randomUUID()),
            NodesOnlyGraphFixturesProvider.EXAMPLE_ENTITY_NODE_TYPE,
            "Example Name 6",
            23,
            "Unique Attribute Value"
        )
    );
  }

  public Node createExampleNode(
      UniversallyUniqueIdentifier exampleNodeId,
      String exampleNodeType,
      String exampleNodeName,
      Integer exampleQuantity,
      String exampleStringAttribute,
      String... exampleListAttribute
  ) {
    var exampleEntityNode = new Node(
        exampleNodeId,
        exampleNodeType,
        new LeafAttribute<>(
            AttributeTypes.EXAMPLE_NAME,
            new StringAttributeValue(exampleNodeName)
        ),
        new LeafAttribute<>(
            AttributeTypes.EXAMPLE_QUANTITY,
            new IntegerAttributeValue(exampleQuantity)
        ),
        new LeafAttribute<>(
            AttributeTypes.EXAMPLE_STRING_ATTRIBUTE,
            new StringAttributeValue(exampleStringAttribute)
        )
    );
    if (exampleListAttribute.length > 0) {
      exampleEntityNode = exampleEntityNode.add(
          new ListAttribute(
              AttributeTypes.EXAMPLE_LIST_ATTRIBUTE,
              Arrays.stream(exampleListAttribute)
                  .map(StringAttributeValue::new)
                  .collect(Collectors.toList())
          )
      );
    }
    return exampleEntityNode;
  }
}
