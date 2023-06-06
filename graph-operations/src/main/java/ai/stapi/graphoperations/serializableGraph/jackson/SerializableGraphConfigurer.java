package ai.stapi.graphoperations.serializableGraph.jackson;

import ai.stapi.graph.Graph;
import ai.stapi.graph.graphelements.Edge;
import ai.stapi.graph.graphelements.Node;
import ai.stapi.graphoperations.serializableGraph.SerializableEdge;
import ai.stapi.graphoperations.serializableGraph.SerializableGraph;
import ai.stapi.graphoperations.serializableGraph.SerializableNode;
import ai.stapi.graphoperations.serializableGraph.deserializer.SerializableGraphDeserializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;

public class SerializableGraphConfigurer {
  
  private final SerializableGraphDeserializer serializableGraphDeserializer;

  public SerializableGraphConfigurer(SerializableGraphDeserializer serializableGraphDeserializer) {
    this.serializableGraphDeserializer = serializableGraphDeserializer;
  }
  
  public void configure(ObjectMapper objectMapper) {
    var inMemoryGraphModule = new SimpleModule("InMemoryGraphModule");
    inMemoryGraphModule.addSerializer(
        Graph.class,
        new JsonSerializer<>() {

          @Override
          public void serialize(
              Graph value,
              JsonGenerator gen,
              SerializerProvider serializers
          ) throws IOException {
            gen.writeObject(SerializableGraph.fromInMemory(value));
          }
        }
    );
    inMemoryGraphModule.addDeserializer(
        Graph.class,
        new JsonDeserializer<>() {

          @Override
          public Graph deserialize(
              JsonParser p,
              DeserializationContext ctxt
          ) throws IOException {
            var serializableGraph = p.readValueAs(SerializableGraph.class);
            return serializableGraphDeserializer.deserialize(serializableGraph);
          }
        }
    );
    inMemoryGraphModule.addSerializer(
        Node.class,
        new JsonSerializer<>() {

          @Override
          public void serialize(
              Node value,
              JsonGenerator gen,
              SerializerProvider serializers
          ) throws IOException {
            gen.writeObject(SerializableNode.fromInputNode(value));
          }
        }
    );
    inMemoryGraphModule.addDeserializer(
        Node.class,
        new JsonDeserializer<>() {

          @Override
          public Node deserialize(
              JsonParser p,
              DeserializationContext ctxt
          ) throws IOException {
            var serializableNode = p.readValueAs(SerializableNode.class);
            return serializableGraphDeserializer.deserializeNode(serializableNode);
          }
        }
    );
    inMemoryGraphModule.addSerializer(
        Edge.class,
        new JsonSerializer<>() {

          @Override
          public void serialize(
              Edge value,
              JsonGenerator gen,
              SerializerProvider serializers
          ) throws IOException {
            gen.writeObject(SerializableEdge.fromInputEdge(value));
          }
        }
    );
    inMemoryGraphModule.addDeserializer(
        Edge.class,
        new JsonDeserializer<>() {

          @Override
          public Edge deserialize(
              JsonParser p,
              DeserializationContext ctxt
          ) throws IOException {
            var serializableEdge = p.readValueAs(SerializableEdge.class);
            return serializableGraphDeserializer.deserializeEdge(serializableEdge);
          }
        }
    );
    objectMapper.registerModule(inMemoryGraphModule);
  }
}
