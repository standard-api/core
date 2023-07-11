package ai.stapi.formapi;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ai.stapi.graphsystem.systemfixtures.model.SystemModelDefinitionsLoader;
import ai.stapi.test.schemaintegration.SchemaIntegrationTestCase;
import ai.stapi.test.schemaintegration.StructureDefinitionScope;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@AutoConfigureMockMvc
@StructureDefinitionScope(SystemModelDefinitionsLoader.SCOPE)
class FormEndpointTest extends SchemaIntegrationTestCase {
  
  @Autowired
  private MockMvc mockMvc;
  
  @Test
  public void itShouldRespondWithJsonSchema() throws Exception {
    var result = this.mockMvc.perform(MockMvcRequestBuilders.get("/form/CreateStructureDefinition"))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn();
    
    var stringResponse = result.getResponse().getContentAsString();
    var mapResponse = new ObjectMapper().readValue(stringResponse, new TypeReference<Map<String, Object>>() {
    });
    this.thenObjectApproved(mapResponse);
  }

}