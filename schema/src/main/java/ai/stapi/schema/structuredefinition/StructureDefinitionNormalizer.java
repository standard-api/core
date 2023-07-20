package ai.stapi.schema.structuredefinition;

import ai.stapi.identity.UniqueIdentifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StructureDefinitionNormalizer {

  private StructureDefinitionNormalizer() {

  }

  public static StructureDefinitionData normalize(RawStructureDefinitionData imported) {
        /*
        Sometimes when structure definition has element of primitive type. The type is not referenced by ID but by url.
         */
    var fixedElements = getFixedElements(imported);

    String replaced = null;
    var baseDefinition = imported.getBaseDefinition();
    if (baseDefinition != null) {
      replaced = baseDefinition.replace(
          "http://hl7.org/fhir/StructureDefinition/",
          ""
      );
    }
    return new StructureDefinitionData(
        imported.getId(),
        imported.getUrl(),
        imported.getStatus(),
        imported.getDescription(),
        imported.getKind(),
        imported.getIsAbstract(),
        imported.getType(),
        baseDefinition,
        baseDefinition == null ? null : new StructureDefinitionId(replaced),
        fixedElements == null ? null : new StructureDefinitionData.Differential(fixedElements)
    );
  }

  public static Map<String, Object> normalize(Map<String, Object> structureDefinition) {
        /*
        Sometimes when structure definition has element of primitive type. The type is not referenced by ID but by url.
         */
    var fixedElements = getFixedElements(structureDefinition);

    String replaced = null;
    var baseDefinition = (String) structureDefinition.get("baseDefinition");
    if (baseDefinition != null) {
      replaced = baseDefinition.replace(
          "http://hl7.org/fhir/StructureDefinition/",
          ""
      );
    }
    var newMap = new HashMap<>(structureDefinition);
    if (baseDefinition != null) {
      newMap.put(
          "baseDefinitionRef",
          new StructureDefinitionId(replaced)
      );
    }

    if (fixedElements != null) {
      newMap.put(
          "differential",
          new HashMap<>(Map.of(
              "element", fixedElements
          ))
      );
    }
    newMap.remove("snapshot");
    return newMap;
  }

  @Nullable
  private static ArrayList<ElementDefinition> getFixedElements(
      RawStructureDefinitionData imported
  ) {
    if (imported.getDifferential() == null) {
      return null;
    }
    return imported.getDifferential().getElement().stream().map(element -> {
          ArrayList<ElementDefinitionType> fixedTypes = new ArrayList<>();
          if (element.getType() != null) {
            fixedTypes = element.getType().stream()
                .map(type -> {
                  if (type.getCode().contains("http://hl7.org/fhirpath/System.")) {
                    var fixedCode = StringUtils.uncapitalize(
                        type.getCode().replace(
                            "http://hl7.org/fhirpath/System.",
                            ""
                        )
                    );
                    return new ElementDefinitionType(
                        type.getCode(),
                        new UniqueIdentifier(fixedCode),
                        type.getTargetProfile(),
                        fixTargetProfile(type.getTargetProfile())
                    );
                  }
                  return new ElementDefinitionType(
                      type.getCode(),
                      new UniqueIdentifier(type.getCode()),
                      type.getTargetProfile(),
                      fixTargetProfile(type.getTargetProfile())
                  );
                }).collect(Collectors.toCollection(ArrayList::new));
          }
          return new ElementDefinition(
              element.getPath(),
              fixedTypes,
              element.getMin(),
              element.getMax(),
              element.getShortDescription(),
              element.getDefinition(),
              element.getComment(),
              element.getContentReference()
          );
        }).collect(Collectors.toCollection(ArrayList::new));
  }

  @Nullable
  private static ArrayList<Map<String, Object>> getFixedElements(
      Map<String, Object> structureDefinition
  ) {
    var differential = (Map<String, Object>) structureDefinition.get("differential");
    if (differential == null) {
      return null;
    }
    var elements = (List<Map<String, Object>>) differential.get("element");
    return elements.stream().map(element -> {
      ArrayList<Map<String, Object>> fixedTypes = new ArrayList<>();
      var types = (List<Map<String, Object>>) element.get("type");
      if (types != null) {
        fixedTypes = types.stream().map(type -> {
          var code = (String) type.get("code");
          var fixedType = new HashMap<>(type);
          if (code.contains("http://hl7.org/fhirpath/System.")) {
            var fixedCode = StringUtils.uncapitalize(
                code.replace(
                    "http://hl7.org/fhirpath/System.",
                    ""
                )
            );
            fixedType.put("codeRef", new UniqueIdentifier(fixedCode));
          } else {
            fixedType.put("codeRef", new UniqueIdentifier(code));
          }
          var targetProfile = (List<String>) type.get("targetProfile");
          if (targetProfile != null) {
            fixedType.put("targetProfileRef", fixTargetProfile(targetProfile));
          }
          return fixedType;
        }).collect(Collectors.toCollection(ArrayList::new));
      }
      var fixedElement = new HashMap<>(element);
      fixedElement.put(
          "type",
          fixedTypes
      );
      var id = (String) element.get("id");
      if (id != null) {
        fixedElement.put(
            "id",
            id.replace("[x]", "")
        );
      }
      return fixedElement;
    }).collect(Collectors.toCollection(ArrayList::new));
  }

  @NotNull
  private static List<UniqueIdentifier> fixTargetProfile(List<String> targetProfile) {
    return targetProfile.stream().map(profile -> profile.replace(
        "http://hl7.org/fhir/StructureDefinition/",
        ""
    )).map(UniqueIdentifier::new).toList();
  }
}
