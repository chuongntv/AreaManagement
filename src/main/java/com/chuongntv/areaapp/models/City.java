package com.chuongntv.areaapp.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by chuongntv on 12/17/15.
 */
@Entity
@Table(name="cities")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
   /* @NotNull
    private Integer countryId;*/
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String code;

    @ManyToOne
    private Country country;

    public City() {
        super();
    }

    public City(Long id) {
        super();
        this.id=id;
    }

    public City(Long id, String name, String code) {
        super();
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public City(Long id, String name, String code, Country country) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
