package com.chuongntv.areaapp.test;

import com.chuongntv.areaapp.AreaManagerApplication;
import com.chuongntv.areaapp.controllers.CountryApi;
import com.chuongntv.areaapp.models.Country;
import com.chuongntv.areaapp.repositories.CountryRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by pm03 on 12/23/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AreaManagerApplication.class)
@WebIntegrationTest
public class CountryTests {

    @Inject
    private CountryApi countryApi;

    @Autowired
    private CountryRepository countryRepository;

    private MockMvc mockMvc;
    private Gson gson = new Gson();

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(countryApi).build();
        countryRepository.deleteAll();
    }

    @Test
    public void testFetchById() throws Exception {
        Country country = new Country("vietnam", "vi");
        countryRepository.save(country);
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/api/country/fetch/"+ country.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Country country1 = gson.fromJson(result,Country.class);
        Assert.assertEquals(gson.toJson(country), gson.toJson(country1));
    }

    @Test
    public void testFetchWithInvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/country/fetch/-1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreate() throws Exception {
        Country country = new Country("vietnam","vi");

        String result = mockMvc.perform(post("/api/country/save")
                .content(gson.toJson(country))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Country country1 = gson.fromJson(result,Country.class);
        country.setId(country1.getId());
        Assert.assertEquals(gson.toJson(country), gson.toJson(country1));
    }

    @Test
    public void testCreateWithDupCode() throws Exception{
        Country country = new Country("vietnam","vi");
        countryRepository.save(country);
        mockMvc.perform(post("/api/country/save")
                .content(gson.toJson(country))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdate() throws Exception{
        Country country = new Country("vietnam","vi");
        countryRepository.save(country);
        country.setName("laocai");
        String result = mockMvc.perform(post("/api/country/save")
                .content(gson.toJson(country))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Country country1 = gson.fromJson(result,Country.class);
        Assert.assertEquals(gson.toJson(country), gson.toJson(country1));
    }

    @Test
    public void testDelete() throws Exception {
        Country country = new Country("vietnam","vi");
        countryRepository.save(country);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/country/delete/" + country.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Country country1 = countryRepository.findOne(country.getId());
        Assert.assertEquals(null, country1);
    }

    @Test
    public void testDeleteWithInvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/country/delete/-1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
