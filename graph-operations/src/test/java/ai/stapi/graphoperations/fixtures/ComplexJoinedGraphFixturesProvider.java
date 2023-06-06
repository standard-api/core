package ai.stapi.graphoperations.fixtures;

import ai.stapi.graph.NodeIdAndType;
import ai.stapi.graph.attribute.LeafAttribute;
import ai.stapi.graph.attribute.ListAttribute;
import ai.stapi.graph.attribute.attributeValue.IntegerAttributeValue;
import ai.stapi.graph.attribute.attributeValue.StringAttributeValue;
import ai.stapi.identity.UniqueIdentifier;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import ai.stapi.graph.Graph;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ComplexJoinedGraphFixturesProvider {

  public static final String NODE_TYPE_1 = "ExampleComplexJoinedGraphNodeType1";
  public static final String EDGE_TYPE_1 = "exampleComplexJoinedGraphEdgeType1";
  public static final String NODE_TYPE_2 = "ExampleComplexJoinedGraphNodeType2";
  public static final String EDGE_TYPE_2 = "exampleComplexJoinedGraphEdgeType2";
  public static final String NODE_TYPE_2V2 = "ExampleComplexJoinedGraphNodeType2Version2";
  public static final String NODE_TYPE_3 = "ExampleComplexJoinedGraphNodeType3";

  public static final String EDGE_TYPE_1_OTHER = "exampleComplexJoinedGraphEdgeType1Other";
  public static final String NODE_TYPE_2_OTHER = "ExampleComplexJoinedGraphNodeType2Other";
  public static final UniqueIdentifier MAIN_NODE_UUID_1 = UniversallyUniqueIdentifier.fromString(
      "913d8c30-6fea-4764-9ad4-fa7af5cb7270"
  );
  public static final UniqueIdentifier MAIN_EDGE_UUID_1 = UniversallyUniqueIdentifier.fromString(
      "46423600-5fd6-4299-bd60-4e8d9300c0de"
  );

  public Graph getFixturesGraph() {
    var exampleNodeId1 = new UniversallyUniqueIdentifier(MAIN_NODE_UUID_1.getId());
    var exampleNodeId2 = new UniversallyUniqueIdentifier(UUID.fromString("576a533b-5764-401b-876c-9df37461bf90"));
    var exampleNodeId3 = new UniversallyUniqueIdentifier(UUID.fromString("14f82bdd-6763-40a5-ac77-6e0acf60f103"));
    var exampleNodeId4 = new UniversallyUniqueIdentifier(UUID.fromString("27bdae4d-3555-4c96-9233-d252b7861413"));
    var exampleNodeId5 = new UniversallyUniqueIdentifier(UUID.fromString("9a0eb50c-c46b-4f20-9476-60dcaf161742"));
    var exampleNodeId6 = new UniversallyUniqueIdentifier(UUID.fromString("1d46826b-3eb8-4a87-9581-775df2ca74c3"));
    var exampleNodeId7 = new UniversallyUniqueIdentifier(UUID.fromString("833dd5a6-4add-418f-90cb-cff3803d4c36"));
    var exampleNodeId8 = new UniversallyUniqueIdentifier(UUID.fromString("68cd24fd-6527-4fdd-bc97-6ccc1db6d8f5"));
    var exampleNodeId9 = new UniversallyUniqueIdentifier(UUID.fromString("02331af7-d6a9-4b12-822a-67d71099efe8"));
    var exampleNodeId10 = new UniversallyUniqueIdentifier(UUID.fromString("db9ac0a8-db12-401f-93b5-e423006e5b55"));
    var exampleNodeId11 = new UniversallyUniqueIdentifier(UUID.fromString("4e0e2087-336e-4a1e-ac43-551629850c8f"));
    var exampleNodeId12 = new UniversallyUniqueIdentifier(UUID.fromString("39120d5a-8375-4bb6-968d-ed2a5cffe1d2"));
    var exampleNodeId13 = new UniversallyUniqueIdentifier(UUID.fromString("9eca788f-df8a-4a9a-8485-c90096b1a0b7"));
    var exampleNodeId14 = new UniversallyUniqueIdentifier(UUID.fromString("9aaec47e-81de-4dec-ba75-9da456c31339"));
    var exampleNodeId15 = new UniversallyUniqueIdentifier(UUID.fromString("4892db72-0184-4e82-9190-388a9d84c43a"));
    var exampleNodeId16 = new UniversallyUniqueIdentifier(UUID.fromString("3a0a2813-f79a-495f-91ea-f382a0f26446"));
    var exampleNodeId17 = new UniversallyUniqueIdentifier(UUID.fromString("17a49bcb-a3dc-4637-a88e-3d7b774f2ba8"));
    var exampleNodeId18 = new UniversallyUniqueIdentifier(UUID.fromString("a51be2d6-5242-4025-824c-9db6d03bc9c6"));
    var exampleNodeId19 = new UniversallyUniqueIdentifier(UUID.fromString("6f8cabb0-e469-4a91-b3c3-b10c76f69346"));
    var exampleEdgeId1 = new UniversallyUniqueIdentifier(MAIN_EDGE_UUID_1.getId());
    var exampleEdgeId2 = new UniversallyUniqueIdentifier(UUID.fromString("f8b8efe3-8920-48d5-8e15-d62aa5fdbee6"));
    var exampleEdgeId3 = new UniversallyUniqueIdentifier(UUID.fromString("7e333a4b-fdf0-4808-8165-b841fd957ddb"));
    var exampleEdgeId4 = new UniversallyUniqueIdentifier(UUID.fromString("7f049386-c160-4878-9603-32bf2c12a2f0"));
    var exampleEdgeId5 = new UniversallyUniqueIdentifier(UUID.fromString("6bd4367e-1d0f-4cbc-89d6-6b0024d6e05c"));
    var exampleEdgeId6 = new UniversallyUniqueIdentifier(UUID.fromString("71d19927-edff-4913-a7c7-6d420e7a787c"));
    var exampleEdgeId7 = new UniversallyUniqueIdentifier(UUID.fromString("927cb600-cdb0-48b5-9f16-859dda7d1bf5"));
    var exampleEdgeId8 = new UniversallyUniqueIdentifier(UUID.fromString("3f96289d-4a6c-4687-b4e6-80bbeb330bcf"));
    var exampleEdgeId9 = new UniversallyUniqueIdentifier(UUID.fromString("7d387e55-a84c-4c4f-92fd-0dc6557f7f0d"));
    var exampleEdgeId10 = new UniversallyUniqueIdentifier(UUID.fromString("28de8c55-b513-4b00-86b1-0b92553d19d4"));
    var exampleEdgeId11 = new UniversallyUniqueIdentifier(UUID.fromString("f0d8c1ef-359c-44bb-8b26-9069f8efb1cf"));

    return new Graph(
        this.createExampleNode(
            exampleNodeId1,
            NODE_TYPE_1,
            "Example Name 1",
            0,
            "Deep From First",
            "List Value 1", "List Value 2", "List Value 3"
        ),
        this.createExampleNode(
            exampleNodeId8,
            NODE_TYPE_2,
            "Example Name 8",
            35,
            "Deep From Middle"
        ),

        this.createExampleNode(
            exampleNodeId13,
            NODE_TYPE_3,
            "Example Name 13",
            60,
            "Deep From Last"
        ),
        this.createExampleNode(
            exampleNodeId19,
            NODE_TYPE_2_OTHER,
            "Example Name 19",
            35,
            "Shallow From Last Other"
        ),
        this.createExampleNode(
            exampleNodeId2,
            NODE_TYPE_1,
            "Example Name 2",
            5,
            "Deep From First",
            "List Value 4", "List Value 5", "List Value 6"
        ),
        this.createExampleNode(
            exampleNodeId9,
            NODE_TYPE_2,
            "Example Name 9",
            40,
            "Deep From Middle"
        ),
        this.createExampleNode(
            exampleNodeId14,
            NODE_TYPE_3,
            "Example Name 14",
            65,
            "Deep From Last"
        ),
        this.createExampleNode(
            exampleNodeId7,
            NODE_TYPE_1,
            "Example Name 7",
            30,
            "Deep To First",
            "List Value 7", "List Value 8", "List Value 9"
        ),
        this.createExampleNode(
            exampleNodeId10,
            NODE_TYPE_2,
            "Example Name 10",
            45,
            "Deep To Middle"
        ),
        this.createExampleNode(
            exampleNodeId15,
            NODE_TYPE_3,
            "Example Name 15",
            70,
            "Deep To Last"
        ),
        this.createExampleNode(
            exampleNodeId3,
            NODE_TYPE_1,
            "Example Name 3",
            10,
            "Shallow From First",
            "List Value 10", "List Value 11", "List Value 12"
        ),
        this.createExampleNode(
            exampleNodeId11,
            NODE_TYPE_2,
            "Example Name 11",
            50,
            "Shallow From Last"
        ),
        this.createExampleNode(
            exampleNodeId6,
            NODE_TYPE_1,
            "Example Name 6",
            25,
            "Shallow To First"
        ),
        this.createExampleNode(
            exampleNodeId12,
            NODE_TYPE_2,
            "Example Name 12",
            55,
            "Shallow To Last"
        ),
        this.createExampleNode(
            exampleNodeId16,
            NODE_TYPE_1,
            "Example Name 16",
            85,
            "Shallow From First V2"
        ),
        this.createExampleNode(
            exampleNodeId17,
            NODE_TYPE_2V2,
            "Example Name 17",
            90,
            "Shallow From Last V2"
        ),
        this.createExampleNode(
            exampleNodeId4,
            NODE_TYPE_1,
            "Example Name 4",
            15,
            "Shallow From First Other"
        ),
        this.createExampleNode(
            exampleNodeId18,
            NODE_TYPE_2_OTHER,
            "Example Name 18",
            85,
            "Shallow From Last Other"
        ),
        this.createExampleNode(
            exampleNodeId5,
            NODE_TYPE_1,
            "Example Name 5",
            20,
            "Without edge"
        ),
        this.createNewExampleEdgeCommand(
            new NodeIdAndType(exampleNodeId1, NODE_TYPE_1),
            new NodeIdAndType(exampleNodeId8, NODE_TYPE_2),
            exampleEdgeId1,
            EDGE_TYPE_1,
            "Example Edge Name 1"
        ),
        this.createNewExampleEdgeCommand(
            new NodeIdAndType(exampleNodeId8, NODE_TYPE_2),
            new NodeIdAndType(exampleNodeId13, NODE_TYPE_3),
            exampleEdgeId2,
            EDGE_TYPE_2,
            "Example Edge Name 2"
        ),
        this.createNewExampleEdgeCommand(
            new NodeIdAndType(exampleNodeId1, NODE_TYPE_1),
            new NodeIdAndType(exampleNodeId19, NODE_TYPE_2_OTHER),
            exampleEdgeId11,
            EDGE_TYPE_1_OTHER,
            "Example Edge Name 11"
        ),
        this.createNewExampleEdgeCommand(
            new NodeIdAndType(exampleNodeId2, NODE_TYPE_1),
            new NodeIdAndType(exampleNodeId9, NODE_TYPE_2),
            exampleEdgeId3,
            EDGE_TYPE_1,
            "Example Edge Name 3"
        ),
        this.createNewExampleEdgeCommand(
            new NodeIdAndType(exampleNodeId9, NODE_TYPE_2),
            new NodeIdAndType(exampleNodeId14, NODE_TYPE_3),
            exampleEdgeId4,
            EDGE_TYPE_2,
            "Example Edge Name 4"
        ),
        this.createNewExampleEdgeCommand(
            new NodeIdAndType(exampleNodeId10, NODE_TYPE_2),
            new NodeIdAndType(exampleNodeId7, NODE_TYPE_1),
            exampleEdgeId5,
            EDGE_TYPE_1,
            "Example Edge Name 5"
        ),
        this.createNewExampleEdgeCommand(
            new NodeIdAndType(exampleNodeId15, NODE_TYPE_3),
            new NodeIdAndType(exampleNodeId10, NODE_TYPE_2),
            exampleEdgeId6,
            EDGE_TYPE_2,
            "Example Edge Name 6"
        ),
        this.createNewExampleEdgeCommand(
            new NodeIdAndType(exampleNodeId3, NODE_TYPE_1),
            new NodeIdAndType(exampleNodeId11, NODE_TYPE_2),
            exampleEdgeId7,
            EDGE_TYPE_1,
            "Example Edge Name 7"
        ),
        this.createNewExampleEdgeCommand(
            new NodeIdAndType(exampleNodeId12, NODE_TYPE_2),
            new NodeIdAndType(exampleNodeId6, NODE_TYPE_1),
            exampleEdgeId8,
            EDGE_TYPE_1,
            "Example Edge Name 8"
        ),
        this.createNewExampleEdgeCommand(
            new NodeIdAndType(exampleNodeId16, NODE_TYPE_1),
            new NodeIdAndType(exampleNodeId17, NODE_TYPE_2V2),
            exampleEdgeId9,
            EDGE_TYPE_1,
            "Example Edge Name 9"
        ),
        this.createNewExampleEdgeCommand(
            new NodeIdAndType(exampleNodeId4, NODE_TYPE_1),
            new NodeIdAndType(exampleNodeId18, NODE_TYPE_2_OTHER),
            exampleEdgeId10,
            EDGE_TYPE_1_OTHER,
            "Example Edge Name 10"
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

  public Edge createNewExampleEdgeCommand(
      NodeIdAndType exampleNodeFromIdAndType,
      NodeIdAndType exampleNodeToIdAndType,
      UniversallyUniqueIdentifier exampleEdgeId,
      String exampleEdgeType,
      String exampleEdgeName,
      Integer exampleQuantity,
      String exampleStringAttribute
  ) {
    var shallowNodeFrom = new Node(
        exampleNodeFromIdAndType.getId(),
        exampleNodeFromIdAndType.getType()
    );
    var shallowNodeTo = new Node(
        exampleNodeToIdAndType.getId(),
        exampleNodeToIdAndType.getType()
    );
    return new Edge(
        exampleEdgeId,
        shallowNodeFrom,
        exampleEdgeType,
        shallowNodeTo,
        new LeafAttribute<>(AttributeTypes.EXAMPLE_NAME, new StringAttributeValue(exampleEdgeName)),
        new LeafAttribute<>(AttributeTypes.EXAMPLE_STRING_ATTRIBUTE, new StringAttributeValue(exampleStringAttribute)),
        new LeafAttribute<>(AttributeTypes.EXAMPLE_QUANTITY, new IntegerAttributeValue(exampleQuantity))
    );
  }

  public Edge createNewExampleEdgeCommand(
      NodeIdAndType exampleNodeFromIdAndType,
      NodeIdAndType exampleNodeToIdAndType,
      UniversallyUniqueIdentifier exampleEdgeId,
      String exampleEdgeType,
      String exampleEdgeName
  ) {
      return this.createNewExampleEdgeCommand(
          exampleNodeFromIdAndType,
          exampleNodeToIdAndType,
          exampleEdgeId,
          exampleEdgeType,
          exampleEdgeName,
          0,
          "default"
      );
  }
}
