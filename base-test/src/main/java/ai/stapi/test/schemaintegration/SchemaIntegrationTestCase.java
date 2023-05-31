package ai.stapi.test.schemaintegration;

import org.junit.jupiter.api.Tag;
import org.springframework.context.annotation.Import;

@Tag("schema-integration")
@Import(SchemaIntegrationTestConfig.class)
public class SchemaIntegrationTestCase extends AbstractSchemaIntegrationTestCase {
}
