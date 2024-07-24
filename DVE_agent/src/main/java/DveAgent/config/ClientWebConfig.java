package DveAgent.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import DveAgent.module.auth.interceptor.SignatureInterceptor;

@Configuration
public class ClientWebConfig implements WebMvcConfigurer {

    @Autowired
    private SignatureInterceptor sInterceptor;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(sInterceptor)
                .addPathPatterns("/test/message");
    }
}
