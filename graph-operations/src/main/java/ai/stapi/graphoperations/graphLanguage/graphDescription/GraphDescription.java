package ai.stapi.graphoperations.graphLanguage.graphDescription;

import ai.stapi.graphoperations.graphLanguage.GraphDeclaration;
import java.util.List;

public interface GraphDescription extends GraphDeclaration {

  String INTERFACE_UUID = "16155223-9c61-4138-a8f2-16fa555f7c61";
  String DECLARATION_TYPE = "GraphDescription";

  GraphDescriptionParameters getParameters();

  String getGraphTraversingType();

  List<GraphDescription> getChildGraphDescriptions();

}
