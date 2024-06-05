package thespeace.springAdvanced.proxy.app.v2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h1>v2 - 인터페이스 없는 구체 클래스 - 스프링 빈으로 수동 등록</h1>
 * 이번에는 인터페이스가 없는 Controller , Service , Repository 를 스프링 빈으로 수동 등록해보자.
 */
@Slf4j
@RestController
public class OrderControllerV2 {

    private final OrderServiceV2 orderService;

    public OrderControllerV2(OrderServiceV2 orderService) {
        this.orderService = orderService;
    }

    /**
     * @see <a href="http://localhost:8080/proxy/v2/request?itemId=hello">정상호출</a>
     * @see <a href="http://localhost:8080/proxy/v2/request?itemId=ex">예외호출</a>
     */
    @GetMapping("/proxy/v2/request")
    public String request(String itemId) {
        orderService.orderItem(itemId);
        return "ok";
    }

    @GetMapping("/proxy/v2/no-log")
    public String noLog() {
        return "ok";
    }
}
