package vn.peterbui.myproject.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import vn.peterbui.myproject.domain.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(value = UserDoesNotExist.class)
    ResponseEntity<ApiResponse> handlingUserDoesNotEx (UserDoesNotExist userDoesNotExist){
        ApiResponse apiResponse = new ApiResponse<>();

        apiResponse.setCode(1001);
        apiResponse.setMessage(userDoesNotExist.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<String> handlingValidation(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body(exception.getFieldError().getDefaultMessage());
    }


}
