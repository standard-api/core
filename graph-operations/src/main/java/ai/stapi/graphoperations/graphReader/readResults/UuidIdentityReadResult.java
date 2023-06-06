package ai.stapi.graphoperations.graphReader.readResults;

import ai.stapi.identity.UniqueIdentifier;

public class UuidIdentityReadResult implements ValueReadResult {

  private final UniqueIdentifier uniqueIdentifier;

  public UuidIdentityReadResult(UniqueIdentifier uniqueIdentifier) {
    this.uniqueIdentifier = uniqueIdentifier;
  }

  public UniqueIdentifier getUniqueIdentifier() {
    return uniqueIdentifier;
  }

  @Override
  public Object getValue() {
    return this.uniqueIdentifier.getId();
  }
}
