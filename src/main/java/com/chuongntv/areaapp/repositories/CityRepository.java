package com.chuongntv.areaapp.repositories;

import com.chuongntv.areaapp.models.City;
import com.chuongntv.areaapp.models.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by chuongntv on 12/17/15.
 */
public interface CityRepository extends CrudRepository<City,Integer>{
    public List<City> findByCode(String code);
    public List<City> findByCountry(Country country);
    public Page<City> findByCountry(Country country, Pageable pageable);
}
