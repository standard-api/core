package ai.stapi.graph;

import ai.stapi.graph.test.base.UnitTestCase;
import ai.stapi.identity.UniqueIdentifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NodeAndTypeTest extends UnitTestCase {

  @Test
  void itCanCreateNodeAndTypeFromString() {
    var nodeIdAndType = NodeIdAndType.fromString(
        "arbitrary_type/arbitrary_id"
    );
    var expectedNodeIdAndType = new NodeIdAndType(
        new UniqueIdentifier("arbitrary_id"),
        "arbitrary_type"
    );

    Assertions.assertEquals("arbitrary_id", nodeIdAndType.getId().getId());
    Assertions.assertEquals("arbitrary_type", nodeIdAndType.getType());
    Assertions.assertEquals(expectedNodeIdAndType, nodeIdAndType);
    Assertions.assertEquals(expectedNodeIdAndType.hashCode(), nodeIdAndType.hashCode());
    Assertions.assertEquals(expectedNodeIdAndType.toString(), nodeIdAndType.toString());
  }


}
