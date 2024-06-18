package thespeace.springAdvanced.proxy.config.v5_autoproxy;

import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import thespeace.springAdvanced.proxy.config.AppV1Config;
import thespeace.springAdvanced.proxy.config.AppV2Config;
import thespeace.springAdvanced.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import thespeace.springAdvanced.trace.logtrace.LogTrace;

/**
 * <ul>
 *     <li>AutoProxyConfig 코드를 보면 advisor1 이라는 어드바이저 하나만 등록했다.</li>
 *     <li>빈 후처리기는 이제 등록하지 않아도 된다. 스프링은 자동 프록시 생성기라는
 *         ( AnnotationAwareAspectJAutoProxyCreator ) 빈 후처리기를 자동으로 등록해준다.</li>
 * </ul>
 */
@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AutoProxyConfig {

    @Bean
    public Advisor advisor1(LogTrace logTrace) {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);

        //advisor = pointcut + advice
        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
