package com.example.Employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CompanyController controller;

    @BeforeEach
    public void setup() {
        controller.clear();
    }

    @Test
    void should_return_company_list_when_get() throws Exception {
        controller.addCompany("company1");
        controller.addCompany("company2");
        MockHttpServletRequestBuilder request = get("/companies")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("company1"))
                .andExpect(jsonPath("$[1].name").value("company2"));
    }

    @Test
    void should_return_company_when_get_with_id_exists() throws Exception {
        Company company1 = controller.addCompany("company1");
        controller.addCompany("company2");
        MockHttpServletRequestBuilder request = get("/companies/" + company1.id())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(company1.id()))
                .andExpect(jsonPath("$.name").value("company1"));
    }

    @Test
    void should_return_page_companies_when_get_with_page_and_size() throws Exception {
        for (int i = 1; i <= 10; i++) {
            controller.addCompany("Company" + i);
        }

        MockHttpServletRequestBuilder request = get("/companies?page=1&size=5")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].name").value("Company1"))
                .andExpect(jsonPath("$[4].name").value("Company5"));
    }

    @Test
    void should_return_created_company_when_post() throws Exception {
        String requestBody = """
                {
                    "name": "Spring Company"
                }
                """;
        MockHttpServletRequestBuilder request = post("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Spring Company"));
    }

    @Test
    void should_update_company_when_put() throws Exception {
        Company company = controller.addCompany("Old Company");
        String requestBody = """
                {
                    "name": "Updated Company"
                }
                """;
        MockHttpServletRequestBuilder request = put("/companies/" + company.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(company.id()))
                .andExpect(jsonPath("$.name").value("Updated Company"));
    }

    @Test
    void should_delete_company_when_delete() throws Exception {
        Company company = controller.addCompany("To Delete");
        MockHttpServletRequestBuilder request = delete("/companies/" + company.id())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isNoContent());
    }
}
