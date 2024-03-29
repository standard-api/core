package ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.*;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.exception.GenericOGMBuilderException;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.SpecificObjectGraphMappingBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.InterfaceGraphMappingBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.LeafGraphMappingBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ListGraphMappingBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.MapGraphMappingBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ReferenceGraphMappingBuilder;
import java.util.ArrayList;
import java.util.List;

public class GenericOGMBuilder {

  private final List<SpecificObjectGraphMappingBuilder> supportingBuilders = new ArrayList<>();

  public GenericOGMBuilder() {
    this.initializeBuilders();
  }

  private void initializeBuilders() {
    this.supportingBuilders.add(new ObjectGraphMappingBuilder());
    this.supportingBuilders.add(new ListGraphMappingBuilder());
    this.supportingBuilders.add(new MapGraphMappingBuilder());
    this.supportingBuilders.add(new LeafGraphMappingBuilder());
    this.supportingBuilders.add(new InterfaceGraphMappingBuilder());
    this.supportingBuilders.add(new ReferenceGraphMappingBuilder());
  }
  
  public static GraphDescription getCompositeGraphDescriptionFromOgm(ObjectGraphMapping objectGraphMapping) {
    var newChildren = new ArrayList<GraphDescription>();
    if (objectGraphMapping instanceof ListObjectGraphMapping list) {
      newChildren.add(GenericOGMBuilder.getCompositeGraphDescriptionFromOgm(list.getChildObjectGraphMapping()));
    } else if (objectGraphMapping instanceof MapObjectGraphMapping map) {
      newChildren.add(GenericOGMBuilder.getCompositeGraphDescriptionFromOgm(map.getValueObjectGraphMapping()));
    } else if (objectGraphMapping instanceof ObjectObjectGraphMapping object) {
      object.getFields().values()
          .stream()
          .map(field -> {
            var newChild = GenericOGMBuilder.getCompositeGraphDescriptionFromOgm(field.getFieldObjectGraphMapping());
            if (field.getRelation() instanceof GraphDescription relationDescription) {
                return new GraphDescriptionBuilder().addToDeepestDescription(
                    relationDescription,
                    List.of(newChild)
                );
            }
            return newChild;
          }).forEach(newChildren::add);
    }
    return new GraphDescriptionBuilder().addToDeepestDescription(
        objectGraphMapping.getGraphDescription(),
        newChildren
    );
  }

  public ObjectGraphMappingBuilder createNewObjectGraphMapping() {
    return new ObjectGraphMappingBuilder();
  }

  public SpecificObjectGraphMappingBuilder copyGraphMappingAsBuilder(
      ObjectGraphMapping objectGraphMapping
  ) {
    var builder = this.getSupportingBuilder(objectGraphMapping);
    return builder.copyObjectGraphMappingAsBuilder(objectGraphMapping, this);
  }

  public SpecificObjectGraphMappingBuilder getSupportingBuilder(
      ObjectGraphMapping objectGraphMapping) {
    var supportingBuilder = this.supportingBuilders.stream()
        .filter(builder -> builder.supports(objectGraphMapping))
        .findAny()
        .orElseThrow(
            () -> GenericOGMBuilderException.becauseThereIsNoSupportingBuilder(objectGraphMapping));
    return supportingBuilder.getEmptyCopy();
  }
}
