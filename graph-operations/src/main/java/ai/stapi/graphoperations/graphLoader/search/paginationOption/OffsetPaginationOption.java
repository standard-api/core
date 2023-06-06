package ai.stapi.graphoperations.graphLoader.search.paginationOption;

public class OffsetPaginationOption extends AbstractPaginationOption<OffsetPaginationParameters> {

  public static final String STRATEGY = "offset";

  private OffsetPaginationOption() {
  }

  public OffsetPaginationOption(Integer offset, Integer limit) {
    super(OffsetPaginationOption.STRATEGY, new OffsetPaginationParameters(offset, limit));
  }

}
