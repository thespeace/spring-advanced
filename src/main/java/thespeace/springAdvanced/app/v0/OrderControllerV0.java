package thespeace.springAdvanced.app.v0;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //컴포넌트 스캔과 스프링 Rest컨트롤러로 인식된다.
@RequiredArgsConstructor
public class OrderControllerV0 {

    private final OrderServiceV0 orderService;

    /**
     * <h2>예제 프로젝트 - V0</h2>
     * 간단한 예제 프로젝트로 상품을 주문하는 프로세스로 가정하고, 일반적인 웹 애플리케이션에서
     * [Controller -> Service -> Repository]로 이어지는 흐름(실무에서 일반적으로 사용하는 기본 흐름)을 최대한 단순하게 만들었다.
     *
     * @see <a href="http://localhost:8080/v0/request?itemId=hello">정상 호출</a>
     * @see <a href="http://localhost:8080/v0/request?itemId=ex">예외 호출</a>
     */
    @GetMapping("/v0/request")
    public String request(String itemId) {
        orderService.orderItem(itemId);
        return "ok";
    }
}
