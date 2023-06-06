package ai.stapi.graphoperations.graphWriter.specific;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.IngoingEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.OutgoingEdgeDescription;
import ai.stapi.graphoperations.graphWriter.SpecificGraphWriter;
import ai.stapi.graphoperations.graphbuilder.GraphBuilder;
import ai.stapi.graphoperations.graphWriter.exceptions.SpecificGraphWriterException;
import ai.stapi.graphoperations.graphbuilder.specific.positive.EdgeDirection;
import ai.stapi.identity.UniversallyUniqueIdentifier;

public class EdgeGraphWriter implements SpecificGraphWriter {

  @Override
  public GraphBuilder write(
      GraphDescription graphDescription,
      GraphBuilder builder
  ) {
    EdgeDirection direction;
    if (graphDescription instanceof IngoingEdgeDescription) {
      direction = EdgeDirection.INGOING;
    } else if (graphDescription instanceof OutgoingEdgeDescription) {
      direction = EdgeDirection.OUTGOING;
    } else {
      throw SpecificGraphWriterException.becauseGivenEdgeDirectionIsNotSupported(graphDescription);
    }
    var parameters = (EdgeDescriptionParameters) graphDescription.getParameters();
    builder.addEdge().setEdgeDirection(direction).setId(UniversallyUniqueIdentifier.randomUUID())
        .setType(parameters.getEdgeType());
    return builder;
  }

  @Override
  public boolean supports(GraphDescription graphDescription) {
    return graphDescription instanceof IngoingEdgeDescription
        || graphDescription instanceof OutgoingEdgeDescription;
  }
}
