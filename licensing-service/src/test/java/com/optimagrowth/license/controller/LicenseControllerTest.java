package com.optimagrowth.license.controller;

import com.google.gson.Gson;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.service.LicenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("The License Controller should")
class LicenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private LicenseService service;

    private License license;

    Gson gson = new Gson();

    @BeforeEach
    public void setUp() {
        license = License.builder()
                .id(100)
                .licenseId("321")
                .organizationId("123")
                .description("Software Product")
                .productName("product name")
                .licenseType("full")
                .build();
    }

    @Test
    void returnA200WhenRetrievingALicenseWithAValidRequest() throws Exception {
        when(service.getLicense(anyString(), anyString())).thenReturn(license);
        this.mockMvc.perform(get("/v1/organization/{organizationId}/license/{licenseId}", 123, 321)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.organizationId").value("123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.licenseId").value("321"));
    }

    @Test
    void returnA201WhenCreatingALicenseWithAValidRequest() throws Exception {
        when(service.createLicense(anyString(), any(License.class), any())).thenReturn("");
        Map<String, String> request = new HashMap<>();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/organization/{organizationId}/license", "org123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isCreated())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        String content = response.getContentAsString();
        assertTrue(content.contains("License created"));
    }

    @Test
    void returnA20WhenUpdatingALicenseWithAValidRequest() throws Exception {
        when(service.updateLicense(anyString(), any(License.class))).thenReturn("");
        Map<String, String> request = new HashMap<>();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/v1/organization/{organizationId}/license", "org123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        String content = response.getContentAsString();
        assertTrue(content.contains("updated"));
    }

    @Test
    void returnA200WhenDeletingALicenseWithAValidRequest() throws Exception {
        when(service.deleteLicense(anyString(), anyString())).thenReturn("");
        MvcResult result = mockMvc.perform(delete("/v1/organization/{organizationId}/license/{licenseId}", 123, 321)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        String content = response.getContentAsString();
        assertEquals("Deleting license with id 321 for the organization 123", content);
    }
}