package ai.stapi.graphoperations.graphWriter;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.removal.RemovalGraphDescription;
import ai.stapi.graphoperations.graphWriter.exceptions.GenericGraphWriterException;
import ai.stapi.graphoperations.graphbuilder.GraphBuilder;
import ai.stapi.graph.attribute.attributeFactory.GenericAttributeFactory;
import ai.stapi.graph.graphElementForRemoval.GraphElementForRemoval;
import ai.stapi.graph.Graph;
import ai.stapi.identity.UniqueIdentifier;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class GenericGraphWriter {

  private final List<SpecificGraphWriter> specificGraphWriters;
  private final GenericAttributeFactory genericAttributeFactory;

  public GenericGraphWriter(
      List<SpecificGraphWriter> specificGraphWriters,
      GenericAttributeFactory genericAttributeFactory
  ) {
    this.specificGraphWriters = specificGraphWriters;
    this.genericAttributeFactory = genericAttributeFactory;
  }

  public Graph createGraph(
      UniqueIdentifier firstElementId,
      PositiveGraphDescription graphDescription
  ) {
    if (!(graphDescription instanceof NodeDescription)) {
      throw GenericGraphWriterException.becauseFirstDescriptionHasToBeNode(graphDescription);
    }
    this.ensureGraphDescriptionContainsOnlyPositiveDescriptions(graphDescription);
    var builder = this.resolvePositiveDescription(graphDescription, new GraphBuilder());
    builder.getFirstGraphElement().setId(firstElementId);
    builder.dropIncompleteEdges();
    return builder.build(this.genericAttributeFactory);
  }

  public Graph createGraph(PositiveGraphDescription graphDescription) {
    if (!(graphDescription instanceof NodeDescription)) {
      throw GenericGraphWriterException.becauseFirstDescriptionHasToBeNode(graphDescription);
    }
    this.ensureGraphDescriptionContainsOnlyPositiveDescriptions(graphDescription);
    var builder = this.resolvePositiveDescription(graphDescription, new GraphBuilder());
    builder.dropIncompleteEdges();
    return builder.build(this.genericAttributeFactory);
  }

  public List<GraphElementForRemoval> createElementsForRemoval(
      List<RemovalGraphDescription> removalGraphDescriptions) {
    var builder = new GraphBuilder();
    removalGraphDescriptions.forEach(
        description -> {
          var resolver = this.getSupportingGraphWriter(description);
          resolver.write(description, builder);
        }
    );
    return builder.buildElementsForRemoval();
  }

  private GraphBuilder resolvePositiveDescription(
      PositiveGraphDescription graphDescription,
      GraphBuilder graphBuilder
  ) {
    var supportingWriter = this.getSupportingGraphWriter(graphDescription);
    supportingWriter.write(graphDescription, graphBuilder);
    graphDescription.getChildGraphDescriptions().forEach(
        child -> {
          var newBranch = graphBuilder.createNewBranch();
          this.resolvePositiveDescription((PositiveGraphDescription) child, newBranch);
        }
    );
    return graphBuilder;
  }

  private GraphBuilder resolveRemovalDescription(
      GraphDescription graphDescription,
      GraphBuilder graphBuilder
  ) {
    var supportingWriter = this.getSupportingGraphWriter(graphDescription);
    supportingWriter.write(graphDescription, graphBuilder);
    graphDescription.getChildGraphDescriptions().forEach(
        child -> {
          var newBranch = graphBuilder.createNewBranch();
          this.resolveRemovalDescription(child, newBranch);
        }
    );
    return graphBuilder;
  }

  private void ensureGraphDescriptionContainsOnlyPositiveDescriptions(
      PositiveGraphDescription graphDescription) {
    var removalDescription = GraphDescriptionBuilder.getGraphDescriptionAsStream(graphDescription)
        .filter(description -> description instanceof RemovalGraphDescription)
        .findAny();
    removalDescription.ifPresent(
        description -> {
          throw GenericGraphWriterException.becauseGraphDescriptionContainsRemovalGraphDescription(
              description);
        }
    );
  }

  @NotNull
  private SpecificGraphWriter getSupportingGraphWriter(GraphDescription graphDescription) {
    var supportingGraphWriters = this.specificGraphWriters.stream()
        .filter(specificGraphWriter -> specificGraphWriter.supports(graphDescription));

    var listOfSupportingResolvers = supportingGraphWriters.toList();
    if (listOfSupportingResolvers.size() == 0) {
      throw GenericGraphWriterException.becauseNoSupportingSpecificResolverForGivenDeclaration(
          graphDescription);
    }
    if (listOfSupportingResolvers.size() > 1) {
      throw GenericGraphWriterException.becauseMoreThanOneSpecificResolverForGivenDeclaration(
          graphDescription,
          listOfSupportingResolvers
      );
    }
    return listOfSupportingResolvers.get(0);
  }
}
