package vn.peterbui.myproject.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import vn.peterbui.myproject.convert.SecurityUtil;
import vn.peterbui.myproject.domain.Permission;
import vn.peterbui.myproject.domain.Role;
import vn.peterbui.myproject.domain.User;
import vn.peterbui.myproject.service.UserService;

@Transactional
@RequiredArgsConstructor
public class PermissionInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(PermissionInterceptor.class);

    @Autowired UserService userService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        logger.info(">>> RUN preHandle");
        logger.info(">>> path= {}", path);
        logger.info(">>> httpMethod= {}", httpMethod);
        logger.info(">>> requestURI= {}", requestURI);

        // check permission
        String email = SecurityUtil.getCurrentUserLogin().orElse("");
        if (email != null && !email.isEmpty()) {
            User user = this.userService.handleGetUserByUserName(email);
            if (user != null) {
                Role role = user.getRole();
                if (role != null) {
                    List<Permission> permission = role.getPermissions();
                    boolean isAllow = permission.stream()
                            .anyMatch(item -> item.getApiPath().equals(path) && item.getMethod().equals(httpMethod));
                    logger.info(">>> isAllow: {}", isAllow);
                }
            }
        }
        return true;
    }
}
