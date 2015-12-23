package com.chuongntv.areaapp.controllers;

import com.chuongntv.areaapp.models.City;
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
@RequestMapping("/api/district")
@RestController
public class DistrictApi {
    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private DistrictRepository districtRepository;
    @Value("${areamanagement.itemperpage}")
    private String itemPerPage;

    @RequestMapping("/fetch/{id}")
    public ResponseEntity<?> fetchDistrictById(@PathVariable Long id) {
        try {
            District district = districtRepository.findOne(id);
            if (district.equals(null))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            district.setCity(null);
            return new ResponseEntity<>(district, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping("/fetch/city/{cityId}/page/{pageNumber}")
    public ResponseEntity<?> fetchDistrictByCityAndPage(@PathVariable Long cityId, @PathVariable Integer pageNumber) {
        try {
            City city = cityRepository.findOne(cityId);
            if (city.equals(null)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (pageNumber > 0) {
                List<District> lstDistricts = districtRepository.findByCity(city, new PageRequest(pageNumber - 1, Integer.parseInt(itemPerPage))).getContent();
                for (District district : lstDistricts) {
                    district.setCity(null);
                }
                return new ResponseEntity<>(new DataList(lstDistricts), HttpStatus.OK);
            } else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseEntity<?> saveDistrict(@Valid @RequestBody District district) {
        try {
            if (district.getCity().equals(null))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            if (!district.getId().equals(null)) {
                List<District> lstDistrict = districtRepository.findByCode(district.getCode());
                if (lstDistrict.size() > 0 && !lstDistrict.get(0).getId().equals(district.getId()))
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                if (cityRepository.findByCode(district.getCity().getCode()).equals(null))
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                District tmp = districtRepository.findOne(district.getId());
                List<City> lstCities = cityRepository.findByCountry(tmp.getCity().getCountry());
                boolean isOkay = false;
                for (City city : lstCities) {
                    if (city.getId().equals(district.getCity().getId())) {
                        isOkay = true;
                    }
                }
                if (!isOkay) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            } else {
                if (districtRepository.findByCode(district.getCode()).size() > 0)
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            district.setCity(null);
            districtRepository.save(district);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteDistrict(@PathVariable Long id) {
        try {
            District dt = districtRepository.findOne(id);
            if (dt.equals(null))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            districtRepository.delete(dt);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
