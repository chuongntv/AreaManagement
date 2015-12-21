package com.chuongntv.areaapp.controllers;

import com.chuongntv.areaapp.models.City;
import com.chuongntv.areaapp.models.Country;
import com.chuongntv.areaapp.models.ErrorCode;
import com.chuongntv.areaapp.models.ErrorMessage;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by chuongntv on 12/19/15.
 */
@Controller
@RequestMapping("/city")
public class CityController {
    private final String URL_CITY = "http://localhost:8080/api/city";
    private final String URL_COUNTRY = "http://localhost:8080/api/country";
    private Gson gson = new Gson();

    @RequestMapping("/country/{strCountryId}/list")
    public String listAll(Model model, @PathVariable String strCountryId){
        RestTemplate restTemplate = new RestTemplate();
        String strJson;
        try {
            Integer id = Integer.parseInt(strCountryId);
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL_CITY + "/fetch/country/"+strCountryId, HttpMethod.GET,null,String.class);
            strJson = responseEntity.getBody();
            if(responseEntity.getStatusCode()== HttpStatus.OK){
                ErrorMessage errorMessage = gson.fromJson(strJson,ErrorMessage.class);
                if(errorMessage.getErrorCode()== ErrorCode.SUCCESS){
                    TypeToken<List<City>> token = new TypeToken<List<City>>() {};
                    List<City> lstCities = gson.fromJson(errorMessage.getContent(),token.getType());
                    model.addAttribute("errorMessage","SUCCESS");
                    model.addAttribute("countryid", strCountryId);
                    model.addAttribute("lstCities",lstCities);
                }else{
                    model.addAttribute("errorMessage",errorMessage.getContent());
                }
            }
        }catch (Exception ex){
            model.addAttribute("errorMessage",ex.getMessage());
        }
        return "city/list";
    }
    @RequestMapping("/country/{strCountryId}/list/{strPageNumber}")
    public String listByPage(Model model, @PathVariable String strCountryId,@PathVariable String strPageNumber){
        RestTemplate restTemplate = new RestTemplate();
        String strJson;
        try {
            Integer id = Integer.parseInt(strCountryId);
            Integer pageNumber = Integer.parseInt(strPageNumber);
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL_CITY + "/fetch/country/"+strCountryId+"/page/"+strPageNumber, HttpMethod.GET,null,String.class);
            strJson = responseEntity.getBody();
            if(responseEntity.getStatusCode()== HttpStatus.OK){
                ErrorMessage errorMessage = gson.fromJson(strJson,ErrorMessage.class);
                if(errorMessage.getErrorCode()== ErrorCode.SUCCESS){
                    TypeToken<List<City>> token = new TypeToken<List<City>>() {};
                    List<City> lstCities = gson.fromJson(errorMessage.getContent(),token.getType());
                    model.addAttribute("errorMessage","SUCCESS");
                    model.addAttribute("countryid", strCountryId);
                    model.addAttribute("lstCities",lstCities);
                }else{
                    model.addAttribute("errorMessage",errorMessage.getContent());
                }
            }
        }catch (Exception ex){
            model.addAttribute("errorMessage",ex.getMessage());
        }
        return "city/list";
    }

    @RequestMapping(value="/country/{strCountryId}/create",method= RequestMethod.GET)
    public String getCreate(Model model,@PathVariable String strCountryId){
        try {
            Integer id = Integer.parseInt(strCountryId);
            model.addAttribute("errorMessage", "NONE");
        }catch (Exception ex){
            model.addAttribute("errorMessage", "Invalid Country");
        }
        return "/city/create";
    }

    @RequestMapping(value="/country/{strCountryId}/create",method=RequestMethod.POST)
    public String postCreate(@PathVariable String strCountryId,@RequestParam("txtName") String strName, @RequestParam("txtCode") String strCode, Model model){
        RestTemplate restTemplate = new RestTemplate();
        String strJson;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            Integer id = Integer.parseInt(strCountryId);
            if(strName.isEmpty() || strName==null){
                model.addAttribute("errorMessage","City Name is not Empty");
            }
            else if (strCode.isEmpty()||strCode==null){
                model.addAttribute("errorMessage","City Code is not Empty");
            }
            else {
                Country country = new Country(id);
                City city = new City(strName, strCode,country);
                HttpEntity entity = new HttpEntity(city, headers);
                ResponseEntity<String> responseEntity = restTemplate.exchange(URL_CITY + "/save", HttpMethod.POST, entity, String.class);
                strJson = responseEntity.getBody();
                if (responseEntity.getStatusCode() == HttpStatus.OK) {
                    ErrorMessage errorMessage = gson.fromJson(strJson, ErrorMessage.class);
                    if (errorMessage.getErrorCode() == ErrorCode.SUCCESS) {
                        TypeToken<City> token = new TypeToken<City>() {
                        };
                        city = gson.fromJson(errorMessage.getContent(), token.getType());
                        model.addAttribute("errorMessage", "Create City success with city id=" + city.getId());
                    } else {
                        model.addAttribute("errorMessage", errorMessage.getContent());
                    }
                }
            }
        }catch (Exception ex){
            model.addAttribute("errorMessage",ex.getMessage());
        }
        return "/city/create";
    }

    @RequestMapping(value="/edit",method=RequestMethod.GET)
    public String getEditRoot(Model model){
        model.addAttribute("errorMessage","Forbiden");
        return "/city/edit";
    }

    @RequestMapping(value="/edit/{strId}",method= RequestMethod.GET)
    public String getEdit(Model model,@PathVariable String strId){
        try{
            Integer id = Integer.parseInt(strId);
            RestTemplate restTemplate = new RestTemplate();
            String strJson;
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL_CITY + "/fetch/" + strId, HttpMethod.GET,null,String.class);
            strJson = responseEntity.getBody();
            if(responseEntity.getStatusCode()== HttpStatus.OK){
                ErrorMessage errorMessage = gson.fromJson(strJson,ErrorMessage.class);
                if(errorMessage.getErrorCode()==ErrorCode.SUCCESS){
                    TypeToken<City> token = new TypeToken<City>() {};
                    City city = gson.fromJson(errorMessage.getContent(),token.getType());
                    responseEntity = restTemplate.exchange(URL_COUNTRY + "/fetch",HttpMethod.GET,null,String.class);
                    strJson = responseEntity.getBody();
                    ErrorMessage countryError = gson.fromJson(strJson,ErrorMessage.class);
                    if(countryError.getErrorCode()==ErrorCode.SUCCESS){
                        TypeToken<List<Country>> countryToken = new TypeToken<List<Country>>() {};
                        List<Country> lstCountries = gson.fromJson(countryError.getContent(),countryToken.getType());
                        model.addAttribute("lstCountries",lstCountries);
                        model.addAttribute("errorMessage","NONE");
                        model.addAttribute("countryid",city.getCountry().getId());
                        model.addAttribute("city",city);
                    }
                    else{
                        model.addAttribute("errorMessage",countryError.getContent());
                    }
                }else{
                    model.addAttribute("errorMessage",errorMessage.getContent());
                }
            }
        }
        catch (Exception ex){
            model.addAttribute("errorMessage",ex.getMessage());
        }
        return "/city/edit";
    }

    @RequestMapping(value="/edit/{strId}",method=RequestMethod.POST)
    public String postEdit(@PathVariable String strId,@RequestParam("txtCountry") String strCountry,@RequestParam("txtName") String strName, @RequestParam("txtCode") String strCode, Model model){
        RestTemplate restTemplate = new RestTemplate();
        String strJson;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            if(strName.isEmpty() || strName==null){
                model.addAttribute("errorMessage","City Name is not Empty");
            }
            else if (strCode.isEmpty()||strCode==null){
                model.addAttribute("errorMessage","City Code is not Empty");
            }
            Integer id = Integer.parseInt(strId);
            Integer cid = Integer.parseInt(strCountry);
            Country country = new Country(cid);
            City city = new City(id, strName,strCode,country);
            HttpEntity entity = new HttpEntity(city,headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL_CITY + "/save", HttpMethod.POST,entity,String.class);
            strJson = responseEntity.getBody();
            if(responseEntity.getStatusCode()== HttpStatus.OK){
                ErrorMessage errorMessage = gson.fromJson(strJson,ErrorMessage.class);
                if(errorMessage.getErrorCode()==ErrorCode.SUCCESS){
                    TypeToken<City> token = new TypeToken<City>() {};
                    city = gson.fromJson(errorMessage.getContent(),token.getType());
                    responseEntity = restTemplate.exchange(URL_COUNTRY + "/fetch",HttpMethod.GET,null,String.class);
                    strJson = responseEntity.getBody();
                    ErrorMessage countryError = gson.fromJson(strJson,ErrorMessage.class);
                    if(countryError.getErrorCode()==ErrorCode.SUCCESS) {
                        TypeToken<List<Country>> countryToken = new TypeToken<List<Country>>() {
                        };
                        List<Country> lstCountries = gson.fromJson(countryError.getContent(), countryToken.getType());
                        model.addAttribute("lstCountries", lstCountries);
                        model.addAttribute("countryid", city.getCountry().getId());
                        model.addAttribute("errorMessage", "Update City Success");
                        model.addAttribute("city",city);
                    }
                    else{
                        model.addAttribute("errorMessage",errorMessage.getContent());
                    }
                }else{
                    model.addAttribute("errorMessage",errorMessage.getContent());
                }
            }
        }catch (Exception ex){
            //strJson = responseEntity.getBody();
            model.addAttribute("errorMessage",ex.getMessage());
        }
        return "/city/edit";
    }
    @RequestMapping(value="/delete",method=RequestMethod.GET)
    @ResponseBody
    public String getDeleteRoot(){
        return "Forbiden";
    }

    @RequestMapping(value="/delete/{strId}",method=RequestMethod.GET)
    @ResponseBody
    public String getDelete(@PathVariable String strId){
        RestTemplate restTemplate = new RestTemplate();
        String strJson;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            Integer id = Integer.parseInt(strId);
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL_CITY + "/delete/"+strId, HttpMethod.GET,null,String.class);
            strJson = responseEntity.getBody();
            if(responseEntity.getStatusCode()== HttpStatus.OK){
                ErrorMessage errorMessage = gson.fromJson(strJson,ErrorMessage.class);
                return errorMessage.getContent();
            }
        }catch (Exception ex){
            //strJson = responseEntity.getBody();
            return ex.getMessage();
        }
        return "Forbiden";
    }
}

