package ai.stapi.graphoperations.graphToMapObjectMapper;

import ai.stapi.graph.Graph;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graphoperations.fixtures.testsystem.TestSystemModelDefinitionsLoader;
import ai.stapi.graphoperations.graphToMapObjectMapper.exception.GraphToMapObjectMapperException;
import ai.stapi.graphoperations.objectGraphMapper.model.GenericObjectGraphMapper;
import ai.stapi.graphoperations.objectGraphMapper.model.MissingFieldResolvingStrategy;
import ai.stapi.graphoperations.ogmProviders.GenericGraphMappingProvider;
import ai.stapi.test.integration.IntegrationTestCase;
import ai.stapi.test.schemaintegration.SchemaIntegrationTestCase;
import ai.stapi.test.schemaintegration.StructureDefinitionScope;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

@StructureDefinitionScope(TestSystemModelDefinitionsLoader.SCOPE)
class GraphToMapObjectMapperTest extends SchemaIntegrationTestCase {

  public static final String EXAMPLE_IDENTIFIER = "exampleIdentifier.json";
  public static final String EXAMPLE_STRUCTURE_DEFINITION = "exampleStructureDefinition.json";

  @Autowired
  private GenericObjectGraphMapper genericObjectGraphMapper;
  
  @Autowired
  private GenericGraphMappingProvider genericGraphMappingProvider;

  @Autowired
  private GraphToMapObjectMapper graphToMapObjectMapper;

  @Test
  void itThrowsWhenSerializationTypeDoesNotExist() {

    var graph = new Graph()
        .with(new Node("not-serializable"))
        .traversable();
    Executable throwable = () -> graphToMapObjectMapper
        .map(
            graph.loadAllNodes("not-serializable").get(0),
            graph.getGraph()
        );
    this.thenExceptionMessageApproved(
        GraphToMapObjectMapperException.class,
        throwable
    );
  }

  @Test
  void itThrowsWhenGraphIsEmpty() {
    var graph = new Graph()
        .with(new Node("Identifier"))
        .traversable();
    Executable throwable = () -> graphToMapObjectMapper
        .map(
            graph.loadAllNodes("Identifier").get(0),
            new Graph()
        );
    this.thenExceptionMessageApproved(
        GraphToMapObjectMapperException.class,
        throwable
    );
  }

  @Test
  void itCanMapIdentifierToHashMap() throws IOException {
    var graph = this.createGraphFromExampleStructure(
        EXAMPLE_IDENTIFIER,
        "Identifier"
    ).traversable();
    var result = graphToMapObjectMapper.map(
        graph.loadAllNodes("Identifier").get(0),
        graph.getGraph()
    );
    this.removeStart(result);
    this.thenObjectApproved(result);
  }

  @Test
  void itCanMapStructureDefinitionToHashMap() throws IOException {
    var graph = this.createGraphFromExampleStructure(
        EXAMPLE_STRUCTURE_DEFINITION,
        "StructureDefinition"
    ).traversable();
    var result = graphToMapObjectMapper.map(
        graph.loadAllNodes("StructureDefinition").get(0),
        graph.getGraph()
    );
    var sortedResults = this.sortResults(result);
    this.thenObjectApproved(sortedResults);
  }

  private void removeStart(Map<String, Object> result) {
    //this is because renderer serialization of TimeStamp object is not ?deterministic
    var periodMap = (Map<String, Object>) result.get("period");
    periodMap.remove("start");
    periodMap.remove("end");
  }

  private Map<String, Object> sortResults(Map<String, Object> result) {
    var differential = (Map<String, Object>) result.get("differential");
    var elements = new ArrayList<>((List<Map<String, Object>>) differential.get("element"));
    elements.sort((e1, e2) -> e1.get("path").toString().compareTo(e2.get("path").toString()));
    differential.put("element", elements);
    return result;
  }

  private Graph createGraphFromExampleStructure(
      String exampleFileName,
      String serializationType
  ) throws IOException {
    var fixtureFilePath = this.getFixtureFilePath(exampleFileName);
    var file = new File(fixtureFilePath);
    var data = FileUtils.readFileToString(file, "UTF-8");
    try {
      var object = new ObjectMapper().readValue(data, Object.class);
      var ogm = genericGraphMappingProvider.provideGraphMapping(serializationType);
      return genericObjectGraphMapper.mapToGraph(
          ogm,
          object,
          MissingFieldResolvingStrategy.LENIENT
      ).getGraph();
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

}