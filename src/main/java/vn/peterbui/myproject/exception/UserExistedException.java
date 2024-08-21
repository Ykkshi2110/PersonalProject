package vn.peterbui.myproject.exception;

public class UserExistedException extends RuntimeException{
    public UserExistedException (String message){
        super(message);
    }
}
