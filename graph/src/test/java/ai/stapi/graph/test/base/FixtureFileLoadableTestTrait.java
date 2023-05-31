package ai.stapi.graph.test.base;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public interface FixtureFileLoadableTestTrait {

  default String getFixtureFilePath(String fixtureFile) {
    var relativeTestFolderPath = this.getRelativeTestFolderPath();
    var absoluteFixturePath = this.getAbsoluteFixturePath(
        fixtureFile,
        relativeTestFolderPath
    );
    return absoluteFixturePath;
  }

  default List<String> getAllFixtureFiles() {
    var fixtureFolder = this.getFixtureFolder();
    return new ArrayList<>(Arrays.stream(Objects.requireNonNull(fixtureFolder.listFiles()))
        .filter(File::isFile)
        .map(File::getAbsolutePath)
        .toList());
  }

  default String getLocationFilePath() {
    return System.getProperty("user.dir")
        + "/src/test/java/"
        + this.getRelativeTestFolderPath();
  }

  default File getFixtureFolder() {
    return new File(
        System.getProperty("user.dir") +
            "/src/test/java/" +
            this.getRelativeTestFolderPath() +
            "/fixtures"
    );
  }

  default String getAbsoluteFixturePath(
      String fixtureFile,
      String relativeTestFolderPath
  ) {
    return System.getProperty("user.dir") +
        "/src/test/java/" +
        relativeTestFolderPath +
        "/fixtures/" +
        fixtureFile;
  }

  default String getRelativeTestFolderPath() {
    var canonicalName = this.getClass().getCanonicalName();
    var split = canonicalName.split("\\.");
    return String.join(
        "/",
        Arrays.copyOfRange(
            canonicalName.split("\\."),
            0,
            split.length - 1
        )
    );
  }
}
