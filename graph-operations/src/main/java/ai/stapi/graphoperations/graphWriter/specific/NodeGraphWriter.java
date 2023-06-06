package ai.stapi.graphoperations.graphWriter.specific;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphWriter.SpecificGraphWriter;
import ai.stapi.graphoperations.graphbuilder.GraphBuilder;
import ai.stapi.identity.UniversallyUniqueIdentifier;
import org.springframework.stereotype.Service;

@Service
public class NodeGraphWriter implements SpecificGraphWriter {

  @Override
  public GraphBuilder write(
      GraphDescription graphDescription,
      GraphBuilder builder
  ) {
    var parameters = (NodeDescriptionParameters) graphDescription.getParameters();
    builder.addNode()
        .setId(UniversallyUniqueIdentifier.randomUUID())
        .setType(parameters.getNodeType());
    return builder;
  }

  @Override
  public boolean supports(GraphDescription graphDescription) {
    return graphDescription instanceof NodeDescription;
  }
}
