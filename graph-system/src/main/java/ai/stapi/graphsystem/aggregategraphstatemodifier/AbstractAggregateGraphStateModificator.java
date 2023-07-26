package ai.stapi.graphsystem.aggregategraphstatemodifier;

import ai.stapi.graph.traversableGraphElements.TraversableNode;
import ai.stapi.graphoperations.graphLanguage.graphDescription.graphDescriptionBuilder.GraphDescriptionBuilder;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectGraphMapping;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.GenericOGMBuilder;
import ai.stapi.graphoperations.objectGraphLanguage.objectGraphMappingBuilder.specific.ogm.ObjectGraphMappingBuilder;
import ai.stapi.graphoperations.objectGraphMapper.model.GenericObjectGraphMapper;
import ai.stapi.graphoperations.ogmProviders.specific.dynamicObjectGraphMappingProvider.DynamicOgmProvider;
import ai.stapi.schema.structureSchema.ComplexStructureType;
import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractAggregateGraphStateModificator implements AggregateGraphStateModificator {

  protected final StructureSchemaFinder structureSchemaFinder;
  protected final DynamicOgmProvider dynamicOgmProvider;
  protected final GenericObjectGraphMapper objectGraphMapper;
  protected final EventFactoryModificationTraverser eventFactoryModificationTraverser;

  public AbstractAggregateGraphStateModificator(
      StructureSchemaFinder structureSchemaFinder,
      DynamicOgmProvider dynamicOgmProvider,
      GenericObjectGraphMapper objectGraphMapper,
      EventFactoryModificationTraverser eventFactoryModificationTraverser
  ) {
    this.structureSchemaFinder = structureSchemaFinder;
    this.dynamicOgmProvider = dynamicOgmProvider;
    this.objectGraphMapper = objectGraphMapper;
    this.eventFactoryModificationTraverser = eventFactoryModificationTraverser;
  }

  @NotNull
  protected Map<String, Object> getMappedObject(
      Object inputValue,
      TraversableNode modifiedNode,
      String fieldName
  ) {
    return new HashMap<>(Map.of(
        "id", modifiedNode.getId().getId(),
        fieldName, inputValue
    ));
  }

  @NotNull
  protected ObjectGraphMapping getOgm(
      TraversableNode modifiedNode,
      FieldDefinition inputValueSchema,
      String fieldName
  ) {
    var objectOgm = new ObjectGraphMappingBuilder().setGraphDescription(
        new GraphDescriptionBuilder().addNodeDescription(modifiedNode.getType())
    );

    objectOgm
        .addField("id")
        .addLeafAsObjectFieldMapping()
        .setGraphDescription(new UuidIdentityDescription());


    var dataField = objectOgm.addField(fieldName);

    var edge = new GraphDescriptionBuilder().addOutgoingEdge(fieldName);
    if (inputValueSchema.isUnionType()) {
      if (inputValueSchema.isList()) {
        dataField
            .addListAsObjectFieldMapping()
            .addInterfaceChildDefinition()
            .setGraphDescription(edge);
      } else {
        dataField
            .setRelation(edge)
            .addInterfaceAsObjectFieldMapping();
      }
    } else {
      var type = inputValueSchema.getTypes().get(0).getType();
      var inputValueType = this.structureSchemaFinder.getStructureType(type);
      if (inputValueType instanceof ComplexStructureType) {
        var inputOgm = this.dynamicOgmProvider.provideGraphMapping(type);
        var inputOgmBuilder = new GenericOGMBuilder()
            .copyGraphMappingAsBuilder(inputOgm);
        if (inputValueSchema.isList()) {
          dataField
              .addListAsObjectFieldMapping()
              .setGraphDescription(edge)
              .setChildDefinition(inputOgmBuilder);
        } else {
          dataField
              .setRelation(edge)
              .setOgmBuilder(inputOgmBuilder);
        }
      } else {
        var primitiveOgm = this.dynamicOgmProvider.provideOgmForPrimitive(
            type,
            new FieldDefinition(
                fieldName,
                inputValueSchema.getMin(),
                inputValueSchema.getMax(),
                inputValueSchema.getDescription(),
                inputValueSchema.getTypes(),
                inputValueSchema.getParentDefinitionType()
            )
        );
        var primitiveOgmBuilder = new GenericOGMBuilder().copyGraphMappingAsBuilder(primitiveOgm);
        dataField.setOgmBuilder(primitiveOgmBuilder);
      }
    }
    return objectOgm.build();
  }
}
