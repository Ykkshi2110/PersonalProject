package vn.peterbui.myproject.exception;

public class PermissionAttributeExists extends RuntimeException{
    public PermissionAttributeExists(String message){
        super(message);
    }
}
