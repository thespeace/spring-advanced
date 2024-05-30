package thespeace.springAdvanced.app.v3;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import thespeace.springAdvanced.trace.TraceStatus;
import thespeace.springAdvanced.trace.logtrace.LogTrace;

@RestController
@RequiredArgsConstructor
public class OrderControllerV3 {

    private final OrderServiceV3 orderService;
    private final LogTrace trace;

    /**
     * <h2>필드 동기화 - 적용</h2>
     * traceIdHolder 필드를 사용한 덕분에 파라미터 추가 없는 깔끔한 로그 추적기를 완성했다.<br>
     * 이제 실제 서비스에 배포한다고 가정해보면 여기에는 심각한 문제가 있다.<br>
     * 다음에 알아보자!
     *
     * @see <a href="http://localhost:8080/v3/request?itemId=hello">정상 호출</a>
     * @see <a href="http://localhost:8080/v3/request?itemId=ex">예외 호출</a>
     */
    @GetMapping("/v3/request")
    public String request(String itemId) {

        TraceStatus status = null;
        try {
            status = trace.begin("OrderController.request()");
            orderService.orderItem(itemId);
            trace.end(status);
            return "ok";
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
