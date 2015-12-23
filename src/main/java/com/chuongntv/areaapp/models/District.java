package com.chuongntv.areaapp.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by chuongntv on 12/17/15.
 */
@Entity
@Table(name="districts")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String code;

    @ManyToOne
    private City city;

    public District() {
        super();
    }

    public District(Long id) {
        super();
        this.id=id;
    }

    public District(String name, String code, City city) {
        super();
        this.name = name;
        this.code = code;
        this.city = city;
    }

    public District(Long id, String name, String code, City city) {
        super();
        this.id = id;
        this.name = name;
        this.code = code;
        this.city = city;
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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
