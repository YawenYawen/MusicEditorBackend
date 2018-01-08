package com.example.demo.dtos;

public class JsonResult {

    private Boolean ok = false;

    private String message;

    private Object object;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "JsonResult{" +
                "ok=" + ok +
                ", message='" + message + '\'' +
                ", object=" + object +
                '}';
    }
}
