package vn.peterbui.myproject.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.peterbui.myproject.convert.SecurityUtil;
import vn.peterbui.myproject.convert.annotation.ApiMessage;
import vn.peterbui.myproject.domain.User;
import vn.peterbui.myproject.domain.dto.LoginDTO;
import vn.peterbui.myproject.domain.dto.ResLoginDTO;
import vn.peterbui.myproject.exception.IdInvalidException;
import vn.peterbui.myproject.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    @Value("${peterBui.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO)
            throws MethodArgumentNotValidException {

        // Nạp input gồm username, password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword());

        // xác thực người dùng => cần viết hàm loadByUserName()
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // set thông tin người dùng đăng nhập vào context (có thể sử dụng sau này)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO resLoginDTO = new ResLoginDTO();
        User currentUserDB = this.userService.handleGetUserByUserName(loginDTO.getUsername());
        if (currentUserDB != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(currentUserDB.getId(), currentUserDB.getEmail(),
                    currentUserDB.getFullName());
            resLoginDTO.setUserLogin(userLogin);
        }

        // create token
        String accessToken = this.securityUtil.createAccessToken(authentication.getName(), resLoginDTO);
        resLoginDTO.setAccessToken(accessToken);

        // create refresh token
        String refreshToken = this.securityUtil.createRefreshToken(loginDTO.getUsername(), resLoginDTO);

        // update user
        this.userService.updateUserToken(refreshToken, loginDTO.getUsername());

        // set cookie
        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(resLoginDTO);
    }

    @GetMapping("/auth/account")
    @ApiMessage("fetch account")
    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

        User currentUserDB = this.userService.handleGetUserByUserName(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();

        if (currentUserDB != null) {
            userLogin.setId(currentUserDB.getId());
            userLogin.setEmail(currentUserDB.getEmail());
            userLogin.setName(currentUserDB.getFullName());
            userGetAccount.setUser(userLogin);
        }

        return ResponseEntity.ok().body(userGetAccount);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Get user by refresh token")
    public ResponseEntity<ResLoginDTO> getRefreshToken(@CookieValue(name = "refresh_token") String refreshToken) {
        // check valid
        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refreshToken);
        String email = decodedToken.getSubject();

        // check user by token + email
        User currentUser = this.userService.getUserRefreshTokenAndEmail(refreshToken, email);

        if (currentUser == null) {
            throw new IdInvalidException("Refresh Token không hợp lệ");
        }

        // issue new token/set refresh token as cookie
        ResLoginDTO resLoginDTO = new ResLoginDTO();
        User currentUserDB = this.userService.handleGetUserByUserName(email);
        if (currentUserDB != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(currentUserDB.getId(), currentUserDB.getEmail(),
                    currentUserDB.getFullName());
            resLoginDTO.setUserLogin(userLogin);
        }

        // create token
        String accessToken = this.securityUtil.createAccessToken(email, resLoginDTO);

        resLoginDTO.setAccessToken(accessToken);

        // create refresh token
        String newRefreshToken = this.securityUtil.createRefreshToken(email, resLoginDTO);

        // update user
        this.userService.updateUserToken(newRefreshToken, email);

        // set cookie
        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(resLoginDTO);
    }

    @PostMapping("/auth/logout") 
    @ApiMessage("Logout User")
    public ResponseEntity<Void> logout() throws IdInvalidException{
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        if(email.equals("")) throw new IdInvalidException("Access Token không hợp lệ");

        // update token bằng null
        this.userService.updateUserToken(null, email);

        // remove refresh token cookie 
        ResponseCookie deleteSpringCookie = ResponseCookie
            .from("refresh_token", null)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(0)
            .build();

            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString()).body(null);
    }
}
