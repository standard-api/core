package ai.stapi.graphoperations.graphWriter.specific;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.ConstantDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.ConstantDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal.RemovalEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal.RemovalEdgeDescriptionParameters;
import ai.stapi.graphoperations.graphWriter.SpecificGraphWriter;
import ai.stapi.graphoperations.graphWriter.exceptions.SpecificGraphWriterException;
import ai.stapi.graphoperations.graphbuilder.GraphBuilder;
import ai.stapi.identity.UniqueIdentifier;
import org.springframework.stereotype.Service;

@Service
public class RemovalEdgeGraphWriter implements SpecificGraphWriter {

  @Override
  public GraphBuilder write(
      GraphDescription graphDescription,
      GraphBuilder builder
  ) {
    var parameters = (RemovalEdgeDescriptionParameters) graphDescription.getParameters();
    var uuidDescription = graphDescription.getChildGraphDescriptions().stream()
        .filter(child -> child instanceof UuidIdentityDescription)
        .findAny()
        .orElseThrow(
            () -> SpecificGraphWriterException.becauseRemovalDescriptionDoesNotHaveUuidDescriptionAsChild(
                graphDescription));
    var constantDescription = uuidDescription.getChildGraphDescriptions().stream()
        .filter(child -> child instanceof ConstantDescription)
        .findAny()
        .orElseThrow(
            () -> SpecificGraphWriterException.becauseAttributeDescriptionDoesNotContainValue(
                uuidDescription));
    var constantParameters = (ConstantDescriptionParameters) constantDescription.getParameters();
    try {
      var uuid = new UniqueIdentifier(constantParameters.getValue().toString());
      builder.addEdgeForRemoval()
          .setType(parameters.getEdgeType())
          .setId(uuid);
      return builder;
    } catch (RuntimeException error) {
      throw SpecificGraphWriterException.becauseProvidedConstantIsNotUuid(
          constantParameters.getValue().toString());
    }
  }

  @Override
  public boolean supports(GraphDescription graphDescription) {
    return graphDescription instanceof RemovalEdgeDescription;
  }
}
