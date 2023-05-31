package ai.stapi.schema.test.base;

import ai.stapi.objectRenderer.infrastructure.objectToJsonStringRenderer.ObjectToJSonStringOptions;
import ai.stapi.objectRenderer.infrastructure.objectToJsonStringRenderer.ObjectToJsonStringRenderer;
import ai.stapi.schema.test.FixtureFileLoadableTestTrait;
import java.util.List;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

public abstract class AbstractUnitTestCase implements FixtureFileLoadableTestTrait {
  
  private final ObjectToJsonStringRenderer objectToJsonStringRenderer = new ObjectToJsonStringRenderer();

  protected void thenObjectApproved(Object obj) {
    var options = new ObjectToJSonStringOptions(
        ObjectToJSonStringOptions.RenderFeature.SORT_FIELDS,
        ObjectToJSonStringOptions.RenderFeature.HIDE_IDS
    );
    this.thenObjectApproved(obj, options);
  }

  protected void thenObjectApproved(
      Object obj,
      ObjectToJSonStringOptions.RenderFeature... features
  ) {
    var options = new ObjectToJSonStringOptions(features);
    this.thenObjectApproved(obj, options);
  }

  protected void thenObjectApproved(
      Object obj,
      List<ObjectToJSonStringOptions.RenderFeature> features
  ) {
    var options = new ObjectToJSonStringOptions(features);
    this.thenObjectApproved(obj, options);
  }

  protected void thenObjectApprovedWithoutSorting(Object obj) {
    var options = new ObjectToJSonStringOptions(ObjectToJSonStringOptions.RenderFeature.HIDE_IDS);
    this.thenObjectApproved(obj, options);
  }

  protected void thenObjectApprovedWithShownIds(Object obj) {
    var options =
        new ObjectToJSonStringOptions(ObjectToJSonStringOptions.RenderFeature.SORT_FIELDS);
    this.thenObjectApproved(obj, options);
  }

  protected void thenObjectApproved(Object obj, ObjectToJSonStringOptions options) {
    var objRender = this.objectToJsonStringRenderer.render(obj, options);
    Approvals.verify(objRender.toPrintableString().replace("\\n", System.lineSeparator()));
  }

  protected void thenStringApproved(String actual) {
    Approvals.verify(actual.replace("\\n", System.lineSeparator()));
  }

  protected void thenObjectsEquals(Object expected, Object actual,
      ObjectToJSonStringOptions options) {
    var expectedRender = this.objectToJsonStringRenderer.render(expected, options);
    var actualRender = this.objectToJsonStringRenderer.render(actual, options);
    Assertions.assertEquals(expectedRender.toPrintableString(), actualRender.toPrintableString(),
        "Objects do not match!");
  }

  protected void thenObjectsEquals(Object expected, Object actual) {
    this.thenObjectsEquals(
        expected,
        actual,
        new ObjectToJSonStringOptions(
            ObjectToJSonStringOptions.RenderFeature.HIDE_IDS,
            ObjectToJSonStringOptions.RenderFeature.SORT_FIELDS
        )
    );
  }

  protected <T extends Throwable> T thenExceptionMessageApprovedWithHiddenUuids(
      Class<T> exception,
      Executable throwable
  ) {
    var error = Assertions.assertThrows(exception, throwable);
    var toApprove = error.getMessage().replaceAll("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})",
        "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxx");
    Approvals.verify(toApprove);
    return error;
  }

  protected <T extends Throwable> T thenExceptionMessageApproved(
      Class<T> exception,
      Executable throwable
  ) {
    var error = Assertions.assertThrows(exception, throwable);
    Approvals.verify(error.getMessage());
    return error;
  }
}
