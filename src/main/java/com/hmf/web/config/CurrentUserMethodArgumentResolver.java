package com.hmf.web.config;

import com.hmf.web.manager.vo.CurrentLoginUser;
import com.hmf.web.manager.vo.GaCodeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(CurrentLoginUser.class)
                && parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        GaCodeVo user = (GaCodeVo) webRequest.getAttribute("currentUser", RequestAttributes.SCOPE_SESSION);
        if (user != null) {
            CurrentLoginUser currentUser = new CurrentLoginUser();
            BeanUtils.copyProperties(user,currentUser);
            return currentUser;
        }
        throw new MissingServletRequestPartException("currentUser");
    }
}
