package com.chuongntv.areaapp.config;

import com.chuongntv.areaapp.models.Country;
import com.chuongntv.areaapp.repositories.CountryRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Created by pm03 on 12/22/15.
 */
@Component
public class DatabaseForTest {
    @Autowired
    private CountryRepository countryRepository;

    public void createDataCountry(){
        ArrayList<Country> countries = new ArrayList<Country>();
        countries.add(new Country("vietnam","vi"));
        countries.add(new Country("thailands","th"));
        countries.add(new Country("china","cn"));
        countries.add(new Country("laos","lo"));
        countries.add(new Country("america","us"));
        for (Country country: countries
             ) {
            countryRepository.save(country);
        }
    }
}
