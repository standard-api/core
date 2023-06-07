package ai.stapi.graphsystem.fixtures.fixtureCommandsGenerator;


public abstract class AbstractFixtureCommandsGenerator implements FixtureCommandsGenerator {

  @Override
  public String getGeneratorName() {
    return this.getClass().getSimpleName();
  }
}
