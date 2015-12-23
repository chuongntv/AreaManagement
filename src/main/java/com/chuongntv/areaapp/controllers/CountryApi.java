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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by chuongntv on 12/22/15.
 */
@RequestMapping("/api/country")
@RestController
public class CountryApi {
    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private DistrictRepository districtRepository;
    @Value("${areamanagement.itemperpage}")
    private String itemPerPage;

    @RequestMapping("/fetch/{id}")
    public ResponseEntity<?> fetchCountryById(@PathVariable Long id) {
        try {
            Country country = countryRepository.findOne(id);
            if (country==null)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(country, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping("/fetch/page/{pageNumber}")
    public ResponseEntity<?> fetchCountryByPage(@PathVariable Integer pageNumber) {
        try {
            if (pageNumber > 0) {
                Page<Country> pageCountries = countryRepository.findAll(new PageRequest(pageNumber-1, Integer.parseInt(itemPerPage)));
                List<Country> lstCountries = pageCountries.getContent();
                return new ResponseEntity<>(new DataList(lstCountries), HttpStatus.OK);
            } else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<?> saveCountry(@Valid @RequestBody Country country) {
        try {
            if (country.getId()!=null) {
                List<Country> lstContries = countryRepository.findByCode(country.getCode());
                if (lstContries.size() > 0 && lstContries.get(0).getId().equals(country.getId()))
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                List<Country> tmp = countryRepository.findByCode(country.getCode());
                if (tmp.size() > 0)
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            countryRepository.save(country);
            return new ResponseEntity<>(country, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> deleteCountry(@PathVariable Long id) {
        try {
            Country ct = countryRepository.findOne(id);
            if (ct==null)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            List<City> lstCities = cityRepository.findByCountry(ct);
            for (City c : lstCities) {
                List<District> lstDistricts = districtRepository.findByCity(c);
                for (District d : lstDistricts) {
                    districtRepository.delete(d);
                }
                cityRepository.delete(c);
            }
            countryRepository.delete(ct);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
