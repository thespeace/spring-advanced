package thespeace.springAdvanced.app.v4;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import thespeace.springAdvanced.trace.TraceStatus;
import thespeace.springAdvanced.trace.logtrace.LogTrace;
import thespeace.springAdvanced.trace.template.AbstractTemplate;

@RestController
@RequiredArgsConstructor
public class OrderControllerV4 {

    private final OrderServiceV4 orderService;
    private final LogTrace trace;

    /**
     * <h2>템플릿 메서드 패턴 - 적용1</h2>
     * 익명 내부 클래스를 사용한다.<br>
     * 객체를 생성하면서 AbstractTemplate 를 상속받은 자식 클래스를 정의했다.
     * 따라서 별도의 자식 클래스를 직접 만들지 않아도 된다.
     *
     * @see <a href="http://localhost:8080/v4/request?itemId=hello">정상 호출</a>
     * @see <a href="http://localhost:8080/v4/request?itemId=ex">예외 호출</a>
     */
    @GetMapping("/v4/request")
    public String request(String itemId) {
        AbstractTemplate<String> template = new AbstractTemplate<>(trace) {
            @Override
            protected String call() {
                orderService.orderItem(itemId);
                return "ok";
            }
        };
        return template.execute("OrderController.request()"); //템플릿을 실행하면서 로그로 남길 message 를 전달.
    }

}
