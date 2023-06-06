package ai.stapi.graphoperations.ogmProviders.specific.dynamicObjectGraphMappingProvider;

import ai.stapi.graphoperations.fixtures.testsystem.TestSystemModelDefinitionsLoader;
import ai.stapi.graphoperations.objectGraphLanguage.ObjectObjectGraphMapping;
import ai.stapi.graphoperations.ogmProviders.specific.dynamicObjectGraphMappingProvider.model.DynamicOgmProviderTestLoader;
import ai.stapi.schema.structureSchema.FieldDefinition;
import ai.stapi.schema.structureSchema.FieldType;
import ai.stapi.test.schemaintegration.SchemaIntegrationTestCase;
import ai.stapi.test.schemaintegration.StructureDefinitionScope;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

@StructureDefinitionScope({
    DynamicOgmProviderTestLoader.SCOPE,
    TestSystemModelDefinitionsLoader.SCOPE
})
class DynamicOgmProviderTest extends SchemaIntegrationTestCase {

  @Autowired
  private DynamicOgmProvider dynamicOgmProvider;

  @Test
  void itThrowsWhenTryingToGenerateOGMForNonExistingType() {
    Executable throwable = () -> this.dynamicOgmProvider.provideGraphMapping(
        "non-existing-serialization-type"
    );
    this.thenExceptionMessageApproved(RuntimeException.class, throwable);
  }

  @Test
  void itThrowsWhenTryingToGenerateOGMForPrimitiveType() {
    Executable throwable = () -> this.dynamicOgmProvider.provideGraphMapping("string");
    this.thenExceptionMessageApproved(RuntimeException.class, throwable);
  }

  @Test
  void itCanGenerateOgmForBoxedString() {
    var actual = this.dynamicOgmProvider.provideOgmForPrimitive(
        "BoxedString",
        new FieldDefinition(
            "exampleAttribute",
            0,
            "1",
            "",
            List.of(FieldType.asPlainType("string")),
            "Irrelevant"
        )
    );
    this.thenObjectApproved(actual);
  }

  @Test
  void itCanGenerateOgmForPrimitiveType() {
    var actual = this.dynamicOgmProvider.provideOgmForPrimitive(
        "decimal",
        new FieldDefinition(
            "exampleDecimalAttribute",
            0,
            "1",
            "",
            List.of(FieldType.asPlainType("decimal")),
            "Irrelevant"
        )
    );
    this.thenObjectApproved(actual);
  }

  @Test
  void itCanGenerateOGMForElementDefinition() {
    var result = this.dynamicOgmProvider.provideGraphMapping("ElementDefinition");
    this.thenObjectApproved(result);
  }

  @Test
  void itCanGenerateStructureDefinition() {
    var result = this.dynamicOgmProvider.provideGraphMapping("StructureDefinition");
    this.thenObjectApproved(result);
  }

  @Test
  void itCanGenerateOgmForLeafField() {
    var result =
        (ObjectObjectGraphMapping) this.dynamicOgmProvider.provideGraphMapping("Parameters");
    this.thenObjectApproved(result.getFields().get("id"));
  }

  @Test
  void itCanGenerateReferenceOgmForFieldWithContentReference() {
    var result = (ObjectObjectGraphMapping) this.dynamicOgmProvider.provideGraphMapping(
        "PlanDefinitionAction"
    );
    this.thenObjectApproved(result.getFields().get("action"));
  }

  @Test
  void itCanGenerateInterfaceOgmForFieldWithUnionType() {
    var result = (ObjectObjectGraphMapping) this.dynamicOgmProvider.provideGraphMapping(
        "Extension"
    );
    this.thenObjectApproved(result.getFields().get("value"));
  }
}