package ai.stapi.graphoperations.declaration;

import ai.stapi.serialization.SerializableObject;

public interface Declaration extends SerializableObject {

  String INTERFACE_UUID = "1b70940b-a231-45c7-ab32-51381a7163a2";

  String getSerializationType();

  String getDeclarationType();
}
