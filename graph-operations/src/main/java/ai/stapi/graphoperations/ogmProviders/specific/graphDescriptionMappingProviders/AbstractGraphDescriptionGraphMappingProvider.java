package ai.stapi.graphoperations.ogmProviders.specific.graphDescriptionMappingProviders;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.SpecificObjectGraphMappingBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import ai.stapi.graphoperations.ogmProviders.specific.SpecificGraphMappingProvider;

public abstract class AbstractGraphDescriptionGraphMappingProvider
    implements SpecificGraphMappingProvider {

  @Override
  public ObjectGraphMapping provideGraphMapping(
      String serializationType,
      String fieldName
  ) {
    var definition = new ObjectGraphMappingBuilder();
    definition.setGraphDescription(
        new GraphDescriptionBuilder().addNodeDescription(this.getGraphDescriptionNodeType())
    );
    definition.addField("parameters")
        .setChild(this.getParametersDefinition());
    definition.addField("childGraphDescriptions")
        .setRelation(new GraphDescriptionBuilder().addOutgoingEdge("has_child_graph_mapping"))
        .addListAsObjectFieldMapping()
        .addInterfaceChildDefinition()
        .setUuid(GraphDescription.INTERFACE_UUID);
    return definition.build();
  }

  private SpecificObjectGraphMappingBuilder getParametersDefinition() {
    var builder = new ObjectGraphMappingBuilder();
    this.setParametersFields(builder);
    return builder;
  }

  protected abstract String getGraphDescriptionNodeType();

  protected abstract void setParametersFields(ObjectGraphMappingBuilder parameters);

  @Override
  public abstract boolean supports(String serializationType);
}
