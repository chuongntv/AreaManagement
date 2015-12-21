package com.chuongntv.areaapp;

import com.chuongntv.areaapp.models.City;
import com.chuongntv.areaapp.models.Country;
import com.chuongntv.areaapp.models.ErrorCode;
import com.chuongntv.areaapp.models.ErrorMessage;
import com.google.gson.Gson;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * Created by chuongntv on 12/21/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AreaManagerApplication.class)
@WebIntegrationTest
public class CityApiTests {
    private final String URL_COUNTRY ="http://localhost:8080/api/country";
    private final String URL_CITY="http://localhost:8080/api/city";
    Gson gson = new Gson();

    @Test
    public void fetchCityByCountrySuccess()throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_CITY + "/fetch/country/1", HttpMethod.GET,null,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.SUCCESS);
    }
    @Test
    public void fetchCityByInvalidCountry()throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_CITY + "/fetch/country/10", HttpMethod.GET,null,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }
    @Test
    public void fetchCityByCountryWithPage()throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_CITY + "/fetch/country/1/page/1", HttpMethod.GET,null,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.SUCCESS);
    }
    @Test
    public void fetchCityByCountryInvalidPage()throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_CITY + "/fetch/country/1/page/-1", HttpMethod.GET,null,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }
    @Test
    public void fetchCityById()throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_CITY + "/fetch/1", HttpMethod.GET,null,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.SUCCESS);
    }
    @Test
    public void fetchCityByInvalidId()throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_CITY + "/fetch/10", HttpMethod.GET,null,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void createCitySuccess()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Country country = new Country(1);
        City city = new City("Quang Binh","QB",country);
        HttpEntity httpEntity = new HttpEntity(city,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_CITY + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.SUCCESS);
    }

    @Test
    public void createCityWithInvalidCity()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Country country = new Country(10);
        City city = new City("Quang Binh New","QBn",country);
        HttpEntity httpEntity = new HttpEntity(city,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_CITY + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void createCityWithNameIsEmpty()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Country country = new Country(1);
        City city = new City("","QBn",country);
        HttpEntity httpEntity = new HttpEntity(city,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_CITY + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void createCityWithCodeIsEmpty()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Country country = new Country(1);
        City city = new City("Quang Binh New","",country);
        HttpEntity httpEntity = new HttpEntity(city,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_CITY + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void createCityWithCodeIsNotAvailable()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Country country = new Country(1);
        City city = new City("Quang Binh","45",country);
        HttpEntity httpEntity = new HttpEntity(city,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_CITY + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void editCitySuccess()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        City city = new City(1,"Danang","511",new Country(1));
        HttpEntity httpEntity = new HttpEntity(city,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_CITY + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.SUCCESS);
    }

    @Test
    public void editCityWithCodeIsNotAvailable()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        City city = new City(1,"Danang","04",new Country(1));
        HttpEntity httpEntity = new HttpEntity(city,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_CITY + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void editCityWithNameIsEmpty()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        City city = new City(1,"","04",new Country(1));
        HttpEntity httpEntity = new HttpEntity(city,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_CITY + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void editCityWithCodeIsEmpty()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        City city = new City(1,"Danang","",new Country(1));
        HttpEntity httpEntity = new HttpEntity(city,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_CITY + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void editCityWithInvalidId()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        City city = new City(10,"Danang","",new Country(1));
        HttpEntity httpEntity = new HttpEntity(city,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_CITY + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }
    @Test
    public void deleteCityWithId()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_CITY + "/delete/2", HttpMethod.GET,null,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.SUCCESS);
    }

    @Test
    public void deleteCityWithInvalidId()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_CITY + "/delete/100", HttpMethod.GET,null,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }
}
