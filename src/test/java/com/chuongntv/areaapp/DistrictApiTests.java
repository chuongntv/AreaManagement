package com.chuongntv.areaapp;

import com.chuongntv.areaapp.models.*;
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
@WebIntegrationTest
@SpringApplicationConfiguration(AreaManagerApplication.class)
public class DistrictApiTests {
    private final String URL_COUNTRY ="http://localhost:8080/api/country";
    private final String URL_CITY="http://localhost:8080/api/city";
    private final String URL_DISTRICT="http://localhost:8080/api/district";
    Gson gson = new Gson();
    @Test
    public void fetchDistrictByCitySuccess()throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_DISTRICT + "/fetch/city/1", HttpMethod.GET,null,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.SUCCESS);
    }

    @Test
    public void fetchDistrictByInvalidCity()throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_DISTRICT + "/fetch/city/100", HttpMethod.GET,null,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }
    @Test
    public void fetchDistrictByCityWithPage()throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_DISTRICT + "/fetch/city/1/page/1", HttpMethod.GET,null,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.SUCCESS);
    }
    @Test
    public void fetchDistrictByCityWithInvalidPage()throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_DISTRICT + "/fetch/city/1/page/-1", HttpMethod.GET,null,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }
    @Test
    public void fetchDistrictById()throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_DISTRICT + "/fetch/1", HttpMethod.GET,null,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.SUCCESS);
    }
    @Test
    public void fetchDistrictByInvalidId()throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_DISTRICT + "/fetch/100", HttpMethod.GET,null,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void createDistrictSuccess()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        City city = new City(1);
        District district = new District("Hoa Khanh","HK",city);
        HttpEntity httpEntity = new HttpEntity(district,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_DISTRICT + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.SUCCESS);
    }

    @Test
    public void createDistrictWithInvalidCity()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        City city = new City(100);
        District district = new District("Hoa Cam","HC",city);
        HttpEntity httpEntity = new HttpEntity(district,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_DISTRICT + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void createDistrictWithNameIsEmpty()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        City city = new City(1);
        District district = new District("","HC",city);
        HttpEntity httpEntity = new HttpEntity(district,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_DISTRICT + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void createDistrictWithCodeIsEmpty()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        City city = new City(1);
        District district = new District("Hoa Cam","",city);
        HttpEntity httpEntity = new HttpEntity(district,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_DISTRICT + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void createDistrictWithCodeIsNotAvailable()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        City city = new City(1);
        District district = new District("Hoa Cam","01",city);
        HttpEntity httpEntity = new HttpEntity(district,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_DISTRICT + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void editDistrictSuccess()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        District district = new District(1,"HaiChau","Q HC",new City(1));
        HttpEntity httpEntity = new HttpEntity(district,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_DISTRICT + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.SUCCESS);
    }

    @Test
    public void editDistrictWithNameIsEmpty()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        District district = new District(1,"","QHC",new City(1));
        HttpEntity httpEntity = new HttpEntity(district,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_DISTRICT + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void editDistrictWithCodeIsEmpty()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        District district = new District(1,"HaiChau","",new City(1));
        HttpEntity httpEntity = new HttpEntity(district,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_DISTRICT + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void editDistrictWithInvalidId()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        District district = new District(100,"Hai Chau","TEST",new City(1));
        HttpEntity httpEntity = new HttpEntity(district,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_DISTRICT + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void editDistrictWithCityIsNotInThisCountry()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        District district = new District(1,"Hai Chau","QHC",new City(5));
        HttpEntity httpEntity = new HttpEntity(district,httpHeaders);
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_DISTRICT + "/save", HttpMethod.POST,httpEntity,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }

    @Test
    public void deleteCityWithId()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_DISTRICT + "/delete/2", HttpMethod.GET,null,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.SUCCESS);
    }

    @Test
    public void deleteCityWithInvalidId()throws  IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =restTemplate.exchange(URL_DISTRICT + "/delete/100", HttpMethod.GET,null,String.class);
        ErrorMessage errorMessage = gson.fromJson(responseEntity.getBody(),ErrorMessage.class);
        Assert.assertEquals(errorMessage.getErrorCode(), ErrorCode.ERROR);
    }
}
