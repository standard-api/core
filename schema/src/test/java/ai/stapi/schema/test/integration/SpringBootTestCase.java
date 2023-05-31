package ai.stapi.schema.test.integration;

import ai.stapi.schema.test.base.AbstractUnitTestCase;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest()
@ActiveProfiles(profiles = {"test"})
public abstract class SpringBootTestCase extends AbstractUnitTestCase {

}
