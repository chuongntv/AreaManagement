package com.chuongntv.areaapp.controllers;

import com.chuongntv.areaapp.models.*;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by chuongntv on 12/19/15.
 */
@Controller
@RequestMapping("/district")
public class DistrictController {
    private final String URL_CITY = "http://localhost:8080/api/city";
    private final String URL_COUNTRY = "http://localhost:8080/api/country";
    private final String URL_DISTRICT = "http://localhost:8080/api/district";
    private Gson gson = new Gson();

    @RequestMapping("/city/{strCityId}/list")
    public String listAll(Model model, @PathVariable String strCityId){
        RestTemplate restTemplate = new RestTemplate();
        String strJson;
        try {
            Integer id = Integer.parseInt(strCityId);
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL_DISTRICT + "/fetch/city/"+strCityId, HttpMethod.GET,null,String.class);
            strJson = responseEntity.getBody();
            if(responseEntity.getStatusCode()== HttpStatus.OK){
                ErrorMessage errorMessage = gson.fromJson(strJson,ErrorMessage.class);
                if(errorMessage.getErrorCode()== ErrorCode.SUCCESS){
                    TypeToken<List<District>> token = new TypeToken<List<District>>() {};
                    List<District> lstDistricts = gson.fromJson(errorMessage.getContent(),token.getType());
                    model.addAttribute("errorMessage","SUCCESS");
                    model.addAttribute("cityId", strCityId);
                    model.addAttribute("lstDistricts",lstDistricts);
                }else{
                    model.addAttribute("errorMessage",errorMessage.getContent());
                }
            }
        }catch (Exception ex){
            model.addAttribute("errorMessage",ex.getMessage());
        }
        return "district/list";
    }
    @RequestMapping("/city/{strCityId}/list/{strPageNumber}")
    public String listByPage(Model model, @PathVariable String strCityId,@PathVariable String strPageNumber){
        RestTemplate restTemplate = new RestTemplate();
        String strJson;
        try {
            Integer id = Integer.parseInt(strCityId);
            Integer pageNumber = Integer.parseInt(strPageNumber);
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL_DISTRICT + "/fetch/city/"+strCityId+"/page/"+strPageNumber, HttpMethod.GET,null,String.class);
            strJson = responseEntity.getBody();
            if(responseEntity.getStatusCode()== HttpStatus.OK){
                ErrorMessage errorMessage = gson.fromJson(strJson,ErrorMessage.class);
                if(errorMessage.getErrorCode()== ErrorCode.SUCCESS){
                    TypeToken<List<District>> token = new TypeToken<List<District>>() {};
                    List<District> lstDistricts = gson.fromJson(errorMessage.getContent(),token.getType());
                    model.addAttribute("errorMessage","SUCCESS");
                    model.addAttribute("cityId", strCityId);
                    model.addAttribute("lstDistricts",lstDistricts);
                }else{
                    model.addAttribute("errorMessage",errorMessage.getContent());
                }
            }
        }catch (Exception ex){
            model.addAttribute("errorMessage",ex.getMessage());
        }
        return "district/list";
    }
    @RequestMapping(value="/city/{strCityId}/create",method= RequestMethod.GET)
    public String getCreate(Model model,@PathVariable String strCityId){
        try {
            Integer id = Integer.parseInt(strCityId);
            model.addAttribute("errorMessage", "NONE");
        }catch (Exception ex){
            model.addAttribute("errorMessage", "Invalid City");
        }
        return "district/create";
    }

    @RequestMapping(value="/city/{strCityId}/create",method=RequestMethod.POST)
    public String postCreate(@PathVariable String strCityId, @RequestParam("txtName") String strName, @RequestParam("txtCode") String strCode, Model model){
        RestTemplate restTemplate = new RestTemplate();
        String strJson;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            Integer id = Integer.parseInt(strCityId);
            if(strName.isEmpty() || strName==null){
                model.addAttribute("errorMessage","District Name is not Empty");
            }
            else if (strCode.isEmpty()||strCode==null){
                model.addAttribute("errorMessage","District Code is not Empty");
            }
            else {
                City city = new City(id);
                District district = new District(strName, strCode,city);
                HttpEntity entity = new HttpEntity(district, headers);
                ResponseEntity<String> responseEntity = restTemplate.exchange(URL_DISTRICT + "/save", HttpMethod.POST, entity, String.class);
                strJson = responseEntity.getBody();
                if (responseEntity.getStatusCode() == HttpStatus.OK) {
                    ErrorMessage errorMessage = gson.fromJson(strJson, ErrorMessage.class);
                    if (errorMessage.getErrorCode() == ErrorCode.SUCCESS) {
                        TypeToken<District> token = new TypeToken<District>() {};
                        district = gson.fromJson(errorMessage.getContent(), token.getType());
                        model.addAttribute("errorMessage", "Create District success with district id=" + district.getId());
                    } else {
                        model.addAttribute("errorMessage", errorMessage.getContent());
                    }
                }
            }
        }catch (Exception ex){
            model.addAttribute("errorMessage",ex.getMessage());
        }
        return "district/create";
    }

    @RequestMapping(value="/edit",method=RequestMethod.GET)
    public String getEditRoot(Model model){
        model.addAttribute("errorMessage","Forbiden");
        return "/district/edit";
    }

    @RequestMapping(value="/edit/{strId}",method= RequestMethod.GET)
    public String getEdit(Model model,@PathVariable String strId){
        try{
            Integer id = Integer.parseInt(strId);
            RestTemplate restTemplate = new RestTemplate();
            String strJson;
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL_DISTRICT + "/fetch/" + strId, HttpMethod.GET,null,String.class);
            strJson = responseEntity.getBody();
            if(responseEntity.getStatusCode()== HttpStatus.OK){
                ErrorMessage errorMessage = gson.fromJson(strJson,ErrorMessage.class);
                if(errorMessage.getErrorCode()==ErrorCode.SUCCESS){
                    TypeToken<District> token = new TypeToken<District>() {};
                    District district = gson.fromJson(errorMessage.getContent(),token.getType());
                    responseEntity = restTemplate.exchange(URL_CITY + "/fetch/country/"+district.getCity().getCountry().getId(),HttpMethod.GET,null,String.class);
                    strJson = responseEntity.getBody();
                    ErrorMessage cityError = gson.fromJson(strJson,ErrorMessage.class);
                    if(cityError.getErrorCode()==ErrorCode.SUCCESS){
                        TypeToken<List<City>> countryToken = new TypeToken<List<City>>() {};
                        List<City> lstCities = gson.fromJson( cityError.getContent(),countryToken.getType());
                        model.addAttribute("lstCities",lstCities);
                        model.addAttribute("errorMessage","NONE");
                        model.addAttribute("cityid",district.getCity().getId());
                        model.addAttribute("district",district);
                    }
                    else{
                        model.addAttribute("errorMessage", cityError.getContent());
                    }
                }else{
                    model.addAttribute("errorMessage",errorMessage.getContent());
                }
            }
        }
        catch (Exception ex){
            model.addAttribute("errorMessage",ex.getMessage());
        }
        return "/district/edit";
    }

    @RequestMapping(value="/edit/{strId}",method=RequestMethod.POST)
    public String postEdit(@PathVariable String strId,@RequestParam("txtCity") String strCity,@RequestParam("txtName") String strName, @RequestParam("txtCode") String strCode, Model model){
        RestTemplate restTemplate = new RestTemplate();
        String strJson;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            if(strName.isEmpty() || strName==null){
                model.addAttribute("errorMessage","District Name is not Empty");
            }
            else if (strCode.isEmpty()||strCode==null){
                model.addAttribute("errorMessage","District Code is not Empty");
            }
            Integer id = Integer.parseInt(strId);
            Integer cid = Integer.parseInt(strCity);
            City city = new City(cid);
            District district = new District(id, strName,strCode,city);
            HttpEntity entity = new HttpEntity(district,headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL_DISTRICT + "/save", HttpMethod.POST,entity,String.class);
            strJson = responseEntity.getBody();
            if(responseEntity.getStatusCode()== HttpStatus.OK){
                ErrorMessage errorMessage = gson.fromJson(strJson,ErrorMessage.class);
                if(errorMessage.getErrorCode()==ErrorCode.SUCCESS){
                    TypeToken<District> token = new TypeToken<District>() {};
                    district = gson.fromJson(errorMessage.getContent(),token.getType());
                    responseEntity = restTemplate.exchange(URL_CITY + "/fetch/country/"+district.getCity().getCountry().getId(),HttpMethod.GET,null,String.class);
                    strJson = responseEntity.getBody();
                    ErrorMessage countryError = gson.fromJson(strJson,ErrorMessage.class);
                    ErrorMessage cityError = gson.fromJson(strJson,ErrorMessage.class);
                    if(cityError.getErrorCode()==ErrorCode.SUCCESS){
                        TypeToken<List<City>> countryToken = new TypeToken<List<City>>() {};
                        List<City> lstCities = gson.fromJson( cityError.getContent(),countryToken.getType());
                        model.addAttribute("lstCities",lstCities);
                        model.addAttribute("errorMessage","Update District Success");
                        model.addAttribute("cityid",district.getCity().getId());
                        model.addAttribute("district",district);
                    }
                    else{
                        model.addAttribute("errorMessage", cityError.getContent());
                    }
                }else{
                    model.addAttribute("errorMessage",errorMessage.getContent());
                }
            }
        }catch (Exception ex){
            //strJson = responseEntity.getBody();
            model.addAttribute("errorMessage",ex.getMessage());
        }
        return "/district/edit";
    }

}
