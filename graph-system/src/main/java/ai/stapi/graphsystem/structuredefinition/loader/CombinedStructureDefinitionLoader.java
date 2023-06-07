package ai.stapi.graphsystem.structuredefinition.loader;

import ai.stapi.schema.structuredefinition.StructureDefinitionData;
import ai.stapi.schema.structuredefinition.loader.StructureDefinitionLoader;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CombinedStructureDefinitionLoader implements StructureDefinitionLoader {

  private final SystemAdHocStructureDefinitionLoader systemAdHocStructureDefinitionLoader;
  private final DatabaseStructureDefinitionLoader databaseStructureDefinitionLoader;

  public CombinedStructureDefinitionLoader(
      SystemAdHocStructureDefinitionLoader systemAdHocStructureDefinitionLoader,
      DatabaseStructureDefinitionLoader databaseStructureDefinitionLoader
  ) {
    this.systemAdHocStructureDefinitionLoader = systemAdHocStructureDefinitionLoader;
    this.databaseStructureDefinitionLoader = databaseStructureDefinitionLoader;
  }


  @Override
  public List<StructureDefinitionData> load() {
    var adHoc = this.systemAdHocStructureDefinitionLoader.load();
    var db = this.databaseStructureDefinitionLoader.load();
    var all = new ArrayList<>(db);
    adHoc.stream().filter(structure ->
        db.stream().noneMatch(
            dbStructure -> dbStructure.getId().equals(structure.getId())
        )
    ).forEach(all::add);

    return all;
  }
}
