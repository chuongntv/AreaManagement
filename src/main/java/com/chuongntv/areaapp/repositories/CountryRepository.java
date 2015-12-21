package com.chuongntv.areaapp.repositories;

import com.chuongntv.areaapp.models.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by chuongntv on 12/17/15.
 */
public interface CountryRepository extends CrudRepository<Country, Integer> {
    public List<Country> findByCode(String code);
    public Page<Country> findAll(Pageable pageable);
}