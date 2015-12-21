package com.chuongntv.areaapp.controllers;

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
 * Created by chuongntv on 12/17/15.
 */
@Controller
@RequestMapping("/country")
public class CountryController {

    private final String URL = "http://localhost:8080/api/country";
    private Gson gson = new Gson();

    @RequestMapping("/list")
    public String listAll(Model model){
        RestTemplate restTemplate = new RestTemplate();
        String strJson;
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL + "/fetch", HttpMethod.GET,null,String.class);
            strJson = responseEntity.getBody();
            if(responseEntity.getStatusCode()== HttpStatus.OK){
                ErrorMessage errorMessage = gson.fromJson(strJson,ErrorMessage.class);
                if(errorMessage.getErrorCode()==ErrorCode.SUCCESS){
                    TypeToken<List<Country>> token = new TypeToken<List<Country>>() {};
                    List<Country> lstCountries = gson.fromJson(errorMessage.getContent(),token.getType());
                    model.addAttribute("errorMessage","SUCCESS");
                    model.addAttribute("lstCountries",lstCountries);

                }else{
                    model.addAttribute("errorMessage",errorMessage.getContent());
                }
            }
        }catch (Exception ex){
            //strJson = responseEntity.getBody();
            model.addAttribute("errorMessage",ex.getMessage());
        }
        return "country/list";
    }

    @RequestMapping("/list/{id}")
    public String listByPage(Model model, @PathVariable String id){
        RestTemplate restTemplate = new RestTemplate();
        String strJson;
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL + "/fetch/page/" + id, HttpMethod.GET,null,String.class);
            strJson = responseEntity.getBody();
            if(responseEntity.getStatusCode()== HttpStatus.OK){
                ErrorMessage errorMessage = gson.fromJson(strJson,ErrorMessage.class);
                if(errorMessage.getErrorCode()==ErrorCode.SUCCESS){
                    TypeToken<List<Country>> token = new TypeToken<List<Country>>() {};
                    List<Country> lstCountries = gson.fromJson(errorMessage.getContent(),token.getType());
                    model.addAttribute("errorMessage","SUCCESS");
                    model.addAttribute("lstCountries",lstCountries);

                }else{
                    model.addAttribute("errorMessage",errorMessage.getContent());
                }
            }
        }catch (Exception ex){
            //strJson = responseEntity.getBody();
            model.addAttribute("errorMessage",ex.getMessage());
        }
        return "country/list";
    }

    @RequestMapping(value="/create",method= RequestMethod.GET)
    public String getCreate(Model model){
        model.addAttribute("errorMessage","NONE");
        return "/country/create";
    }

    @RequestMapping(value="/create",method=RequestMethod.POST)
    public String postCreate(@RequestParam("txtName") String strName, @RequestParam("txtCode") String strCode, Model model){
        RestTemplate restTemplate = new RestTemplate();
        String strJson;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            if(strName.isEmpty() || strName==null){
                model.addAttribute("errorMessage","Country Name is not Empty");
            }
            else if (strCode.isEmpty()||strCode==null){
                model.addAttribute("errorMessage","Country Code is not Empty");
            }
            else {
                Country country = new Country(strName,strCode);
                HttpEntity entity = new HttpEntity(country,headers);
                ResponseEntity<String> responseEntity = restTemplate.exchange(URL + "/save", HttpMethod.POST,entity,String.class);
                strJson = responseEntity.getBody();
                if(responseEntity.getStatusCode()== HttpStatus.OK){
                    ErrorMessage errorMessage = gson.fromJson(strJson,ErrorMessage.class);
                    if(errorMessage.getErrorCode()==ErrorCode.SUCCESS){
                        TypeToken<Country> token = new TypeToken<Country>() {};
                        country = gson.fromJson(errorMessage.getContent(),token.getType());
                        model.addAttribute("errorMessage","Create Country success with country id="+country.getId());
                    }else{
                        model.addAttribute("errorMessage",errorMessage.getContent());
                    }
                }
            }
        }catch (Exception ex){
            model.addAttribute("errorMessage",ex.getMessage());
        }
        return "/country/create";
    }

   @RequestMapping(value="/edit",method=RequestMethod.GET)
   public String getEditRoot(Model model){
       model.addAttribute("errorMessage","Forbiden");
       return "/country/edit";
   }

    @RequestMapping(value="/edit/{strId}",method= RequestMethod.GET)
    public String getEdit(Model model,@PathVariable String strId){
        try{

            Integer id = Integer.parseInt(strId);
            RestTemplate restTemplate = new RestTemplate();
            String strJson;
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL + "/fetch/" + strId, HttpMethod.GET,null,String.class);
            strJson = responseEntity.getBody();
            if(responseEntity.getStatusCode()== HttpStatus.OK){
                ErrorMessage errorMessage = gson.fromJson(strJson,ErrorMessage.class);
                if(errorMessage.getErrorCode()==ErrorCode.SUCCESS){
                    TypeToken<Country> token = new TypeToken<Country>() {};
                    Country country = gson.fromJson(errorMessage.getContent(),token.getType());

                    model.addAttribute("errorMessage","NONE");
                    model.addAttribute("country",country);
                }else{
                    model.addAttribute("errorMessage",errorMessage.getContent());
                }
            }
        }
        catch (Exception ex){
            model.addAttribute("errorMessage",ex.getMessage());
        }
        return "/country/edit";
    }

    @RequestMapping(value="/edit/{strId}",method=RequestMethod.POST)
    public String postEdit(@PathVariable String strId,@RequestParam("txtName") String strName, @RequestParam("txtCode") String strCode, Model model){
        RestTemplate restTemplate = new RestTemplate();
        String strJson;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            if(strName.isEmpty() || strName==null){
                model.addAttribute("errorMessage","Country Name is not Empty");
            }
            else if (strCode.isEmpty()||strCode==null){
                model.addAttribute("errorMessage","Country Code is not Empty");
            }
            Integer id = Integer.parseInt(strId);
            Country country = new Country(id, strName,strCode);
            HttpEntity entity = new HttpEntity(country,headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL + "/save", HttpMethod.POST,entity,String.class);
            strJson = responseEntity.getBody();
            if(responseEntity.getStatusCode()== HttpStatus.OK){
                ErrorMessage errorMessage = gson.fromJson(strJson,ErrorMessage.class);
                if(errorMessage.getErrorCode()==ErrorCode.SUCCESS){
                    TypeToken<Country> token = new TypeToken<Country>() {};
                    country = gson.fromJson(errorMessage.getContent(),token.getType());
                    model.addAttribute("country",country);
                    model.addAttribute("errorMessage","Update Country Success");
                }else{
                    model.addAttribute("errorMessage",errorMessage.getContent());
                }
            }
        }catch (Exception ex){
            //strJson = responseEntity.getBody();
            model.addAttribute("errorMessage",ex.getMessage());
        }
        return "/country/edit";
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
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL + "/delete/"+strId, HttpMethod.GET,null,String.class);
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
