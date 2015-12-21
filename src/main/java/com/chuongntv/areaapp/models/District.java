package com.chuongntv.areaapp.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by chuongntv on 12/17/15.
 */
@Entity
@Table(name="districts")
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private String code;

    @ManyToOne
    private City city;

    public District() {
        super();
    }

    public District(Integer id){
        super();
        this.id=id;
    }

    public District(String name, String code, City city) {
        super();
        this.name = name;
        this.code = code;
        this.city = city;
    }

    public District(Integer id, String name, String code, City city) {
        super();
        this.id = id;
        this.name = name;
        this.code = code;
        this.city = city;
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
