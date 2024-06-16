package thespeace.springAdvanced.proxy.config.v3_proxyfactory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import thespeace.springAdvanced.proxy.app.v1.*;
import thespeace.springAdvanced.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import thespeace.springAdvanced.trace.logtrace.LogTrace;

/**
 * <h2>인터페이스가 있는 v1 애플리케이션에 LogTrace 기능을 프록시 팩토리를 통해서 프록시를 만들어 적용</h2>
 *
 * <ul>
 *     <li>포인트컷은 NameMatchMethodPointcut 을 사용한다. 여기에는 심플 매칭 기능이 있어서 * 을 매칭할 수 있다.
 *         <ul>
 *             <li>request* , order* , save* : request 로 시작하는 메서드에 포인트컷은 true 를 반환한다.
 *                 나머지도 같다.</li>
 *             <li>이렇게 설정한 이유는 noLog() 메서드에는 어드바이스를 적용하지 않기 위해서다.</li>
 *         </ul>
 *     </li>
 *     <li>어드바이저는 포인트컷( NameMatchMethodPointcut ), 어드바이스( LogTraceAdvice )를 가지고 있다.</li>
 *     <li>프록시 팩토리에 각각의 target 과 advisor 를 등록해서 프록시를 생성한다. 그리고 생성된 프록시를 스프링 빈으로 등록한다.</li>
 * </ul>
 *
 * @see <a href="http://localhost:8080/proxy/v1/request?itemId=hello">정상 호출</a>
 * @see <a href="http://localhost:8080/proxy/v1/request?itemId=ex">예외 호출</a>
 */
@Slf4j
@Configuration
public class ProxyFactoryConfigV1 {

    @Bean
    public OrderControllerV1 orderControllerV1(LogTrace logTrace) {
        OrderControllerV1 orderController = new OrderControllerV1Impl(orderServiceV1(logTrace));
        ProxyFactory factory = new ProxyFactory(orderController);
        factory.addAdvisor(getAdvisor(logTrace));
        OrderControllerV1 proxy = (OrderControllerV1) factory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), orderController.getClass());
        return proxy;
    }

    @Bean
    public OrderServiceV1 orderServiceV1(LogTrace logTrace) {
        OrderServiceV1 orderService = new OrderServiceV1Impl(orderRepositoryV1(logTrace));
        ProxyFactory factory = new ProxyFactory(orderService);
        factory.addAdvisor(getAdvisor(logTrace));
        OrderServiceV1 proxy = (OrderServiceV1) factory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), orderService.getClass());
        return proxy;
    }

    @Bean
    public OrderRepositoryV1 orderRepositoryV1(LogTrace logTrace) {
        OrderRepositoryV1Impl orderRepository = new OrderRepositoryV1Impl();
        ProxyFactory factory = new ProxyFactory(orderRepository);
        factory.addAdvisor(getAdvisor(logTrace));
        OrderRepositoryV1 proxy = (OrderRepositoryV1) factory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), orderRepository.getClass());
        return proxy;
    }

    private Advisor getAdvisor(LogTrace logTrace) {
        //pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");

        //advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

}
