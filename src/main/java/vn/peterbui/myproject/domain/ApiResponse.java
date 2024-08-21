package vn.peterbui.myproject.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) // cái nào null thì sẽ không kèm vào
public class ApiResponse <T>{
    private int statusCode;
    private Object message;
    private String error;
    private T data;
    
    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public Object getMessage() {
        return message;
    }
    public void setMessage(Object message) {
        this.message = message;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }

    

}
