package ai.stapi.graphoperations.fixtures;

import ai.stapi.graph.Graph;
import ai.stapi.graphoperations.fixtures.testsystem.TestSystemModelDefinitionsLoader;
import ai.stapi.schema.adHocLoaders.GenericAdHocModelDefinitionsLoader;
import ai.stapi.schema.scopeProvider.ScopeOptions;
import ai.stapi.graphoperations.objectGraphMapper.model.GenericObjectGraphMapper;
import ai.stapi.graphoperations.objectGraphMapper.model.GraphMappingResult;
import ai.stapi.graphoperations.objectGraphMapper.model.MissingFieldResolvingStrategy;
import ai.stapi.schema.structuredefinition.RawStructureDefinitionData;
import ai.stapi.schema.structuredefinition.StructureDefinitionNormalizer;
import org.springframework.stereotype.Service;

@Service
public class ShowcaseGraphFixturesProvider {
  private final GenericAdHocModelDefinitionsLoader genericAdHocModelDefinitionsLoader;
  private final GenericObjectGraphMapper objectGraphMapper;

  public ShowcaseGraphFixturesProvider(
      GenericAdHocModelDefinitionsLoader genericAdHocModelDefinitionsLoader,
      GenericObjectGraphMapper objectGraphMapper
  ) {
    this.genericAdHocModelDefinitionsLoader = genericAdHocModelDefinitionsLoader;
    this.objectGraphMapper = objectGraphMapper;
  }

  public Graph getFixtureGraph() {
    var structures = this.genericAdHocModelDefinitionsLoader.load(
        new ScopeOptions(TestSystemModelDefinitionsLoader.SCOPE, ScopeOptions.TEST_TAG),
        RawStructureDefinitionData.SERIALIZATION_TYPE,
        RawStructureDefinitionData.class
    );
    var organizations = this.genericAdHocModelDefinitionsLoader.load(
        new ScopeOptions(TestSystemModelDefinitionsLoader.SCOPE, ScopeOptions.TEST_TAG),
        "CreateOrganization"
    );
    var mappedStructures = structures.stream()
        .map(StructureDefinitionNormalizer::normalize)
        .map(
            structure -> this.objectGraphMapper.mapToGraph(
                structure,
                "StructureDefinition",
                MissingFieldResolvingStrategy.LENIENT
            )
        ).reduce(GraphMappingResult::merge)
        .map(GraphMappingResult::getGraph)
        .orElse(new Graph());
    
    var mappedOrganizations = organizations.stream().map(
            organization -> this.objectGraphMapper.mapToGraph(
                organization,
                "Organization",
                MissingFieldResolvingStrategy.LENIENT
            )
        ).reduce(GraphMappingResult::merge)
        .map(GraphMappingResult::getGraph)
        .orElse(new Graph());
    
    return mappedStructures.merge(mappedOrganizations);
  }
}
