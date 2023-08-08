package movierankchart.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// WebMvcConfigurer: Spring Web MVC 설정 관련 클래스
// CorsRegistry: CORS 설정 관련 클래스
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // CORS 설정을 적용할 URL 패턴
                .allowedOrigins("http://localhost:5173")  // CORS 허용하는 Origin
                .allowedMethods("GET", "POST")  // CORS 허용하는 HTTP Method
                .allowCredentials(true);  // 쿠키가 포함된 요청 허용
    }
}