package ai.stapi.schema.adHocLoaders;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class FileLoader {

  private final Logger logger;

  public FileLoader() {

    this.logger = LoggerFactory.getLogger(FileLoader.class);
  }

  @Nullable
  public static FixtureFileInfo getFixtureFileInfo(Resource resource) {
    String fileURI;
    try {
      fileURI = resource.getURI().toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    String[] parts = fileURI.split("/");
    if (parts.length < 2) {
      return null;
    }
    String lastFolderName = parts[parts.length - 2];
    String filename = lastFolderName + File.separator + parts[parts.length - 1];
    return new FixtureFileInfo(filename, lastFolderName);
  }

  public List<Resource> loadFixtureFiles(Class<?> rootClass) {
    return this.loadFixtureFiles(rootClass, Set.of());
  }


  public List<Resource> loadFixtureFiles(Class<?> rootClass, String lastFolderName) {
    return this.loadFixtureFiles(rootClass, Set.of(), "json", lastFolderName);
  }

  public List<Resource> loadFixtureFiles(Class<?> rootClass, Set<String> processedFileNames) {
    return this.loadFixtureFiles(rootClass, processedFileNames, "json", null);
  }

  private List<Resource> loadFixtureFiles(
      Class<?> rootClass,
      Set<String> processedFileNames,
      String fileAppendix,
      @Nullable String lastFolderName
  ) {

    String relativePath = this.getRelativePath(rootClass);

//    logger.info("Relative path full: " + this.getRelativePath(rootClass));

    var resolver = new PathMatchingResourcePatternResolver(rootClass.getClassLoader());
    Resource[] resources;

    if (fileAppendix.startsWith(".")) {
      fileAppendix = fileAppendix.substring(1);
    }

    String resourceLocation = String.format(
        "classpath*:%s/**/*." + fileAppendix,
        relativePath
    );
//    logger.info("Resource location: " + resourceLocation);
    try {
      resources = resolver.getResources(
          resourceLocation
      );
    } catch (IOException e) {
      this.logger.warn("Error at " + resourceLocation);
      throw new RuntimeException(
          String.format(
              "Unable to reaf fixture files.\n"
                  + "Relative Path: %s\n"
                  + "Original error: %s",
              relativePath,
              e
          )
      );
    }
    Set<String> currentlyLoadedFixtureFiles = ConcurrentHashMap.newKeySet();
//    logger.info("Resource classpath resolved to list of resources - " + resourceLocation);
    return Arrays.stream(resources)
        .filter(resource -> {
              var fileInfo = getFixtureFileInfo(resource);
              if (fileInfo == null) {
                return false;
              }
//                    logger.info(String.format(
//                        "Loading filename: %s\nIn path: %s",
//                        filename,
//                        resourceLocation
//                    ));
              if (lastFolderName != null && !fileInfo.getLastFolderName().equals(lastFolderName)) {
                return false;
              }
              var wasAlreadyProcessed = processedFileNames.contains(fileInfo.getFullName());
              var wasCurrentlyLoaded = currentlyLoadedFixtureFiles.contains(
                  fileInfo.getFullName()
              );
              if (!wasAlreadyProcessed && !wasCurrentlyLoaded) {
                currentlyLoadedFixtureFiles.add(fileInfo.getFullName());
              }
              return !wasAlreadyProcessed && !wasCurrentlyLoaded;
            }
        ).toList();
  }

  private String getRelativePath(Class<?> rootClass) {
    var relativeCanonicalPart = rootClass.getCanonicalName();
    var partsOfRelativePart = relativeCanonicalPart.split("\\.");
    return String.join(
        File.separator,
        Arrays.copyOfRange(
            relativeCanonicalPart.split("\\."),
            0,
            partsOfRelativePart.length - 1
        )
    );
  }

  public static class FixtureFileInfo {

    private final String fullName;
    private final String lastFolderName;

    public FixtureFileInfo(String fullName, String lastFolderName) {
      this.fullName = fullName;
      this.lastFolderName = lastFolderName;
    }

    public String getFullName() {
      return fullName;
    }

    public String getLastFolderName() {
      return lastFolderName;
    }
  }

}
