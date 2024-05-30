package thespeace.springAdvanced.trace.mytrace;

import org.junit.jupiter.api.Test;
import thespeace.springAdvanced.trace.TraceStatus;

/**
 * 예제를 최대한 단순화 하기 위해 '검증 테스트를 생략'<br>
 * 즉, 온전한 테스트 코드가 아니고 결과만 콘솔로 직접 확인.
 */
class MyTraceV2Test {

    @Test
    void begin_end() {
        MyTraceV2 trace = new MyTraceV2();
        TraceStatus status1 = trace.begin("hello1");
        TraceStatus status2 = trace.beginSync(status1.getTraceId(), "hello2");
        trace.end(status2);
        trace.end(status1);
    }

    @Test
    void begin_exception() {
        MyTraceV2 trace = new MyTraceV2();
        TraceStatus status1 = trace.begin("hello1");
        TraceStatus status2 = trace.beginSync(status1.getTraceId(), "hello2");
        trace.exception(status2, new IllegalStateException());
        trace.exception(status1, new IllegalStateException());
    }
}