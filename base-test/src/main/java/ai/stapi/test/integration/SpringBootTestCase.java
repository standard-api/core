package ai.stapi.test.integration;

import ai.stapi.test.base.AbstractUnitTestCase;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest()
@ActiveProfiles(profiles = {"test"})
public abstract class SpringBootTestCase extends AbstractUnitTestCase {

}
