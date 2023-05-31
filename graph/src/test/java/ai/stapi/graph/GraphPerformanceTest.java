package ai.stapi.graph;

import ai.stapi.graph.Graph;
import ai.stapi.graph.inputGraphElements.InputEdge;
import ai.stapi.graph.inputGraphElements.InputNode;
import ai.stapi.graph.test.base.UnitTestCase;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

public class GraphPerformanceTest extends UnitTestCase {

  private StopWatch stopWatch = new StopWatch();

  @Test
  public void itCanRenderGraphFast() {
    var maximumSeconds = 15;
    var graph = this.getGraphWithMaximumEdges(10);

    stopWatch.start();
    var result = this.graphRenderer.render(graph).toPrintableString();
    stopWatch.stop();

    Assertions.assertTrue(!result.isEmpty());
    Assertions.assertTrue(stopWatch.getLastTaskTimeMillis() < 1000 * maximumSeconds);
  }

  private Graph getGraphWithMaximumEdges(int nodeCount) {
    var graph = new Graph();
    var addedNodes = new ArrayList<InputNode>();

    for (int i = 0; i < nodeCount; i++) {
      var newNode = new InputNode("node");
      graph = graph.withNode(newNode);
      addedNodes.add(newNode);

      for (InputNode addedNode : addedNodes) {
        graph = graph.withEdge(new InputEdge(newNode, "connects", addedNode));
      }
    }
    return graph;
  }


}
