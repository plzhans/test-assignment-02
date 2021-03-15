package com.plzhans.assignment.api.auth;

import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * The type Auth user resolver.
 */
public class AuthRoomResolver implements HandlerMethodArgumentResolver {

    /**
     * The constant USER_ID_HEADER.
     */
    public static String USER_ID_HEADER = "X-USER-ID";
    /**
     * The constant ROOM_ID_HEADER.
     */
    public static String ROOM_ID_HEADER = "X-ROOM-ID";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(AuthRoomIdentity.class) != null;

        boolean isUserClass = AuthRoomRequester.class.equals(parameter.getParameterType());

        return isLoginUserAnnotation && isUserClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        int userId = Integer.valueOf(webRequest.getHeader(USER_ID_HEADER));
        if (StringUtils.isEmpty(userId)) {
            throw new Exception(String.format("%s header notfound.", USER_ID_HEADER));
        }

        int roomId = Integer.valueOf(webRequest.getHeader(ROOM_ID_HEADER));
        if (StringUtils.isEmpty(roomId)) {
            throw new Exception(String.format("%s header notfound.", ROOM_ID_HEADER));
        }

        return new AuthRoomRequester(userId, roomId);
    }
}
