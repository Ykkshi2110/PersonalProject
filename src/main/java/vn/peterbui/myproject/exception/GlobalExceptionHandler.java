package vn.peterbui.myproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import vn.peterbui.myproject.domain.ApiResponse;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = UserDoesNotExist.class)
    ResponseEntity<ApiResponse<Object>> handlingUserDoesNotEx (UserDoesNotExist userDoesNotExist){
        ApiResponse<Object> res = new ApiResponse<>();
        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setMessage(userDoesNotExist.getMessage());
        res.setError("USER DOES NOT EXISTS!");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    @ExceptionHandler(value = UserExistedException.class)
    ResponseEntity<ApiResponse<Object>> handlingUserExistedEx (UserExistedException e){
        ApiResponse<Object> res = new ApiResponse<>();
        res.setStatusCode(HttpStatus.CONFLICT.value());
        res.setMessage(e.getMessage());
        res.setError("USER EXISTED!");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Object>> handlingValidationEx(MethodArgumentNotValidException e) {
       ApiResponse<Object> res = new ApiResponse<>();
       res.setStatusCode(HttpStatus.BAD_REQUEST.value());
       res.setMessage(e.getFieldError().getDefaultMessage());
       res.setError("BAD REQUEST!");
       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }


}
