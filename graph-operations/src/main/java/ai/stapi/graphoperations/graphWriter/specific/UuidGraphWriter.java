package ai.stapi.graphoperations.graphWriter.specific;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.ConstantDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.ConstantDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.graphWriter.SpecificGraphWriter;
import ai.stapi.graphoperations.graphWriter.exceptions.SpecificGraphWriterException;
import ai.stapi.graphoperations.graphbuilder.GraphBuilder;
import ai.stapi.identity.UniqueIdentifier;

public class UuidGraphWriter implements SpecificGraphWriter {

  @Override
  public GraphBuilder write(
      GraphDescription graphDescription,
      GraphBuilder builder
  ) {
    var constant = graphDescription.getChildGraphDescriptions().stream()
        .filter(child -> child instanceof ConstantDescription)
        .findAny()
        .orElseThrow(
            () -> SpecificGraphWriterException.becauseAttributeDescriptionDoesNotContainValue(
                graphDescription));
    var constantParameters = (ConstantDescriptionParameters) constant.getParameters();
    try {
      var uuid = new UniqueIdentifier(constantParameters.getValue().toString());
      builder.setIdToLastElement(uuid);
      return builder;
    } catch (RuntimeException error) {
      throw SpecificGraphWriterException.becauseProvidedConstantIsNotUuid(
          constantParameters.getValue().toString());
    }
  }

  @Override
  public boolean supports(GraphDescription graphDescription) {
    return graphDescription instanceof UuidIdentityDescription;
  }
}
