package ai.stapi.schema.structuredefinition;

import ai.stapi.identity.UniqueIdentifier;
import java.util.ArrayList;
import java.util.List;

public class ElementDefinitionType {

  private String code;
  private UniqueIdentifier codeRef;
  
  private List<String> targetProfile;
  
  private List<UniqueIdentifier> targetProfileRef;

  protected ElementDefinitionType() {
  }

  public ElementDefinitionType(
      String code,
      UniqueIdentifier codeRef,
      List<String> targetProfile,
      List<UniqueIdentifier> targetProfileRef
  ) {
    this.code = code;
    this.codeRef = codeRef;
    this.targetProfile = targetProfile;
    this.targetProfileRef = targetProfileRef;
  }

  public ElementDefinitionType(String code, UniqueIdentifier codeRef) {
    this.code = code;
    this.codeRef = codeRef;
    this.targetProfile = new ArrayList<>();
    this.targetProfileRef = new ArrayList<>();
  }

  public ElementDefinitionType(String code) {
    this.code = code;
    this.codeRef = new UniqueIdentifier(code);
    this.targetProfile = new ArrayList<>();
    this.targetProfileRef = new ArrayList<>();
  }

  public ElementDefinitionType(
      String code,
      List<String> targetProfile
  ) {
    this.code = code;
    this.codeRef = new UniqueIdentifier(code);
    this.targetProfile = targetProfile;
    this.targetProfileRef = targetProfile.stream().map(UniqueIdentifier::new).toList();
  }

  public String getCode() {
    return code;
  }

  public UniqueIdentifier getCodeRef() {
    return codeRef;
  }

  public List<String> getTargetProfile() {
    return targetProfile;
  }

  public List<UniqueIdentifier> getTargetProfileRef() {
    return targetProfileRef;
  }
}
