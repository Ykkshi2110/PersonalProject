package vn.peterbui.myproject.exception;

public class PermissionDoesNotEx extends RuntimeException{
    public PermissionDoesNotEx(String message){
        super(message);
    }
}
