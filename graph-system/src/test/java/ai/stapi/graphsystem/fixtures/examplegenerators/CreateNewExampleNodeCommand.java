package ai.stapi.graphsystem.fixtures.examplegenerators;

import ai.stapi.graphsystem.messaging.command.AbstractCommand;
import ai.stapi.identity.UniqueIdentifier;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateNewExampleNodeCommand extends AbstractCommand<UniqueIdentifier> {

  public static final String SERIALIZATION_TYPE = "CreateNewExampleNodeCommand";

  @Nonnull
  private String exampleNodeType;

  @Nonnull
  private String exampleNodeName;

  @Nonnull
  private Integer exampleQuantity;

  @Nonnull
  private String exampleStringAttribute;

  private List<String> exampleListAttribute;

  protected CreateNewExampleNodeCommand() {
  }

  public CreateNewExampleNodeCommand(
      @Nonnull UniqueIdentifier exampleNodeId,
      @Nonnull String exampleNodeType,
      @Nonnull String exampleNodeName
  ) {
    this(exampleNodeId, exampleNodeType, exampleNodeName, 0, "default");
  }

  public CreateNewExampleNodeCommand(
      @Nonnull UniqueIdentifier exampleNodeId,
      @Nonnull String exampleNodeType,
      @Nonnull String exampleNodeName,
      @Nonnull Integer exampleQuantity,
      @Nonnull String exampleStringAttribute,
      String... exampleListAttribute
  ) {
    super(exampleNodeId, SERIALIZATION_TYPE);
    this.exampleNodeType = exampleNodeType;
    this.exampleNodeName = exampleNodeName;
    this.exampleQuantity = exampleQuantity;
    this.exampleStringAttribute = exampleStringAttribute;
    this.exampleListAttribute = new ArrayList<>(Arrays.stream(exampleListAttribute).toList());
  }

  public String getExampleNodeName() {
    return this.exampleNodeName;
  }

  @JsonIgnore
  public UniqueIdentifier getUniqueIdentifier() {
    return this.getTargetIdentifier();
  }

  public String getExampleNodeType() {
    return exampleNodeType;
  }

  public Integer getExampleQuantity() {
    return exampleQuantity;
  }

  public String getExampleStringAttribute() {
    return exampleStringAttribute;
  }

  public List<String> getExampleListAttribute() {
    return exampleListAttribute;
  }
}
