package ai.stapi.graphoperations.synchronization.nodeIdentificator;

import ai.stapi.graphoperations.synchronization.nodeIdentificator.testImplementation.TestNodeIdentificatorsProvider;
import ai.stapi.test.integration.IntegrationTestCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class GenericNodeIdentificatorsProviderTest extends IntegrationTestCase {
    
    @Autowired
    private GenericNodeIdentificatorsProvider genericNodeIdentificatorsProvider;
    
    @Autowired
    private TestNodeIdentificatorsProvider testNodeIdentificatorsProvider;

    @Test
    void itShouldProvideDefaultUuidIdentityIdentificatorForAllNodes() {
        var actual = this.genericNodeIdentificatorsProvider.provide("AnyNodeType");
        this.thenObjectApproved(actual);
    }

    @Test
    void itWillReturnFromMultipleProviders() {
        var testNodeType = "TestNodeType";
        this.testNodeIdentificatorsProvider.add(
            testNodeType,
            new NodeIdentificator("someAttribute")
        );
        var actual = this.genericNodeIdentificatorsProvider.provide(testNodeType);
        this.thenObjectApproved(actual);
    }
}