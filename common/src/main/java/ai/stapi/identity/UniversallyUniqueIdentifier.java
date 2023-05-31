package ai.stapi.identity;


import java.util.UUID;

public class UniversallyUniqueIdentifier extends UniqueIdentifier {


  protected UniversallyUniqueIdentifier() {
  }

  public UniversallyUniqueIdentifier(String id) {
    super(id);
  }

  public UniversallyUniqueIdentifier(UUID id) {
    super(id.toString());
  }

  public static UniversallyUniqueIdentifier randomUUID() {
    return new UniversallyUniqueIdentifier(UUID.randomUUID());
  }

  public static UniversallyUniqueIdentifier fromString(String id) {
    return new UniversallyUniqueIdentifier(UUID.fromString(id));
  }

  public UUID toUuid() {
    return UUID.fromString(this.getId());
  }
}
