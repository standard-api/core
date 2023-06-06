package ai.stapi.graphoperations.graphLoader.inmemory;

import ai.stapi.graph.inMemoryGraph.InMemoryGraphRepository;
import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;
import ai.stapi.graphoperations.graphLoader.search.ResolvedQueryPart;
import ai.stapi.graphoperations.graphLoader.search.SearchResolvingContext;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;

public class InMemorySearchResolvingContext implements SearchResolvingContext, ResolvedQueryPart {

  private Stream<TraversableGraphElement> graphElements;

  @Nullable
  private Comparator<TraversableGraphElement> comparator;

  private final InMemoryGraphRepository contextGraph;
  
  private final PositiveGraphDescription lastDescription;

  public InMemorySearchResolvingContext(
      Stream<TraversableGraphElement> graphElements,
      InMemoryGraphRepository contextGraph,
      PositiveGraphDescription lastDescription
  ) {
    this.graphElements = graphElements;
    this.contextGraph = contextGraph;
    this.lastDescription = lastDescription;
  }

  public InMemorySearchResolvingContext setSearchOption(SearchOptionSetter setter) {
    this.graphElements = setter.set(this.graphElements);
    return this;
  }

  public <U extends Comparable<? super U>> InMemorySearchResolvingContext setAscSort(
      Function<TraversableGraphElement, ? extends U> keyExtractor
  ) {
    if (this.comparator == null) {
      this.comparator = Comparator.comparing(keyExtractor);
    } else {
      this.comparator = this.comparator.thenComparing(keyExtractor);
    }
    return this;
  }

  public <U extends Comparable<? super U>> InMemorySearchResolvingContext setDescSort(
      Function<TraversableGraphElement, ? extends U> keyExtractor
  ) {
    if (this.comparator == null) {
      this.comparator = Comparator.comparing(keyExtractor, Comparator.reverseOrder());
    } else {
      this.comparator = this.comparator.thenComparing(keyExtractor, Comparator.reverseOrder());
    }
    return this;
  }

  public void applySort() {
    if (this.comparator == null) {
      return;
    }
    this.graphElements = this.graphElements.sorted(this.comparator);
  }

  public List<TraversableGraphElement> getGraphElements() {
    return graphElements.toList();
  }

  public InMemoryGraphRepository getContextGraph() {
    return contextGraph;
  }

  public PositiveGraphDescription getLastDescription() {
    return lastDescription;
  }

  public interface SearchOptionSetter {
    Stream<TraversableGraphElement> set(Stream<TraversableGraphElement> graphElements);
  }
}
