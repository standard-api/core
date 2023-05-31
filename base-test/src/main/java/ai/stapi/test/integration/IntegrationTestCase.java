package ai.stapi.test.integration;

import org.junit.jupiter.api.Tag;
import org.springframework.context.annotation.Import;

@Tag("integration")
@Import(IntegrationTestConfig.class)
public abstract class IntegrationTestCase extends AbstractIntegrationTestCase {

}
