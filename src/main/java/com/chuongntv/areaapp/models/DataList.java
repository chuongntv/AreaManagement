package com.chuongntv.areaapp.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by chuongntv on 12/22/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataList {
    private List<?> listObject;

    public DataList(List<?> listObject) {
        this.listObject = listObject;
    }

    public List<?> getListObject() {
        return listObject;
    }

    public void setListObject(List<?> listObject) {
        this.listObject = listObject;
    }
}
