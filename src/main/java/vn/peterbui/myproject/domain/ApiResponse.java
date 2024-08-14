package vn.peterbui.myproject.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) // cái nào null thì sẽ không kèm vào
public class ApiResponse <T>{
    private int code;
    private String message;
    private T result;

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public T getResult() {
        return result;
    }
    public void setResult(T result) {
        this.result = result;
    }

    

}
