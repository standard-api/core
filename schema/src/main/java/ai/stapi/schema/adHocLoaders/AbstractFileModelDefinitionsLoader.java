package ai.stapi.schema.adHocLoaders;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

public abstract class AbstractFileModelDefinitionsLoader
    implements SpecificAdHocModelDefinitionsLoader {
  
  protected final String scope;
  protected final String tag;
  private final ObjectMapper objectMapper = new ObjectMapper()
      .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
      .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  private FileLoader fileLoader;

  protected AbstractFileModelDefinitionsLoader(
      FileLoader fileLoader,
      String scope,
      String tag
  ) {
    this.fileLoader = fileLoader;
    this.scope = scope;
    this.tag = tag;
  }

  @Override
  public <T> List<T> load(String serializationType, Class<T> returnClass) {
    var resources = this.fileLoader.loadFixtureFiles(
        this.getClass(),
        serializationType
    );

    return resources.stream()
        .sorted(Comparator.comparing(Resource::getFilename))
        .map(resource -> this.deserializeJsonFile(resource, returnClass))
        .toList();
  }

  private <R> R deserializeJsonFile(
      Resource resource,
      Class<R> returnClass
  ) {
    byte[] content;
    try {
      InputStream input = resource.getInputStream();
      content = IOUtils.toByteArray(input);
    } catch (IOException exception) {
      try {
        throw new RuntimeException(
            String.format(
                "Unable to read resource file of URI %s",
                resource.getURI()
            ),
            exception
        );
      } catch (IOException ex) {
        throw new RuntimeException(
            "Unable to read resource file and not even its URI.",
            exception
        );
      }
    }
    var info = FileLoader.getFixtureFileInfo(resource);
    if (info == null) {
      throw new RuntimeException("Please put fixture file in folder named by right command.");
    }
    try {
      return objectMapper.readValue(content, returnClass);
    } catch (IOException e) {
      throw new RuntimeException(
          "Failed while deserializing '" + info.getFullName() + "'.",
          e
      );
    }
  }

  @Override
  public String getScope() {
    return scope;
  }

  @Override
  public String getTag() {
    return tag;
  }
}
