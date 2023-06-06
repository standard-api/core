package ai.stapi.graphoperations.serializationTypeProvider;

import ai.stapi.graph.traversableGraphElements.TraversableGraphElement;
import ai.stapi.graphoperations.serializationTypeProvider.exception.GenericSerializationTypeProviderException;
import ai.stapi.graphoperations.serializationTypeProvider.specific.SpecificSerializationTypeProvider;
import java.util.List;
import org.springframework.stereotype.Service;

public class GenericSerializationTypeByNodeProvider {

  private final List<SpecificSerializationTypeProvider> serializationTypeProviders;

  public GenericSerializationTypeByNodeProvider(
      List<SpecificSerializationTypeProvider> serializationTypeProviders
  ) {
    this.serializationTypeProviders = serializationTypeProviders;
  }

  public String getSerializationType(TraversableGraphElement element) {
    var provider = this.getSupportingProvider(element);
    return provider.provideSerializationType(element);
  }

  public boolean existsSerializationTypeForNode(TraversableGraphElement element) {
    try {
      this.getSupportingProvider(element);
      return true;
    } catch (GenericSerializationTypeProviderException ignore) {
      return false;
    }
  }

  private SpecificSerializationTypeProvider getSupportingProvider(TraversableGraphElement element) {
    var supportingProviders = this.serializationTypeProviders.stream()
        .filter(provider -> provider.supports(element))
        .toList();
    if (supportingProviders.size() == 0) {
      throw GenericSerializationTypeProviderException.becauseThereIsNotSupportingProvider(element);
    }
    return supportingProviders.get(0);
  }
}
