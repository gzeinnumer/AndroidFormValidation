package com.gzeinnumer.afv.constant;

public class BaseMessage {
    public String empty = "Required*";
    public String format = "Wrong Format";

    public BaseMessage() {
    }

    public BaseMessage(String empty, String format) {
        this.empty = empty;
        this.format = format;
    }

    public String getEmpty() {
        return empty;
    }

    public void setEmpty(String empty) {
        this.empty = empty;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
