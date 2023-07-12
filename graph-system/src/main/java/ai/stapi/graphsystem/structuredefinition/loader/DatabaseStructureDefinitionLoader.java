package ai.stapi.graphsystem.structuredefinition.loader;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.EdgeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NodeDescriptionParameters;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.OutgoingEdgeDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.UuidIdentityDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.AttributeQueryDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.NodeQueryGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.OutgoingEdgeQueryDescription;
import ai.stapi.graphoperations.graphLoader.GraphLoader;
import ai.stapi.graphoperations.graphLoader.search.SearchQueryParameters;
import ai.stapi.graphoperations.graphLoader.search.paginationOption.OffsetPaginationOption;
import ai.stapi.graphsystem.systemfixtures.model.SystemModelDefinitionsLoader;
import ai.stapi.schema.scopeProvider.ScopeCacher;
import ai.stapi.schema.scopeProvider.ScopeOptions;
import ai.stapi.schema.scopeProvider.ScopeProvider;
import ai.stapi.schema.structuredefinition.StructureDefinitionData;
import ai.stapi.schema.structuredefinition.loader.StructureDefinitionLoader;
import java.util.List;
import org.springframework.context.annotation.Lazy;

public class DatabaseStructureDefinitionLoader implements StructureDefinitionLoader {

  public static final NodeQueryGraphDescription STRUCTURE_DEFINITION_GRAPH_DESCRIPTION = new NodeQueryGraphDescription(
      new NodeDescriptionParameters("StructureDefinition"),
      SearchQueryParameters.builder()
          .setPaginationOption(new OffsetPaginationOption(0, 300))
          .build(),
      new UuidIdentityDescription(),
      new AttributeQueryDescription("url"),
      new AttributeQueryDescription("status"),
      new AttributeQueryDescription("description"),
      new AttributeQueryDescription("kind"),      new AttributeQueryDescription("abstract"),
      new AttributeQueryDescription("type"),
      new OutgoingEdgeDescription(
          new EdgeDescriptionParameters("baseDefinitionRef"),
          new NodeDescription(
              new NodeDescriptionParameters("StructureDefinition"),
              new UuidIdentityDescription()
          )
      ),
      new OutgoingEdgeDescription(
          new EdgeDescriptionParameters("differential"),
          new NodeDescription(
              new NodeDescriptionParameters("StructureDefinitionDifferential"),
              new OutgoingEdgeQueryDescription(
                  new EdgeDescriptionParameters("element"),
                  SearchQueryParameters.builder()
                      .setPaginationOption(new OffsetPaginationOption(0, 300))
                      .build(),
                  new NodeDescription(
                      new NodeDescriptionParameters("ElementDefinition"),
                      new AttributeQueryDescription("path"),
                      new AttributeQueryDescription("min"),
                      new AttributeQueryDescription("max"),
                      new AttributeQueryDescription("short"),
                      new AttributeQueryDescription("definition"),
                      new AttributeQueryDescription("comment"),
                      new AttributeQueryDescription("contentReference"),
                      new OutgoingEdgeQueryDescription(
                          new EdgeDescriptionParameters("type"),
                          SearchQueryParameters.builder()
                              .setPaginationOption(new OffsetPaginationOption(0, 300))
                              .build(),
                          new NodeDescription(
                              new NodeDescriptionParameters("ElementDefinitionType"),
                              new AttributeQueryDescription("code"),
                              new OutgoingEdgeDescription(
                                  new EdgeDescriptionParameters("codeRef"),
                                  new NodeDescription(
                                      "StructureDefinition",
                                      new UuidIdentityDescription()
                                  )
                              ),
                              new AttributeQueryDescription("targetProfile"),
                              new OutgoingEdgeDescription(
                                  new EdgeDescriptionParameters("targetProfileRef"),
                                  new NodeDescription(
                                      "StructureDefinition",
                                      new UuidIdentityDescription()
                                  )
                              )
                          )
                      )
                  )
              )
          )
      )
  );
  
  private final GraphLoader graphLoader;
  private final SystemAdHocStructureDefinitionLoader systemAdHocStructureDefinitionLoader;
  private final ScopeCacher scopeCacher;
  private final ScopeProvider scopeProvider;

  public DatabaseStructureDefinitionLoader(
      @Lazy GraphLoader graphLoader,
      SystemAdHocStructureDefinitionLoader systemAdHocStructureDefinitionLoader,
      ScopeCacher scopeCacher,
      ScopeProvider scopeProvider
  ) {
    this.graphLoader = graphLoader;
    this.systemAdHocStructureDefinitionLoader = systemAdHocStructureDefinitionLoader;
    this.scopeCacher = scopeCacher;
    this.scopeProvider = scopeProvider;
  }

  @Override
  public List<StructureDefinitionData> load() {
    var systemScope = new ScopeOptions(SystemModelDefinitionsLoader.SCOPE, SystemModelDefinitionsLoader.TAG);
    if (this.scopeProvider.provide().equals(systemScope)) {
      return this.systemAdHocStructureDefinitionLoader.load();
    }
    if (!this.scopeCacher.hasCached(DatabaseStructureDefinitionLoader.class)) {
      var originalScope = this.scopeProvider.provide();
      this.scopeProvider.set(systemScope);
      var data = loadData();
      this.scopeProvider.set(originalScope);
      this.scopeCacher.cache(
          DatabaseStructureDefinitionLoader.class,
          data
      );
      return data;
    }

    return this.scopeCacher.getCachedOrCompute(
        DatabaseStructureDefinitionLoader.class,
        scope -> this.loadData()
    );
  }

  private List<StructureDefinitionData> loadData() {
    return this.graphLoader.find(
        STRUCTURE_DEFINITION_GRAPH_DESCRIPTION,
        StructureDefinitionData.class
    ).getData();
  }
}
