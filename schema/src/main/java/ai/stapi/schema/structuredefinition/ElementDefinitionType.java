package ai.stapi.schema.structuredefinition;

import java.util.ArrayList;
import java.util.List;

public class ElementDefinitionType {

  private String code;
  private List<String> targetProfile;

  protected ElementDefinitionType() {
  }

  public ElementDefinitionType(
      String code,
      List<String> targetProfile
  ) {
    this.code = code;
    this.targetProfile = targetProfile;
  }

  public ElementDefinitionType(String code) {
    this.code = code;
    this.targetProfile = new ArrayList<>();
  }

  public String getCode() {
    return code;
  }

  public List<String> getTargetProfile() {
    return targetProfile;
  }
}
