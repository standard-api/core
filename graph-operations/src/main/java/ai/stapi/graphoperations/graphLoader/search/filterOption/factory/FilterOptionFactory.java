package ai.stapi.graphoperations.graphLoader.search.filterOption.factory;

import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.NullGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.query.AttributeQueryDescription;
import ai.stapi.graphoperations.graphLoader.search.exceptions.CannotCreateFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.AbstractOneValueFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.AllMatchFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.AndFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.AnyMatchFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.CompositeFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.CompositeFilterParameters;
import ai.stapi.graphoperations.graphLoader.search.filterOption.ContainsFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.EndsWithFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.EqualsFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.FilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.FilterOptionParameters;
import ai.stapi.graphoperations.graphLoader.search.filterOption.GreaterThanFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.GreaterThanOrEqualFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.LowerThanFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.LowerThanOrEqualsFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.NoneMatchFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.NotEqualsFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.NotFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.OneValueFilterOptionParameters;
import ai.stapi.graphoperations.graphLoader.search.filterOption.OrFilterOption;
import ai.stapi.graphoperations.graphLoader.search.filterOption.StartsWithFilterOption;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FilterOptionFactory {

  public FilterOption<?> create(
      String filterStrategy,
      FilterOptionParameters filterOptionParameters
  ) {
    if (filterOptionParameters instanceof OneValueFilterOptionParameters<?> oneValueFilterOptionParameters) {
      return this.createLeaf(
          filterStrategy,
          oneValueFilterOptionParameters.getAttributeNamePath(),
          oneValueFilterOptionParameters.getAttributeValue()
      );
    }
    if (filterOptionParameters instanceof CompositeFilterParameters compositeFilterParameters) {
      return this.createComposite(
          filterStrategy,
          compositeFilterParameters.getChildFilterOptions()
      );
    }
    throw CannotCreateFilterOption.becauseInvalidFilterOptionParameters(filterStrategy,
        filterOptionParameters);
  }

  public AbstractOneValueFilterOption<?> createLeaf(
      String filterStrategy,
      PositiveGraphDescription attributeNamePath,
      Object attributeValue
  ) {
    if (filterStrategy.equals(ContainsFilterOption.STRATEGY)) {
      return new ContainsFilterOption(attributeNamePath, (String) attributeValue);
    }
    if (filterStrategy.equals(EndsWithFilterOption.STRATEGY)) {
      return new EndsWithFilterOption(attributeNamePath, (String) attributeValue);
    }
    if (filterStrategy.equals(EqualsFilterOption.STRATEGY)) {
      return new EqualsFilterOption<>(attributeNamePath, attributeValue);
    }
    if (filterStrategy.equals(GreaterThanFilterOption.STRATEGY)) {
      return new GreaterThanFilterOption<>(attributeNamePath, attributeValue);
    }
    if (filterStrategy.equals(GreaterThanOrEqualFilterOption.STRATEGY)) {
      return new GreaterThanOrEqualFilterOption<>(attributeNamePath, attributeValue);
    }
    if (filterStrategy.equals(LowerThanFilterOption.STRATEGY)) {
      return new LowerThanFilterOption<>(attributeNamePath, attributeValue);
    }
    if (filterStrategy.equals(LowerThanOrEqualsFilterOption.STRATEGY)) {
      return new LowerThanOrEqualsFilterOption<>(attributeNamePath, attributeValue);
    }
    if (filterStrategy.equals(NotEqualsFilterOption.STRATEGY)) {
      return new NotEqualsFilterOption<>(attributeNamePath, attributeValue);
    }
    if (filterStrategy.equals(StartsWithFilterOption.STRATEGY)) {
      return new StartsWithFilterOption(attributeNamePath, (String) attributeValue);
    }
    throw CannotCreateFilterOption.becauseInvalidLeafFilterStrategyProvided(filterStrategy);
  }

  public AbstractOneValueFilterOption<?> createLeaf(
      String filterStrategy,
      String attributeName,
      Object attributeValue
  ) {
    return this.createLeaf(
        filterStrategy,
        new AttributeQueryDescription(attributeName),
        attributeValue
    );
  }

  public FilterOption<?> createComposite(
      String filterStrategy,
      List<FilterOption<?>> childFilterOption
  ) {
    try {
      return this.createList(filterStrategy, (AbstractOneValueFilterOption<?>) childFilterOption.get(0));
    } catch (CannotCreateFilterOption ignored) {
      //ignore exception
    }
    return this.createLogical(filterStrategy, childFilterOption);
  }

  public FilterOption<?> createList(
      String filterStrategy,
      AbstractOneValueFilterOption<?> childFilterOption
  ) {
    if (filterStrategy.equals(AllMatchFilterOption.STRATEGY)) {
      return new AllMatchFilterOption(childFilterOption);
    }
    if (filterStrategy.equals(AnyMatchFilterOption.STRATEGY)) {
      return new AnyMatchFilterOption(childFilterOption);
    }
    if (filterStrategy.equals(NoneMatchFilterOption.STRATEGY)) {
      return new NoneMatchFilterOption(childFilterOption);
    }
    throw CannotCreateFilterOption.becauseInvalidListFilterStrategyProvided(filterStrategy);
  }

  public FilterOption<?> createLogical(
      String filterStrategy,
      List<FilterOption<?>> childFilterOption
  ) {
    if (filterStrategy.equals(AndFilterOption.STRATEGY)) {
      return new AndFilterOption(childFilterOption);
    }
    if (filterStrategy.equals(OrFilterOption.STRATEGY)) {
      return new OrFilterOption(childFilterOption);
    }
    if (filterStrategy.equals(NotFilterOption.STRATEGY)) {
      return new NotFilterOption(childFilterOption.get(0));
    }
    throw CannotCreateFilterOption.becauseInvalidLogicalFilterStrategyProvided(filterStrategy);
  }

  public FilterOption<?> copyWithNewAttributeNamePath(
      FilterOption<?> originalFilter,
      PositiveGraphDescription attributeNamePath
  ) {
    if (originalFilter instanceof CompositeFilterOption compositeFilterOption) {
      var newChildren = new ArrayList<FilterOption<?>>();
      compositeFilterOption.getParameters().getChildFilterOptions()
          .stream()
          .map(child -> this.copyWithNewAttributeNamePath(child, attributeNamePath))
          .forEach(newChildren::add);

      return this.createComposite(
          compositeFilterOption.getStrategy(),
          newChildren
      );
    }
    if (originalFilter instanceof AbstractOneValueFilterOption<?> oneValueFilterOption) {
      return this.createLeaf(
          oneValueFilterOption.getStrategy(),
          attributeNamePath,
          oneValueFilterOption.getParameters().getAttributeValue()
      );
    }
    throw CannotCreateFilterOption.becauseInvalidFilterOptionFoundWhenMakingCopy(originalFilter);
  }

  public PositiveGraphDescription getAttributeNamePath(
      FilterOption<?> filterOption
  ) {
    if (filterOption instanceof CompositeFilterOption compositeFilterOption) {
      return this.getAttributeNamePath(
          compositeFilterOption.getParameters().getChildFilterOptions().get(0)
      );
    }
    if (filterOption instanceof AbstractOneValueFilterOption<?> oneValueFilterOption) {
      return oneValueFilterOption.getParameters().getAttributeNamePath();
    }
    return new NullGraphDescription();
  }
}
