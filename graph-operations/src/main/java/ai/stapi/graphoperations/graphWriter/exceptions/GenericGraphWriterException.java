package ai.stapi.graphoperations.graphWriter.exceptions;

import ai.stapi.graphoperations.declaration.Declaration;
import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal.RemovalGraphDescription;
import ai.stapi.graphoperations.graphWriter.GenericGraphWriter;
import ai.stapi.graphoperations.graphWriter.SpecificGraphWriter;
import ai.stapi.utils.LineFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class GenericGraphWriterException extends RuntimeException {

  public GenericGraphWriterException(String format) {
    super(format);
  }

  public static GenericGraphWriterException becauseNoSupportingSpecificResolverForGivenDeclaration(
      Declaration declaration) {
    return new GenericGraphWriterException(
        String.format(
            "There is no supporting %s for declaration '%s'.",
            SpecificGraphWriter.class.getSimpleName(),
            declaration.getClass().getSimpleName()
        )
    );
  }

  public static GenericGraphWriterException becauseMoreThanOneSpecificResolverForGivenDeclaration(
      Declaration declaration,
      List<SpecificGraphWriter> supportingSpecificGraphWriters
  ) {
    return new GenericGraphWriterException(
        String.format(
            "There is more than one supporting %s for declaration '%s'."
                + LineFormatter.createNewLine() +
                "Supporting: " + LineFormatter.createNewLine() +
                "'%s'",
            SpecificGraphWriter.class.getSimpleName(),
            declaration.getClass().getSimpleName(),
            supportingSpecificGraphWriters.stream()
                .map(supportingSpecificGraphWriter -> supportingSpecificGraphWriter.getClass()
                    .getSimpleName())
                .collect(Collectors.joining("'" + LineFormatter.createNewLine() + "'"))
        )
    );
  }

  public static GenericGraphWriterException becauseFirstDescriptionHasToBeNode(
      PositiveGraphDescription graphDescription) {
    return new GenericGraphWriterException(
        "First graph description must be of type '"
            + NodeDescription.class.getSimpleName()
            + "' but was of type '"
            + graphDescription.getClass().getSimpleName()
            + "'."
    );
  }

  public static GenericGraphWriterException becauseGraphDescriptionContainsRemovalGraphDescription(
      GraphDescription description) {
    return new GenericGraphWriterException(
        "Provided "
            + GraphDescription.class.getSimpleName()
            + " contains " + RemovalGraphDescription.class.getSimpleName()
            + " of type '" + description.getClass().getSimpleName()
            + "' and that is not allowed in " + GenericGraphWriter.class.getSimpleName()
            + "."
    );
  }
}
