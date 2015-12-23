package com.chuongntv.areaapp.test;

import com.chuongntv.areaapp.AreaManagerApplication;
import com.chuongntv.areaapp.controllers.CityApi;
import com.chuongntv.areaapp.controllers.CountryApi;
import com.chuongntv.areaapp.models.City;
import com.chuongntv.areaapp.models.Country;
import com.chuongntv.areaapp.repositories.CityRepository;
import com.chuongntv.areaapp.repositories.CountryRepository;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.WebIntegrationTest;
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
public class CityTests {
    @Inject
    private CityApi cityApi;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CountryRepository countryRepository;

    private MockMvc mockMvc;
    private Gson gson = new Gson();
    private Country country;
    private City city;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cityApi).build();
        cityRepository.deleteAll();
        countryRepository.deleteAll();
    }

    public void createData(){
        country = new Country("vietnam", "vi");
        countryRepository.save(country);
        city = new City("dannang","dn", country);
        cityRepository.save(city);
    }

    @Test
    public void testFetchById() throws Exception {
        createData();
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/api/city/fetch/"+ city.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        City city1 = gson.fromJson(result,City.class);
        city.setCountry(null);
        Assert.assertEquals(gson.toJson(city), gson.toJson(city1));
    }

    @Test
    public void testCityFetchWithInvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/city/fetch/-1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCityCreate() throws Exception {
        country = new Country("vietnam", "vi");
        countryRepository.save(country);
        city = new City("dannang","dn", country);

        String result = mockMvc.perform(post("/api/city/save")
                .content(gson.toJson(city))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        City city1 = gson.fromJson(result,City.class);
        city.setId(city1.getId());
        Assert.assertEquals(gson.toJson(city), gson.toJson(city1));
    }

    @Test
    public void testCityUpdate() throws Exception{
        createData();
        city.setName("hanoi");
        String result = mockMvc.perform(post("/api/city/save")
                .content(gson.toJson(city))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        City city1 = gson.fromJson(result, City.class);
        city.setCountry(null);
        Assert.assertEquals(gson.toJson(city), gson.toJson(city1));
    }

    @Test
    public void testCityDelete() throws Exception {
        createData();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/city/delete/" + city.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        City city1 = cityRepository.findOne(city.getId());
        Assert.assertEquals(null, city1);
    }

    @Test
    public void testCityDeleteWithInvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/city/delete/-1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
