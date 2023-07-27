package ai.stapi.graphoperations.configuration;

import ai.stapi.graphoperations.dagtoobjectconverter.DAGToObjectConverter;
import ai.stapi.schema.structureSchemaProvider.StructureSchemaFinder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class DAGToObjectConverterConfiguration {
    
    @Bean
    public DAGToObjectConverter dagToObjectConverter(StructureSchemaFinder structureSchemaFinder) {
        return new DAGToObjectConverter(structureSchemaFinder);
    }
}
