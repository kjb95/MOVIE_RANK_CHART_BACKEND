package movierankchart.common.aspect;


import lombok.extern.slf4j.Slf4j;
import movierankchart.common.exception.ErrorResponse;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class AspectAdvice {
    @Before("execution(* movierankchart.domain.*.controller..*(..))")
    public void logBeforeController() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        log.info("{} {}", request.getMethod(), request.getRequestURI());
    }

    @AfterReturning(value = "execution(* movierankchart.common.exception.GlobalControllerAdvice..*(..))", returning = "response")
    public void logBeforeControllerAdvice(ResponseEntity<ErrorResponse> response) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        log.error("{} {}", request.getMethod(), request.getRequestURI());
        log.error("{}", response.getBody());
    }
}
