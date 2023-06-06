package ai.stapi.graphoperations.graphLoader;

import ai.stapi.graphoperations.graphLanguage.graphDescription.GraphDescription;
import ai.stapi.graphoperations.graphLanguage.graphDescription.specific.positive.PositiveGraphDescription;

public interface ExtrapolateGraphDescription extends PositiveGraphDescription {

  GraphDescription getExtrapolatingInstructions();
}
