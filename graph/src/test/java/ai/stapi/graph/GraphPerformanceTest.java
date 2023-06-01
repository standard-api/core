package ai.stapi.graph;

import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graph.test.base.UnitTestCase;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

class GraphPerformanceTest extends UnitTestCase {

  private StopWatch stopWatch = new StopWatch();

  @Test
  void itCanRenderGraphFast() {
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
    var addedNodes = new ArrayList<Node>();

    for (int i = 0; i < nodeCount; i++) {
      var newNode = new Node("node");
      graph = graph.with(newNode);
      addedNodes.add(newNode);

      for (Node addedNode : addedNodes) {
        graph = graph.with(new Edge(newNode, "connects", addedNode));
      }
    }
    return graph;
  }


}
