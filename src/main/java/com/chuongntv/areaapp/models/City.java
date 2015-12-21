package com.chuongntv.areaapp.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by chuongntv on 12/17/15.
 */
@Entity
@Table(name="cities")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
   /* @NotNull
    private Integer countryId;*/
    @NotNull
    private String name;

    @NotNull
    private String code;

    @ManyToOne
    private Country country;

    @OneToMany(targetEntity=District.class)
    private List<District> districts;

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    public City() {
        super();
    }
    public City(Integer id){
        super();
        this.id=id;
    }

    public City(Integer id, String name, String code) {
        super();
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public City(Integer id,String name, String code, Country country) {
        super();
        this.id = id;
        this.name = name;
        this.code = code;
        this.country = country;
    }

    public City(String name, String code, Country country) {
        super();
        this.name = name;
        this.code = code;
        this.country = country;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
