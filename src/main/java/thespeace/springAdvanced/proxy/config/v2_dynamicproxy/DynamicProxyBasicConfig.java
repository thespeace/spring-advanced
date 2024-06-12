package thespeace.springAdvanced.proxy.config.v2_dynamicproxy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import thespeace.springAdvanced.proxy.app.v1.*;
import thespeace.springAdvanced.proxy.config.v2_dynamicproxy.handler.LogTraceBasicHandler;
import thespeace.springAdvanced.trace.logtrace.LogTrace;

import java.lang.reflect.Proxy;

/**
 * <h1>동적 프록시를 사용하도록 수동 빈 등록을 설정</h1>
 * <ul>
 *     <li>이전에는 프록시 클래스를 직접 개발했지만, 이제는 JDK 동적 프록시 기술을 사용해서
 *         각각의 Controller ,Service , Repository 에 맞는 동적 프록시를 생성해주면 된다.</li>
 *     <li>LogTraceBasicHandler : 동적 프록시를 만들더라도 LogTrace 를 출력하는 로직은
 *         모두 같기 때문에 프록시는 모두 LogTraceBasicHandler 를 사용한다.</li>
 * </ul>
 *
 * @see <a href="http://localhost:8080/proxy/v1/request?itemId=hello">정상 호출</a>
 * @see <a href="http://localhost:8080/proxy/v1/request?itemId=ex">예외 호출</a>
 * @see /docs/17.Dynamic_proxy_technology-JDK_dynamic_proxy-apply1.md
 */
@Configuration
public class DynamicProxyBasicConfig {

    @Bean
    public OrderControllerV1 orderControllerV1(LogTrace logTrace) {
        OrderControllerV1 orderController = new
                OrderControllerV1Impl(orderServiceV1(logTrace));
        OrderControllerV1 proxy = (OrderControllerV1)
                Proxy.newProxyInstance(OrderControllerV1.class.getClassLoader(),
                        new Class[]{OrderControllerV1.class},
                        new LogTraceBasicHandler(orderController, logTrace)
                );
        return proxy;
    }

    @Bean
    public OrderServiceV1 orderServiceV1(LogTrace logTrace) {
        OrderServiceV1 orderService = new OrderServiceV1Impl(orderRepositoryV1(logTrace));
        OrderServiceV1 proxy = (OrderServiceV1)
                Proxy.newProxyInstance(OrderServiceV1.class.getClassLoader(),
                        new Class[]{OrderServiceV1.class},
                        new LogTraceBasicHandler(orderService, logTrace)
                );
        return proxy;
    }

    @Bean
    public OrderRepositoryV1 orderRepositoryV1(LogTrace logTrace) {
        OrderRepositoryV1 orderRepository = new OrderRepositoryV1Impl();
        OrderRepositoryV1 proxy = (OrderRepositoryV1) Proxy.newProxyInstance(OrderRepositoryV1.class.getClassLoader(),
                new Class[]{OrderRepositoryV1.class},
                new LogTraceBasicHandler(orderRepository, logTrace));
        return proxy;
    }
}
