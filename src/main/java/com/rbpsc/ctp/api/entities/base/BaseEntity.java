package com.rbpsc.ctp.api.entities.base;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

public class BaseEntity<T> {

    @Id
    private T id;

    private String idFieldName;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public String getIdFieldName() {
        return idFieldName;
    }

    public void setIdFieldName(String idFieldName) {
        this.idFieldName = idFieldName;
    }
}
