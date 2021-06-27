package com.telerikacademy.web.deliver_it.models.dto;

import com.telerikacademy.web.deliver_it.models.contracts.IdentifyAble;

import java.util.List;


public class IdDto implements IdentifyAble {

    private int id;
    private String option;
    private List<String> options;

    public IdDto() {
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options){
        this.options = options;
    }
}
