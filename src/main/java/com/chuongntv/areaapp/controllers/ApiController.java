package com.chuongntv.areaapp.controllers;

import com.chuongntv.areaapp.models.*;
import com.chuongntv.areaapp.repositories.CityRepository;
import com.chuongntv.areaapp.repositories.CountryRepository;
import com.chuongntv.areaapp.repositories.DistrictRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by chuongntv on 12/17/15.
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private DistrictRepository districtRepository;

    private final int NUMBER_OF_ITEM = 1;

    private Gson gson = new Gson();

    private ObjectMapper mapper = new ObjectMapper();

    // API for Country
    @RequestMapping("/country/fetch")
    public ResponseEntity<?> fetchAllCountry() {
        List<Country> lstCountries = (List<Country>) countryRepository.findAll();
        return new ResponseEntity<>(new ErrorMessage(ErrorCode.SUCCESS, gson.toJson(lstCountries)), HttpStatus.OK);
    }

    @RequestMapping("/country/fetch/{strId}")
    public ResponseEntity<?> fetchCountryById(@PathVariable String strId) {
        try {
            Integer id = Integer.parseInt(strId);
            Country country = countryRepository.findOne(id);
            if (country == null)
                return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"Invalid Country"), HttpStatus.OK);
            return new ResponseEntity<>(new ErrorMessage(ErrorCode.SUCCESS, gson.toJson(country)), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,ex.getMessage()), HttpStatus.OK);
        }
    }

    @RequestMapping("/country/fetch/page/{strPageNumber}")
    public ResponseEntity<?> fetchCountryByPage(@PathVariable String strPageNumber) {
        try {
            Integer pageNumber = Integer.parseInt(strPageNumber);
            if (pageNumber > 0) {
                List<Country> lstCountries = countryRepository.findAll(new PageRequest(pageNumber-1,NUMBER_OF_ITEM)).getContent();
                return new ResponseEntity<>(new ErrorMessage(ErrorCode.SUCCESS, gson.toJson(lstCountries)), HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"Invalid Page Number"), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,ex.getMessage()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/country/save", method = RequestMethod.POST)
    public ResponseEntity<?> saveCountry(@RequestBody Country country) {
        try {
            if (country.getName() == null || country.getName().isEmpty())
                return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"Country Name is not Empty"), HttpStatus.OK);
            if (country.getCode() == null || country.getCode().isEmpty())
                return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"Country Code is not Empty"), HttpStatus.OK);
            if (country.getId() != null) {
                List<Country>lstContries = countryRepository.findByCode(country.getCode());
                if (lstContries.size() > 0 && lstContries.get(0).getId() != country.getId())
                    return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"Country Code is not available"), HttpStatus.OK);
            } else {
                List<Country> tmp = countryRepository.findByCode(country.getCode());
                if (tmp.size() > 0)
                    return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"Country Code is not available"), HttpStatus.OK);
            }
            countryRepository.save(country);
            return new ResponseEntity<>(new ErrorMessage(ErrorCode.SUCCESS, gson.toJson(country)), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,ex.getMessage()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/country/delete/{strId}", method = RequestMethod.GET)
    public ResponseEntity<?> deleteCountry(@PathVariable String strId) {
        try {
            Integer id = Integer.parseInt(strId);
            Country ct = countryRepository.findOne(id);
            if (ct == null)
                return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"Country is not available"), HttpStatus.OK);
            List<City> lstCities = cityRepository.findByCountry(ct);
            for(City c : lstCities){
                List<District> lstDistricts = districtRepository.findByCity(c);
                for(District d : lstDistricts){
                    districtRepository.delete(d);
                }
                cityRepository.delete(c);
            }
            countryRepository.delete(ct);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,ex.getMessage()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ErrorMessage(ErrorCode.SUCCESS,"Delete Country Success"), HttpStatus.OK);
    }

    // API for City
    @RequestMapping("/city/fetch/country/{strCountryId}")
    public ResponseEntity<?> fectchCityByCountry(@PathVariable String strCountryId) {
        try {
            Integer id = Integer.parseInt(strCountryId);
            Country country = countryRepository.findOne(id);
            if(country==null)
                return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"Invalid Country"),HttpStatus.OK);
            List<City> lstCities = (List<City>) cityRepository.findByCountry(country);
            return new ResponseEntity<>(new ErrorMessage(ErrorCode.SUCCESS,gson.toJson(lstCities)), HttpStatus.OK);
        }
        catch (Exception ex){
            return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,ex.getMessage()),HttpStatus.OK);
        }
    }

    @RequestMapping("/city/fetch/{strId}")
    public ResponseEntity<?> fetchCityById(@PathVariable String strId) {
        try {
            Integer id = Integer.parseInt(strId);
            City city = cityRepository.findOne(id);
            if (city == null)
                return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"Invalid City"), HttpStatus.OK);
            return new ResponseEntity<>(new ErrorMessage(ErrorCode.SUCCESS,gson.toJson(city)), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,ex.getMessage()), HttpStatus.OK);
        }
    }

    @RequestMapping("/city/fetch/country/{strCountryId}/page/{strPageNumber}")
    public ResponseEntity<?> fetchCityByCountryAndPage(@PathVariable String strCountryId,@PathVariable String strPageNumber) {
        try {
            Integer countryId = Integer.parseInt(strCountryId);
            Integer pageNumber = Integer.parseInt(strPageNumber);
            Country country = countryRepository.findOne(countryId);
            if(country==null){
                return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"Invalid Country"),HttpStatus.OK);
            }
            if (pageNumber > 0) {
                List<City> lstCities = cityRepository.findByCountry(country,new PageRequest(pageNumber-1, NUMBER_OF_ITEM)).getContent();
                return new ResponseEntity<>(new ErrorMessage(ErrorCode.SUCCESS, gson.toJson(lstCities)), HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"Invalid Page Number"), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,ex.getMessage()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/city/save", method = RequestMethod.POST)
    public ResponseEntity<?> saveCity(@RequestBody City city) {
        try {
            if (city.getName() == null || city.getName().isEmpty())
                return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"City Name is not Empty"), HttpStatus.OK);
            if (city.getCode() == null || city.getCode().isEmpty())
                return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"City Code is not Empty"), HttpStatus.OK);
            if (city.getCountry() == null)
                return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"Country is not Empty"), HttpStatus.OK);
            if (city.getId() != null) {
                List<City> lstCities = cityRepository.findByCode(city.getCode());
                if (lstCities.size() > 0 && lstCities.get(0).getId()!=city.getId())
                    return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"City Code is not available"), HttpStatus.OK);
                if (countryRepository.findOne(city.getCountry().getId()) == null)
                    return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"Country is not available"), HttpStatus.OK);
            } else {
                if (cityRepository.findByCode(city.getCode()).size() > 0)
                    return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"City Code is not available"), HttpStatus.OK);
            }
            cityRepository.save(city);
            return new ResponseEntity<>(new ErrorMessage(ErrorCode.SUCCESS,gson.toJson(city)), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,ex.getMessage()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/city/delete/{strId}")
    public ResponseEntity<?> deleteCity(@PathVariable String strId) {
        try {
            Integer id = Integer.parseInt(strId);
            City ct = cityRepository.findOne(id);
            if (ct == null)
                return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"City is not available"), HttpStatus.OK);
            List<District> lstDistricts = districtRepository.findByCity(ct);
            for(District d: lstDistricts)
                districtRepository.delete(d);
            cityRepository.delete(ct);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,ex.getMessage()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ErrorMessage(ErrorCode.SUCCESS,"Delete Success"), HttpStatus.OK);
    }

    // API for District
    @RequestMapping("/district/fetch/city/{strId}")
    public ResponseEntity<?> fetchAllDistrict(@PathVariable String strId) {
        try{
            Integer id = Integer.parseInt(strId);
            City city = cityRepository.findOne(id);
            if(city==null){
                return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"Invalid City"),HttpStatus.OK);
            }
            List<District> lstDistricts = (List<District>) districtRepository.findByCity(city);
            return new ResponseEntity<>(new ErrorMessage(ErrorCode.SUCCESS,gson.toJson(lstDistricts)), HttpStatus.OK);
        }
        catch (Exception ex){
            return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,ex.getMessage()),HttpStatus.OK);
        }
    }

    @RequestMapping("/district/fetch/{strId}")
    public ResponseEntity<?> fetchDistrictById(@PathVariable String strId) {
        try {
            Integer id = Integer.parseInt(strId);
            District district = districtRepository.findOne(id);
            if (district == null)
                return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"Invalid District"), HttpStatus.OK);
            return new ResponseEntity<>(new ErrorMessage(ErrorCode.SUCCESS,gson.toJson(district)), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,ex.getMessage()), HttpStatus.OK);
        }
    }


    @RequestMapping("/district/fetch/city/{strCityId}/page/{strPageNumber}")
    public ResponseEntity<?> fetchDistrictByCityAndPage(@PathVariable String strCityId,@PathVariable String strPageNumber) {
        try {
            Integer cityId = Integer.parseInt(strCityId);
            Integer pageNumber = Integer.parseInt(strPageNumber);
            City city = cityRepository.findOne(cityId);
            if(city==null){
                return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"Invalid City"),HttpStatus.OK);
            }
            if (pageNumber > 0) {
                List<District> lstDistricts = districtRepository.findByCity(city,new PageRequest(pageNumber-1,NUMBER_OF_ITEM)).getContent();
                return new ResponseEntity<>(new ErrorMessage(ErrorCode.SUCCESS, gson.toJson(lstDistricts)), HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"Invalid Page Number"), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,ex.getMessage()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/district/save", method = RequestMethod.POST)
    public ResponseEntity<?> saveDistrict(@RequestBody District district) {
        try {
            if (district.getName() == null || district.getName().isEmpty())
                return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"District Name is not Empty"), HttpStatus.OK);
            if (district.getCode() == null || district.getCode().isEmpty())
                return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"District Code is not Empty"), HttpStatus.OK);
            if (district.getCity() == null)
                return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"City Code is not Empty"), HttpStatus.OK);
            if (district.getId() != null) {
                List<District> lstDistrict = districtRepository.findByCode(district.getCode());
                if (lstDistrict.size() > 0 && lstDistrict.get(0).getId()!=district.getId())
                    return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"District Code is not available"), HttpStatus.OK);
                if (cityRepository.findByCode(district.getCity().getCode())==null)
                    return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"City is not available"), HttpStatus.OK);
                District tmp = districtRepository.findOne(district.getId());
                List<City> lstCities = cityRepository.findByCountry(tmp.getCity().getCountry());
                boolean isOkay = false;
                for(City city:lstCities){
                    if(city.getId()==district.getCity().getId()){
                        isOkay=true;
                    }
                }
                if(!isOkay){
                    return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"City is not available in this Country"), HttpStatus.OK);
                }
            } else {
                if (districtRepository.findByCode(district.getCode()).size() > 0)
                    return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"District Code is not available"), HttpStatus.OK);
            }
            districtRepository.save(district);
            return new ResponseEntity<>(new ErrorMessage(ErrorCode.SUCCESS,gson.toJson(district)), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,ex.getMessage()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/district/delete/{strId}")
    public ResponseEntity<?> deleteDistrict(@PathVariable String strId) {
        try {
            Integer id = Integer.parseInt(strId);
            District dt = districtRepository.findOne(id);
            if (dt == null)
                return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,"City is not available"), HttpStatus.OK);
            districtRepository.delete(dt);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ErrorMessage(ErrorCode.ERROR,ex.getMessage()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ErrorMessage(ErrorCode.SUCCESS,"Delete Success"), HttpStatus.OK);
    }
}
