package ai.stapi.test;

import ai.stapi.graphsystem.systemfixtures.model.SystemModelDefinitionsLoader;
import ai.stapi.test.schemaintegration.AbstractSchemaIntegrationTestCase;
import ai.stapi.test.schemaintegration.StructureDefinitionScope;

@StructureDefinitionScope(SystemModelDefinitionsLoader.SCOPE)
public abstract class SystemSchemaIntegrationTestCase extends AbstractSchemaIntegrationTestCase {

}
