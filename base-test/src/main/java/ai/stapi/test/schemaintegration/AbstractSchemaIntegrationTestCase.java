package ai.stapi.test.schemaintegration;

import ai.stapi.schema.scopeProvider.ScopeOptions;
import ai.stapi.schema.scopeProvider.ScopeProvider;
import ai.stapi.test.integration.AbstractIntegrationTestCase;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractSchemaIntegrationTestCase extends AbstractIntegrationTestCase {

  @Autowired
  private ScopeProvider scopeProvider;
  
  @BeforeAll
  @Order(0)
  public static void setTestClassScopeBeforeAll(
      TestInfo testInfo,
      @Autowired ScopeProvider scopeProvider
  ) {
    var testClass = testInfo.getTestClass();
    testClass.ifPresent(clazz -> {
      var testScopes = AbstractSchemaIntegrationTestCase.gatherScopeAnnotations(clazz);
      scopeProvider.set(
          new ScopeOptions(
              testScopes,
              List.of("domain", "test")
          )
      );
    });
  }

  @BeforeEach
  @Order(0)
  public void setTestClassScope(TestInfo testInfo) {
    var testClass = testInfo.getTestClass();
    testClass.ifPresent(clazz -> {
      var testScopes = AbstractSchemaIntegrationTestCase.gatherScopeAnnotations(clazz);
      this.scopeProvider.set(
          new ScopeOptions(
              testScopes,
              List.of("domain", "test")
          )
      );
    });
  }

  private static List<String> gatherScopeAnnotations(Class<?> testClazz) {
    if (testClazz == null) {
      return new ArrayList<>();
    }
    var testScopes = new ArrayList<String>();
    var parent = testClazz.getSuperclass();
    testScopes.addAll(AbstractSchemaIntegrationTestCase.gatherScopeAnnotations(parent));
    testScopes.addAll(
        Arrays.stream(testClazz.getAnnotationsByType(StructureDefinitionScope.class))
            .map(StructureDefinitionScope::value)
            .flatMap(Arrays::stream)
            .collect(Collectors.toCollection(ArrayList::new))
    );
    return testScopes;
  }

}
