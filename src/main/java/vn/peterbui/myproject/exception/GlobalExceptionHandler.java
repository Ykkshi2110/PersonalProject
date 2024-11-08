package vn.peterbui.myproject.exception;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import vn.peterbui.myproject.domain.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = { UsernameNotFoundException.class, BadCredentialsException.class,
            IdInvalidException.class, PermissionAttributeExists.class })
    public ResponseEntity<ApiResponse<Object>> handleIdException(Exception e) {
        ApiResponse<Object> res = new ApiResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(e.getMessage());
        res.setMessage("Exception occurs...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = UserDoesNotExist.class)
    ResponseEntity<ApiResponse<Object>> handlingUserDoesNotEx(UserDoesNotExist userDoesNotExist) {
        ApiResponse<Object> res = new ApiResponse<>();
        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setMessage(userDoesNotExist.getMessage());
        res.setError("USER DOES NOT EXISTS!");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    @ExceptionHandler(value = UserExistedException.class)
    ResponseEntity<ApiResponse<Object>> handlingUserExistedEx(UserExistedException e) {
        ApiResponse<Object> res = new ApiResponse<>();
        res.setStatusCode(HttpStatus.CONFLICT.value());
        res.setMessage(e.getMessage());
        res.setError("USER EXISTED!");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Object>> handlingValidationEx(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();

        ApiResponse<Object> res = new ApiResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(e.getBody().getDetail());

        List<String> errors = fieldErrors.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toList());
        res.setMessage(errors.size() > 1 ? errors : errors.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = {NoResourceFoundException.class})
    public ResponseEntity<ApiResponse<Object>> handleNoResourceFoundException(NoResourceFoundException e) {
        ApiResponse<Object> res = new ApiResponse<>();
        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setMessage(e.getMessage());
        res.setError("404 Not Found. URL may not exist...");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    // Handle exception not define
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllException(Exception ex) {
        ApiResponse<Object> res = new ApiResponse<>();
        res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setMessage(ex.getMessage());
        res.setError("Internal Server Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }

    @ExceptionHandler(value = {StorageException.class})
    public ResponseEntity<ApiResponse<Object>> handleNoResourceFoundException(StorageException e) {
        ApiResponse<Object> res = new ApiResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessage(e.getMessage());
        res.setError("Exception upload file occurs...");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }
}
