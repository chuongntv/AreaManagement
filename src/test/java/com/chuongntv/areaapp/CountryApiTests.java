package com.chuongntv.areaapp;

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
 * Created by chuongntv on 12/19/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AreaManagerApplication.class)
@WebIntegrationTest

public class CountryApiTests {
    private final String URL_COUNTRY ="http://localhost:8080/api/country";
    Gson gson = new Gson();
    @Test
    public void fetchCountryWithAllPage()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_COUNTRY + "/fetch", HttpMethod.GET,null,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.SUCCESS);
    }
    @Test
    public void fetchCountryWithCorrectPage()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_COUNTRY + "/fetch/page/1", HttpMethod.GET,null,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.SUCCESS);
    }
    @Test
    public void fetchCountryWithErrorPage()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_COUNTRY + "/fetch/page/a", HttpMethod.GET,null,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void fetchCountryWithId()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_COUNTRY + "/fetch/1", HttpMethod.GET,null,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.SUCCESS);
    }

    @Test
    public void fetchCountryWithInvalidId()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_COUNTRY + "/fetch/0", HttpMethod.GET,null,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void createCountrySuccess()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Country country = new Country("Japan","jp");
        HttpEntity httpEntity = new HttpEntity(country,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_COUNTRY + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.SUCCESS);
    }

    @Test
    public void createCountryWithCodeIsNotAvailable()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Country country = new Country("Viet Nam","vi");
        HttpEntity httpEntity = new HttpEntity(country,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_COUNTRY + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void createCountryWithNameIsEmpty()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Country country = new Country("","sg");
        HttpEntity httpEntity = new HttpEntity(country,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_COUNTRY + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void createCountryWithCodeIsEmpty()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Country country = new Country("Singapore","");
        HttpEntity httpEntity = new HttpEntity(country,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_COUNTRY + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void editCountrySuccess()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Country country = new Country(3,"Malay","ml");
        HttpEntity httpEntity = new HttpEntity(country,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_COUNTRY + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.SUCCESS);
    }

    @Test
    public void editCountryWithCodeIsNotAvailable()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Country country = new Country(1,"VIET NAM","ml");
        HttpEntity httpEntity = new HttpEntity(country,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_COUNTRY + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void editCountryWithNameIsEmpty()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Country country =  new Country(1,"","VN");
        HttpEntity httpEntity = new HttpEntity(country,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_COUNTRY + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void editCountryWithCodeIsEmpty()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Country country = new Country(1,"VIET NAM","");
        HttpEntity httpEntity = new HttpEntity(country,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_COUNTRY + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void editCountryWithInvalidId()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Country country = new Country(0,"VIET NAM","VI");
        HttpEntity httpEntity = new HttpEntity(country,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_COUNTRY + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void deleteCountryWithId()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_COUNTRY + "/delete/2", HttpMethod.GET,null,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.SUCCESS);
    }

    @Test
    public void deleteCountryWithInvalidId()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_COUNTRY + "/delete/0", HttpMethod.GET,null,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }
}
