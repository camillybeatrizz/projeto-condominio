package com.codewithus.kondo.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public abstract class IntegrationTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    protected String postAndReturnBody(String path, String payload) throws Exception {
        MvcResult result = mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andReturn();

        return result.getResponse().getContentAsString();
    }

    protected String jsonField(String json, String field) {
        String prefix = "\"" + field + "\":\"";
        int start = json.indexOf(prefix);
        if (start < 0) {
            throw new IllegalStateException("Campo não encontrado no JSON: " + field);
        }
        int valueStart = start + prefix.length();
        int valueEnd = json.indexOf("\"", valueStart);
        return json.substring(valueStart, valueEnd);
    }
}
