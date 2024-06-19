package thespeace.springAdvanced.proxy.config.v6_aop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import thespeace.springAdvanced.proxy.config.AppV1Config;
import thespeace.springAdvanced.proxy.config.AppV2Config;
import thespeace.springAdvanced.proxy.config.v6_aop.aspect.LogTraceAspect;
import thespeace.springAdvanced.trace.logtrace.LogTrace;

@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AopConfig {

    /**
     * @Bean logTraceAspect() : @Aspect 가 있어도 스프링 빈으로 등록을 해줘야 한다.<br>
     * 물론 LogTraceAspect 에 @Component 애노테이션을 붙여서 컴포넌트 스캔을 사용해서 스프링 빈으로 등록해도 된다.
     */
    @Bean
    public LogTraceAspect logTraceAspect(LogTrace logTrace) {
        return new LogTraceAspect(logTrace);
    }
}
