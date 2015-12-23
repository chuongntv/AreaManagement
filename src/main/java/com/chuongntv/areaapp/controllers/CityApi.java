package com.chuongntv.areaapp.controllers;

import com.chuongntv.areaapp.models.City;
import com.chuongntv.areaapp.models.Country;
import com.chuongntv.areaapp.models.DataList;
import com.chuongntv.areaapp.models.District;
import com.chuongntv.areaapp.repositories.CityRepository;
import com.chuongntv.areaapp.repositories.CountryRepository;
import com.chuongntv.areaapp.repositories.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by chuongntv on 12/22/15.
 */
@RestController
@RequestMapping("/api/city")
public class CityApi {
    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Value("${areamanagement.itemperpage}")
    private String itemPerPage;

    @RequestMapping("/fetch/{id}")
    public ResponseEntity<?> fetchCityById(@PathVariable Long id) {
        try {
            City city = cityRepository.findOne(id);
            if (city == null)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            city.setCountry(null);
            return new ResponseEntity<>(city, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping("/fetch/country/{countryId}/page/{pageNumber}")
    public ResponseEntity<?> fetchCityByCountryAndPage(@PathVariable Long countryId, @PathVariable Integer pageNumber) {
        try {
            Country country = countryRepository.findOne(countryId);
            if (country.equals(null)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (pageNumber > 0) {
                List<City> lstCities = cityRepository.findByCountry(country, new PageRequest(pageNumber - 1, Integer.parseInt(itemPerPage))).getContent();
                for (City city : lstCities) {
                    city.setCountry(null);
                }
                return new ResponseEntity<>(new DataList(lstCities), HttpStatus.OK);
            } else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<?> saveCity(@Valid @RequestBody City city) {
        try {
            if (city.getCountry().equals(null))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            if (!city.getId().equals(null)) {
                List<City> lstCities = cityRepository.findByCode(city.getCode());
                if (lstCities.size() > 0 && !lstCities.get(0).getId().equals(city.getId()))
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                if (countryRepository.findOne(city.getCountry().getId()).equals(null))
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                if (cityRepository.findByCode(city.getCode()).size() > 0)
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            city.setCountry(null);
            cityRepository.save(city);
            return new ResponseEntity<>(city, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteCity(@PathVariable Long id) {
        try {
            City ct = cityRepository.findOne(id);
            if (ct.equals(null))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            List<District> lstDistricts = districtRepository.findByCity(ct);
            for (District d : lstDistricts)
                districtRepository.delete(d);
            cityRepository.delete(ct);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
