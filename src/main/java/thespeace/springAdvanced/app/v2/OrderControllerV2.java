package thespeace.springAdvanced.app.v2;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import thespeace.springAdvanced.trace.TraceStatus;
import thespeace.springAdvanced.trace.mytrace.MyTraceV2;

@RestController
@RequiredArgsConstructor
public class OrderControllerV2 {

    private final OrderServiceV2 orderService;
    private final MyTraceV2 trace;

    /**
     * <h2>로그 추적기(MyTraceV2) 적용 - V2</h2>
     *
     * <br>
     *
     * <h2>구현한 요구사항</h2>
     * <ul>
     *     <li>메서드 호출의 깊이 표현</li>
     *     <li>HTTP 요청을 구분
     *         <ul>
     *             <li>HTTP 요청 단위로 특정 ID를 남겨서 어떤 HTTP 요청에서 시작된 것인지 명확하게 구분이 가능해야 함</li>
     *             <li>트랜잭션 ID (DB 트랜잭션X)</li>
     *         </ul>
     *     </li>
     * </ul>
     *
     * @see <a href="http://localhost:8080/v2/request?itemId=hello">정상 호출</a>
     * @see <a href="http://localhost:8080/v2/request?itemId=ex">예외 호출</a>
     */
    @GetMapping("/v2/request")
    public String request(String itemId) {

        TraceStatus status = null;
        try {
            status = trace.begin("OrderController.request()");
            orderService.orderItem(status.getTraceId(), itemId);
            trace.end(status);
            return "ok";
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
