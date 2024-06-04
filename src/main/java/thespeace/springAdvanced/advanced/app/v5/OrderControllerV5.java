package thespeace.springAdvanced.advanced.app.v5;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thespeace.springAdvanced.trace.callback.TraceCallback;
import thespeace.springAdvanced.trace.callback.TraceTemplate;
import thespeace.springAdvanced.trace.logtrace.LogTrace;

/**
 * <h1>템플릿 콜백 패턴 - 적용</h1>
 * <ul>
 *     <li>`this.template = new TraceTemplate(trace)` : `trace` 의존관계 주입을 받으면서 필요한
 *         `TraceTemplate` 템플릿을 생성한다. 참고로 `TraceTemplate` 를 처음부터 스프링 빈으로 등록하고
 *         주입받아도 된다. 이 부분은 선택이다.</li>
 *     <li>`template.execute(.., new TraceCallback(){..})` : 템플릿을 실행하면서 콜백을 전달한다.
 *         여기서는 콜백으로 익명 내부 클래스를 사용했다.</li>
 * </ul><br>
 *
 * <h2>정리</h2>
 * 지금까지 우리는 변하는 코드와 변하지 않는 코드를 분리하고, 더 적은 코드로 로그 추적기를 적용하기 위해 고군분투했다.<br>
 * 템플릿 메서드 패턴, 전략 패턴, 그리고 템플릿 콜백 패턴까지 진행하면서 변하는 코드와 변하지 않는 코드를 분리했다.<br>
 * 그리고 최종적으로 템플릿 콜백 패턴을 적용하고 콜백으로 람다를 사용해서 코드 사용도 최소화 할 수 있었다.<p><p>
 *
 * <h2>한계</h2>
 * 그런데 지금까지 설명한 방식의 한계는 아무리 최적화를 해도 결국 로그 추적기를 적용하기 위해서 원본 코드를
 * 수정해야 한다는 점이다.<br>
 * 클래스가 수백개이면 수백개를 더 힘들게 수정하는가 조금 덜 힘들게 수정하는가의 차이가 있을 뿐, 본질적으로
 * 코드를 다 수정해야 하는 것은 마찬가지이다.<p><p>
 *
 * 개발자의 게으름에 대한 욕심은 끝이 없다. 수 많은 개발자가 이 문제에 대해서 집요하게 고민해왔고,
 * 여러가지 방향으로 해결책을 만들어왔다. 지금부터 원본 코드를 손대지 않고 로그 추적기를 적용할 수 있는 방법을 알아보자.<br>
 * 그러기 위해서 프록시 개념을 먼저 이해해야 한다.
 *
 * @reference 지금까지 설명한 방식은 실제 스프링 안에서 많이 사용되는 방식이다. `XxxTemplate` 를 만나면 이번에 학습한
 *            내용을 떠올려보면 어떻게 돌아가는지 쉽게 이해할 수 있을 것이다.
 */
@RestController
public class OrderControllerV5 {

    private final OrderServiceV5 orderService;
    private final TraceTemplate Template;

    public OrderControllerV5(OrderServiceV5 orderService, LogTrace trace) {
        this.orderService = orderService;
        this.Template = new TraceTemplate(trace);
    }

    /**
     * <h2>템플릿 콜백 패턴 - 적용</h2>
     * @see <a href="http://localhost:8080/advanced/v5/request?itemId=hello">정상 호출</a>
     * @see <a href="http://localhost:8080/advanced/v5/request?itemId=ex">예외 호출</a>
     */
    @GetMapping("/advanced/v5/request")
    public String request(String itemId) {
        return Template.execute("OrderController.request()", new TraceCallback<>() {
            @Override
            public String call() {
                orderService.orderItem(itemId);
                return "ok";
            }
        });
    }

}
