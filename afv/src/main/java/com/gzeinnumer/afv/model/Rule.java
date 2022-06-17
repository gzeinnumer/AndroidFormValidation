package com.gzeinnumer.afv.model;

import com.gzeinnumer.afv.constant.TypeForm;

public class Rule {
    private TypeForm typeForm;
    private int minLength = 1;
    private String errorEmpty = "Required*";
    private String errorFormat = "Wrong Format";
    private String permitedSymbol;

    public Rule(TypeForm typeForm) {
        this.typeForm = typeForm;
    }

    public Rule(TypeForm typeForm, int minLength) {
        this.typeForm = typeForm;
        this.minLength = minLength;
    }

    public Rule(TypeForm typeForm, int minLength, String errorEmpty) {
        this.typeForm = typeForm;
        this.minLength = minLength;
        this.errorEmpty = errorEmpty;
    }

    public Rule(TypeForm typeForm, int minLength, String errorEmpty, String errorFormat) {
        this.typeForm = typeForm;
        this.minLength = minLength;
        this.errorEmpty = errorEmpty;
        this.errorFormat = errorFormat;
    }

    public Rule(TypeForm typeForm, int minLength, String errorEmpty, String errorFormat, String permitedSymbol) {
        this.typeForm = typeForm;
        this.minLength = minLength;
        this.errorEmpty = errorEmpty;
        this.errorFormat = errorFormat;
        this.permitedSymbol = permitedSymbol;
    }

    public String getErrorEmpty() {
        return errorEmpty;
    }

    public void setErrorEmpty(String errorEmpty) {
        this.errorEmpty = errorEmpty;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public TypeForm getTypeForm() {
        return typeForm;
    }

    public void setTypeForm(TypeForm typeForm) {
        this.typeForm = typeForm;
    }

    public String getErrorFormat() {
        return errorFormat;
    }

    public void setErrorFormat(String errorFormat) {
        this.errorFormat = errorFormat;
    }

    public String getPermitedSymbol() {
        return permitedSymbol;
    }

    public void setPermitedSymbol(String permitedSymbol) {
        this.permitedSymbol = permitedSymbol;
    }
}
