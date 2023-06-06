package ai.stapi.graphoperations.objectGraphLanguage;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.declaration.Declaration;

public interface ObjectGraphMapping extends Declaration {

  String INTERFACE_UUID = "1eb45324-8162-44c5-ae87-104318446aed";

  GraphDescription getGraphDescription();
}
