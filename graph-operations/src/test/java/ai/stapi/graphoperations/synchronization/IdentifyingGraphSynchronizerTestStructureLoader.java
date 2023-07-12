package ai.stapi.graphoperations.synchronization;

import ai.stapi.schema.adHocLoaders.AbstractJavaModelDefinitionsLoader;
import ai.stapi.schema.scopeProvider.ScopeOptions;
import ai.stapi.schema.structuredefinition.ElementDefinition;
import ai.stapi.schema.structuredefinition.ElementDefinitionType;
import ai.stapi.schema.structuredefinition.StructureDefinitionData;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class IdentifyingGraphSynchronizerTestStructureLoader
    extends AbstractJavaModelDefinitionsLoader<StructureDefinitionData> {

  public static final String SCOPE = "IdentifyingGraphSynchronizerTest";
  public static final String TAG = ScopeOptions.TEST_TAG;

  protected IdentifyingGraphSynchronizerTestStructureLoader() {
    super(SCOPE, TAG, StructureDefinitionData.SERIALIZATION_TYPE);
  }

  @Override
  public List<StructureDefinitionData> load() {
    return List.of(
        new StructureDefinitionData(
            "Test_node_type",
            "http://example.com/fhir/StructureDefinition/test_node_type",
            "active",
            "Represents a node in a test tree",
            "complex-type",
            false,
            "TestNodeType",
            null,
            null,
            new StructureDefinitionData.Differential(
                List.of(
                    new ElementDefinition(
                        "Test_node_type.string_type",
                        List.of(new ElementDefinitionType("string", null)),
                        1,
                        "1",
                        "A string attribute of the test node",
                        null,
                        null
                    ),
                    new ElementDefinition(
                        "Test_node_type.identifying_attribute",
                        List.of(new ElementDefinitionType("string", null)),
                        1,
                        "1",
                        "An identifying attribute of the test node",
                        null,
                        null
                    ),
                    new ElementDefinition(
                        "Test_node_type.not_identifying_attribute",
                        List.of(new ElementDefinitionType("string", null)),
                        0,
                        "1",
                        "A non-identifying attribute of the test node",
                        null,
                        null
                    ),
                    new ElementDefinition(
                        "Test_node_type.some_other_attribute",
                        List.of(new ElementDefinitionType("string", null)),
                        0,
                        "1",
                        "Some other attribute of the test node",
                        null,
                        null
                    ),
                    new ElementDefinition(
                        "Test_node_type.string_type2",
                        List.of(new ElementDefinitionType("string", null)),
                        0,
                        "1",
                        "A string attribute of the test node",
                        null,
                        null
                    )
                )
            )
        ),
        new StructureDefinitionData(
            "contains",
            "http://example.com/StructureDefinition/contains",
            "active",
            "Defines a contains relationship between two resources.",
            "complex-type",
            false,
            "contains",
            null,
            null,
            new StructureDefinitionData.Differential(
                List.of(
                    new ElementDefinition(
                        "contains.new",
                        List.of(new ElementDefinitionType("string", null)),
                        1,
                        "1",
                        "A new field that contains something.",
                        null,
                        null
                    )
                )
            )
        ),
        new StructureDefinitionData(
            "node_from_type",
            "http://example.com/StructureDefinition/node_from_type",
            "active",
            "Structure representing a node with an identifying attribute",
            "complex-type",
            false,
            null,
            null,
            null,
            new StructureDefinitionData.Differential(
                List.of(
                    new ElementDefinition(
                        "node_from_type.identifying_attribute",
                        List.of(new ElementDefinitionType("string", null)),
                        1,
                        "1",
                        "The identifying attribute of the node",
                        null,
                        null
                    )
                )
            )
        )
    );
  }
}
