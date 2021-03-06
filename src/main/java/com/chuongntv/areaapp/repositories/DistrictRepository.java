package com.chuongntv.areaapp.repositories;

import com.chuongntv.areaapp.models.City;
import com.chuongntv.areaapp.models.District;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by chuongntv on 12/17/15.
 */
public interface DistrictRepository extends CrudRepository<District,Integer>{
    public List<District> findByCode(String name);
    public List<District> findByCity(City city);
    public Page<District> findByCity(City city, Pageable pageable);
}
