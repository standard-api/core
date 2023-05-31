package ai.stapi.graph.test.integration;

import ai.stapi.graph.configuration.AttributeFactoryConfiguration;
import ai.stapi.graph.configuration.renderer.ApiRendererConfiguration;
import ai.stapi.graph.configuration.renderer.IdLessTextRendererConfiguration;
import ai.stapi.graph.configuration.renderer.RendererConfiguration;
import ai.stapi.graph.configuration.renderer.ResponseGraphRendererConfiguration;
import ai.stapi.graph.configuration.renderer.TextRendererConfiguration;
import org.junit.jupiter.api.Tag;
import org.springframework.context.annotation.Import;

@Import({
    AttributeFactoryConfiguration.class,
    RendererConfiguration.class,
    ApiRendererConfiguration.class,
    IdLessTextRendererConfiguration.class,
    ResponseGraphRendererConfiguration.class,
    TextRendererConfiguration.class
})
@Tag("integration")
public abstract class IntegrationTestCase extends AbstractIntegrationTestCase {

}
