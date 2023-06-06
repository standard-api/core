package ai.stapi.graphoperations.graphLoader;

import ai.stapi.graphoperations.graphLoader.search.exceptions.UnsupportedFilterOptionAttributeValueType;
import ai.stapi.graphoperations.graphLoader.search.filterOption.EqualsFilterOption;
import ai.stapi.graph.Graph;
import ai.stapi.test.base.UnitTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class EqualsFilterOptionTest extends UnitTestCase {

  @Test
  void itShouldThrowExceptionWhenUnsupportedValueTypePassed() {
    Executable executable =
        () -> new EqualsFilterOption<>("irrelevant", new Graph());
    Assertions.assertThrows(UnsupportedFilterOptionAttributeValueType.class, executable);
  }
}
