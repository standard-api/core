package ai.stapi.graphoperations.objectGraphMapper.model.exceptions;

import ai.stapi.graphoperations.objectGraphMapper.model.GenericObjectGraphMapper;
import ai.stapi.graphoperations.objectGraphMapper.model.SpecificObjectGraphMapper;
import ai.stapi.graphoperations.declaration.Declaration;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectObjectGraphMapping;
import ai.stapi.utils.LineFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectGraphMapperException extends RuntimeException {

  public ObjectGraphMapperException(String format) {
    super(format);
  }

  public static ObjectGraphMapperException becausegivenObjectIsNull(
      ObjectGraphMapping objectGraphMapping
  ) {
    return new ObjectGraphMapperException(
        String.format(
            "Given object to be mapped to graph should not be null. But it is."
                + LineFormatter.createNewLine() +
                "Given Graph Object Mapping:" + LineFormatter.createNewLine() +
                "\"%s\"",
            objectGraphMapping.getClass().getSimpleName()
        )
    );
  }

  public static ObjectGraphMapperException becauseNoSupportingSpecificResolverForGivenDeclaration(
      Declaration declaration) {
    return new ObjectGraphMapperException(
        String.format(
            "There is no supporting \"%s\" for declaration \"%s\".",
            GenericObjectGraphMapper.class.getSimpleName(),
            declaration.getClass().getSimpleName()
        )
    );
  }

  public static ObjectGraphMapperException becauseMoreThanOneSpecificResolverForGivenDeclaration(
      Declaration declaration,
      List<SpecificObjectGraphMapper> supportingSpecificGraphWriters
  ) {
    return new ObjectGraphMapperException(
        String.format(
            "There is more than one supporting \"%s\" " + LineFormatter.createNewLine() +
                "for declaration \"%s\". " + LineFormatter.createNewLine() +
                "Supporting: " + LineFormatter.createNewLine() +
                "\"%s\"",
            GenericObjectGraphMapper.class.getSimpleName(),
            declaration.getClass().getSimpleName(),
            supportingSpecificGraphWriters.stream()
                .map(supportingSpecificGraphWriter -> supportingSpecificGraphWriter.getClass()
                    .getSimpleName())
                .collect(Collectors.joining(LineFormatter.createNewLine()))
        )
    );
  }

  public static ObjectGraphMapperException becauseFirstObjectGraphMappingIsNotObject(
      ObjectGraphMapping objectGraphMapping) {
    return new ObjectGraphMapperException(
        String.format(
            "First Object Graph Mapping should be of type \"%s\" " + LineFormatter.createNewLine() +
                "but is \"%s\". " + LineFormatter.createNewLine(),
            ObjectObjectGraphMapping.class.getSimpleName(),
            objectGraphMapping == null
                ? "null"
                : objectGraphMapping.getClass().getSimpleName()
        )
    );
  }

  public static ObjectGraphMapperException becauseResolverCanNotResolveEntityIdentifier(
      SpecificObjectGraphMapper supportingResolver) {
    return new ObjectGraphMapperException(
        "Specific Object Graph Mapper of type '"
            + supportingResolver.getClass().getSimpleName()
            + "' can not resolve Entity Identifier."
    );
  }
}
