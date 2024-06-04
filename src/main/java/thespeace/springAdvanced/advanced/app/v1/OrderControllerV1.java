package thespeace.springAdvanced.advanced.app.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thespeace.springAdvanced.trace.TraceStatus;
import thespeace.springAdvanced.trace.mytrace.MyTraceV1;

@RestController
@RequiredArgsConstructor
public class OrderControllerV1 {

    private final OrderServiceV1 orderService;
    private final MyTraceV1 trace;

    /**
     * <h2>로그 추적기(MyTraceV1) 적용 - V1</h2>
     *
     * <br>
     *
     * <h2>구현한 요구사항</h2>
     * <ul>
     *     <li>모든 PUBLIC 메서드의 호출과 응답 정보를 로그로 출력</li>
     *     <li>애플리케이션의 흐름을 변경하면 안됨
     *         <ul>
     *             <li>로그를 남긴다고 해서 비즈니스 로직의 동작에 영향을 주면 안됨</li>
     *         </ul>
     *     </li>
     *     <li>메서드 호출에 걸린 시간</li>
     *     <li>정상 흐름과 예외 흐름 구분
     *         <ul>
     *             <li>예외 발생시 예외 정보가 남아야 함</li>
     *         </ul>
     *     </li>
     * </ul>
     *
     * @see <a href="http://localhost:8080/advanced/v1/request?itemId=hello">정상 호출</a>
     * @see <a href="http://localhost:8080/advanced/v1/request?itemId=ex">예외 호출</a>
     */
    @GetMapping("/advanced/v1/request")
    public String request(String itemId) {

        TraceStatus status = null;
        try {
            status = trace.begin("OrderController.request()"); //컨트롤러 이름 + 메서드 이름.
            orderService.orderItem(itemId);
            trace.end(status);
            return "ok";
        } catch (Exception e) {
            trace.exception(status, e);
            throw e; //예외를 꼭 다시 던져주어야 한다.
        }
    }
}
