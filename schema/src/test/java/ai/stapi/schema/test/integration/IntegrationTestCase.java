package ai.stapi.schema.test.integration;

import ai.stapi.schema.configuration.AdHocLoaderConfiguration;
import ai.stapi.schema.configuration.ScopeProviderConfiguration;
import ai.stapi.schema.configuration.StructureSchemaConfiguration;
import org.junit.jupiter.api.Tag;
import org.springframework.context.annotation.Import;

@Import({
    ScopeProviderConfiguration.class,
    AdHocLoaderConfiguration.class,
    StructureSchemaConfiguration.class
})
@Tag("integration")
public abstract class IntegrationTestCase extends AbstractIntegrationTestCase {

}
