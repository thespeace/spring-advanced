package thespeace.springAdvanced.trace.mytrace;

import org.junit.jupiter.api.Test;
import thespeace.springAdvanced.trace.TraceStatus;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 예제를 최대한 단순화 하기 위해 '검증 테스트를 생략'<br>
 * 즉, 온전한 테스트 코드가 아니고 결과만 콘솔로 직접 확인.
 */
class MyTraceV1Test {

    @Test
    void begin_end() {
        MyTraceV1 trace = new MyTraceV1();
        TraceStatus status = trace.begin("hello");
        trace.end(status);
    }

    @Test
    void begin_exception() {
        MyTraceV1 trace = new MyTraceV1();
        TraceStatus status = trace.begin("hello");
        trace.exception(status, new IllegalStateException());
    }
}