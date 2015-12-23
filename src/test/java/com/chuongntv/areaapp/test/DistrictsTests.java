package com.chuongntv.areaapp.test;

import com.chuongntv.areaapp.AreaManagerApplication;
import com.chuongntv.areaapp.controllers.CityApi;
import com.chuongntv.areaapp.controllers.DistrictApi;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by pm03 on 12/23/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AreaManagerApplication.class)
@WebIntegrationTest
public class DistrictsTests {
//    @Inject
//    private DistrictApi districtApi;
//
//    @Autowired
//    private CityRepository cityRepository;
//
//    @Autowired
//    private CountryRepository countryRepository;
//
//    @Autowired
//    private DistrictRepository districtRepository;
//
//    private MockMvc mockMvc;
//    private Gson gson = new Gson();
//    private Country country;
//    private City city;
//    private District district;
//
//    @Before
//    public void setUp(){
//        MockitoAnnotations.initMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(districtApi).build();
//        districtRepository.deleteAll();
//        cityRepository.deleteAll();
//        countryRepository.deleteAll();
//    }
//
//    public void createDataForDistrict(){
//        country = new Country("vietnam", "vi");
//        countryRepository.save(country);
//        city = new City("dannang","dn", country);
//        cityRepository.save(city);
//        district = new District("haichau","jc",city);
//        districtRepository.save(district);
//    }
//
//    @Test
//    public void testDistrictFetchById() throws Exception {
//        createDataForDistrict();
//        String result = mockMvc.perform(MockMvcRequestBuilders.get("/api/district/fetch/"+ district.getId()).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//        District district1 = gson.fromJson(result,District.class);
//        district.setCity(null);
//        Assert.assertEquals(gson.toJson(district), gson.toJson(district1));
//    }
//
//    @Test
//    public void testDistrictFetchWithInvalidId() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/city/fetch/-1").accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
}
