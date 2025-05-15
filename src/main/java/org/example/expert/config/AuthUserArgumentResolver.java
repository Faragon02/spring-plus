package org.example.expert.config;

import jakarta.servlet.http.HttpServletRequest;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAuthAnnotation = parameter.getParameterAnnotation(Auth.class) != null;
        boolean isAuthUserType = parameter.getParameterType().equals(AuthUser.class);

        // @Auth 어노테이션과 AuthUser 타입이 함께 사용되지 않은 경우 예외 발생
        if (hasAuthAnnotation != isAuthUserType) {
            throw new AuthException("@Auth와 AuthUser 타입은 함께 사용되어야 합니다.");
        }

        return hasAuthAnnotation;
    }

    @Override
    public Object resolveArgument(
            @Nullable MethodParameter parameter,
            @Nullable ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            @Nullable WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        // Lv2-9 Security Filter
        CustomUserDetails customUserDetails = null;
        Object pricipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         if(pricipal instanceof UserDetails){
             customUserDetails = (CustomUserDetails)pricipal;
         }
         if(customUserDetails == null){
             throw new AuthException("유저 정보가 존재하지 않습니다.");
         }
        // JwtFilter 에서 set 한 userId, email, userRole 값을 가져옴
        Long userId = customUserDetails.getId();
        String email = customUserDetails.getUsername();

        UserRole userRole =customUserDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .map(role->role.replace("ROLE_","").trim())
            .map(UserRole::valueOf)
            .findFirst()
            .orElse(null);
        return new AuthUser(userId, email, userRole);
    }
}
