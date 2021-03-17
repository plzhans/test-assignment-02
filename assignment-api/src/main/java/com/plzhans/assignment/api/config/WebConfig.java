package com.plzhans.assignment.api.config;

import com.plzhans.assignment.api.auth.AuthRoomResolver;
import com.plzhans.assignment.api.service.common.datatype.ErrorCode;
import com.plzhans.assignment.api.service.spread.datatype.DistributeReceiveResultCode;
import com.plzhans.assignment.common.config.CodeEnumableFormatters;
import com.plzhans.assignment.common.domain.spread.SpreadState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.List;

/**
 * The type Web config.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Json view mapping jackson 2 json view.
     *
     * @return the mapping jackson 2 json view
     */
    @Bean
    MappingJackson2JsonView jsonView() {
        return new MappingJackson2JsonView();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthRoomResolver());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        CodeEnumableFormatters.registry(registry, SpreadState.class);
        CodeEnumableFormatters.registry(registry, ErrorCode.class);
        CodeEnumableFormatters.registry(registry, DistributeReceiveResultCode.class);
    }

}
