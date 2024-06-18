package thespeace.springAdvanced.proxy.config.v5_autoproxy;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
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

    //@Bean
    public Advisor advisor1(LogTrace logTrace) {
        //pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");

        //advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    /**
     * 주의 : advisor1 에 있는 @Bean 은 꼭 주석처리해주어야 한다. 그렇지 않으면 어드바이저가 중복 등록된다.
     *
     * <ul>
     *     <li>AspectJExpressionPointcut : AspectJ 포인트컷 표현식을 적용할 수 있다.</li>
     *     <li>execution(* thespeace.springAdvanced.proxy.app..*(..)) : AspectJ가 제공하는 포인트컷 표현식이다.
     *         <ul>
     *             <li>* : 모든 반환 타입</li>
     *             <li>thespeace.springAdvanced.proxy.app.. : 해당 패키지와 그 하위 패키지</li>
     *             <li>*(..) : * 모든 메서드 이름, (..) 파라미터는 상관 없음</li>
     *         </ul>
     *     </li>
     * </ul>
     * 쉽게 이야기해서 thespeace.springAdvanced.proxy.app 패키지와 그 하위 패키지의 모든 메서드는 포인트컷의 매칭 대상이 된다.
     *
     * @see <a href="http://localhost:8080/proxy/v1/request?itemId=hello">v1 호출</a>
     * @see <a href="http://localhost:8080/proxy/v2/request?itemId=hello">v2 호출</a>
     * @see <a href="http://localhost:8080/proxy/v3/request?itemId=hello">v3 호출</a>
     * @see <a href="http://localhost:8080/proxy/v1/no-log">no-log 호출(로그가 출력되면 안됨)</a>
     */
    //@Bean
    public Advisor advisor2(LogTrace logTrace) {
        //pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* thespeace.springAdvanced.proxy.app..*(..))");

        //advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    /**
     * advisor2 에서는 단순히 package 를 기준으로 포인트컷 매칭을 했기 때문에 no-log 요청에도 로그가 출력되었다.<br>
     * 이를 개선하기 위해 표현식을 수정하였더니 noLog()메서드는 제외하여 로그가 남지 않는다.
     */
    @Bean
    public Advisor advisor3(LogTrace logTrace) {
        //pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* thespeace.springAdvanced.proxy.app..*(..)) && !execution(* thespeace.springAdvanced.proxy.app..noLog(..))");

        //advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
