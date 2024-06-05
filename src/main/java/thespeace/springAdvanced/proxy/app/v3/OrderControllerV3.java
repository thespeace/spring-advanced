package thespeace.springAdvanced.proxy.app.v3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h1>v3 - 컴포넌트 스캔으로 스프링 빈 자동 등록</h1>
 */
@Slf4j
@RestController
public class OrderControllerV3 {

    private final OrderServiceV3 orderService;

    public OrderControllerV3(OrderServiceV3 orderService) {
        this.orderService = orderService;
    }

    /**
     * @see <a href="http://localhost:8080/proxy/v3/request?itemId=hello">정상호출</a>
     * @see <a href="http://localhost:8080/proxy/v3/request?itemId=ex">예외호출</a>
     */
    @GetMapping("/proxy/v3/request")
    public String request(String itemId) {
        orderService.orderItem(itemId);
        return "ok";
    }

    @GetMapping("/proxy/v3/no-log")
    public String noLog() {
        return "ok";
    }
}
