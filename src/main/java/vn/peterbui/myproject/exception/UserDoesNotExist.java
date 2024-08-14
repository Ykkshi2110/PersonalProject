package vn.peterbui.myproject.exception;

public class UserDoesNotExist extends RuntimeException {
    public UserDoesNotExist (String message){
        super(message);
    }
    
}
