package ai.stapi.formapi;

public class FormRequest {
  
  private final String operationId;

  public FormRequest(String operationId) {
    this.operationId = operationId;
  }

  public String getOperationId() {
    return operationId;
  }
}
