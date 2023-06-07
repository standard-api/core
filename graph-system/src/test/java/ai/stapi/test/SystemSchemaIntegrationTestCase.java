package ai.stapi.test;

import ai.stapi.graphsystem.systemfixtures.model.SystemModelDefinitionsLoader;
import ai.stapi.test.schemaintegration.SchemaIntegrationTestCase;
import ai.stapi.test.schemaintegration.StructureDefinitionScope;
import org.springframework.context.annotation.Import;

@Import(SystemSchemaIntegrationTestConfig.class)
@StructureDefinitionScope(SystemModelDefinitionsLoader.SCOPE)
public abstract class SystemSchemaIntegrationTestCase extends SchemaIntegrationTestCase {

}
