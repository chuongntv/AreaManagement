package com.chuongntv.areaapp;
import com.chuongntv.areaapp.config.DatabaseForTest;
import com.chuongntv.areaapp.controllers.ApiController;
import com.chuongntv.areaapp.models.City;
import com.chuongntv.areaapp.models.Country;
import com.chuongntv.areaapp.models.District;
import com.chuongntv.areaapp.repositories.CityRepository;
import com.chuongntv.areaapp.repositories.CountryRepository;
import com.chuongntv.areaapp.repositories.DistrictRepository;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;
import javax.persistence.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by pm03 on 12/22/15.
 */

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = {PersistenceContext.class})
@SpringApplicationConfiguration(AreaManagerApplication.class)
@WebAppConfiguration
public class CountryTests {
    private MockMvc mockMvc;

    @InjectMocks
    ApiController apiController;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private DistrictRepository districtRepository;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(apiController).build();
    }



    @Test
    public void testSave() throws Exception {
        Country country = new Country("usx","vixxx");
        Gson gson = new Gson();
        String json = gson.toJson(country);

        String result = mockMvc.perform(post("/api/country/save")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertEquals("xyx", result);
    }

    @Test
    public void testWetch() throws Exception {
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/api/country/fetch").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertEquals("xxx", result);
    }

    @Test
    public void testRemove(){

    }
}
