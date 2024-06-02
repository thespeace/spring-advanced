package thespeace.springAdvanced.trace.template;

import thespeace.springAdvanced.trace.TraceStatus;
import thespeace.springAdvanced.trace.logtrace.LogTrace;

/**
 * <h1>로그 추적기 로직에 템플릿 메서드 패턴을 적용</h1>
 * <ul>
 *     <li>AbstractTemplate 은 템플릿 메서드 패턴에서 부모 클래스이고, 템플릿 역할을 한다.</li>
 *     <li><T> 제네릭을 사용했다. 반환 타입을 정의한다.</li>
 *     <li>객체를 생성할 때 내부에서 사용할 LogTrace trace 를 전달 받는다.</li>
 *     <li>로그에 출력할 message 를 외부에서 파라미터로 전달받는다.</li>
 *     <li>템플릿 코드 중간에 call() 메서드를 통해서 변하는 부분을 처리한다.</li>
 *     <li>abstract T call() 은 변하는 부분을 처리하는 메서드이다. 이 부분은 상속으로 구현해야 한다.</li>
 * </ul>
 */
public abstract class AbstractTemplate<T> {

    private final LogTrace trace;

    public AbstractTemplate(LogTrace trace) {
        this.trace = trace;
    }

    public T execute(String message) {
        TraceStatus status = null;
        try {
            status = trace.begin("OrderController.request()");

            //로직 호출
            T result = call();

            trace.end(status);
            return result;
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }

    protected abstract T call();
}
