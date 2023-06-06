package ai.stapi.graphoperations.objectGraphMaper.ExampleImplementations;

import ai.stapi.identity.UniqueIdentifier;
import jakarta.annotation.Nonnull;
import java.util.UUID;

public class ExampleEntityId extends UniqueIdentifier {

  public ExampleEntityId(@Nonnull String id) {
    super(id);
  }

  public ExampleEntityId(@Nonnull UUID id) {
    super(id.toString());
  }
}
