package ai.stapi.graphoperations.graphLoader.search.paginationOption;



public class OffsetPaginationParameters {

  private Integer offset;

  private Integer limit;

  protected OffsetPaginationParameters() {
  }

  public OffsetPaginationParameters(Integer offset, Integer limit) {
    this.offset = offset;
    this.limit = limit;
  }

  public Integer getOffset() {
    return offset;
  }

  public Integer getLimit() {
    return limit;
  }
}
