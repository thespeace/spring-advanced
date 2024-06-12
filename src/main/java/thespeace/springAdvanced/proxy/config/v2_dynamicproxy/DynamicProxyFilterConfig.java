package thespeace.springAdvanced.proxy.config.v2_dynamicproxy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import thespeace.springAdvanced.proxy.app.v1.*;
import thespeace.springAdvanced.proxy.config.v2_dynamicproxy.handler.LogTraceFilterHandler;
import thespeace.springAdvanced.trace.logtrace.LogTrace;

import java.lang.reflect.Proxy;

/**
 * <h2>JDK 동적 프록시 - 한계</h2>
 * JDK 동적 프록시는 인터페이스가 필수이다.<br>
 * 그렇다면 V2 애플리케이션 처럼 인터페이스 없이 클래스만 있는 경우에는 어떻게 동적 프록시를 적용할 수 있을까?<br>
 * 이것은 일반적인 방법으로는 어렵고 CGLIB 라는 바이트코드를 조작하는 특별한 라이브러리를 사용해야 한다.
 *
 * @see <a href="http://localhost:8080/proxy/v1/no-log">no-log</a>
 * @see <a href="http://localhost:8080/proxy/v1/request?itemId=hello">정상 호출</a>
 */
@Configuration
public class DynamicProxyFilterConfig {

    //적용할 패턴으로 request , order , save 로 시작하는 메서드에 로그가 남는다.
    private static final String[] PATTERNS = {"request*", "order*", "save*"};

    @Bean
    public OrderControllerV1 orderControllerV1(LogTrace logTrace) {
        OrderControllerV1 orderController = new
                OrderControllerV1Impl(orderServiceV1(logTrace));
        OrderControllerV1 proxy = (OrderControllerV1)
                Proxy.newProxyInstance(OrderControllerV1.class.getClassLoader(),
                        new Class[]{OrderControllerV1.class},
                        new LogTraceFilterHandler(orderController, logTrace, PATTERNS)
                );
        return proxy;
    }

    @Bean
    public OrderServiceV1 orderServiceV1(LogTrace logTrace) {
        OrderServiceV1 orderService = new OrderServiceV1Impl(orderRepositoryV1(logTrace));
        OrderServiceV1 proxy = (OrderServiceV1)
                Proxy.newProxyInstance(OrderServiceV1.class.getClassLoader(),
                        new Class[]{OrderServiceV1.class},
                        new LogTraceFilterHandler(orderService, logTrace, PATTERNS)
                );
        return proxy;
    }

    @Bean
    public OrderRepositoryV1 orderRepositoryV1(LogTrace logTrace) {
        OrderRepositoryV1 orderRepository = new OrderRepositoryV1Impl();
        OrderRepositoryV1 proxy = (OrderRepositoryV1) Proxy.newProxyInstance(OrderRepositoryV1.class.getClassLoader(),
                new Class[]{OrderRepositoryV1.class},
                new LogTraceFilterHandler(orderRepository, logTrace, PATTERNS)
        );
        return proxy;
    }
}
