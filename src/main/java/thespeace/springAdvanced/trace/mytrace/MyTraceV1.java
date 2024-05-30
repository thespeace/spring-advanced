package thespeace.springAdvanced.trace.mytrace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import thespeace.springAdvanced.trace.TraceId;
import thespeace.springAdvanced.trace.TraceStatus;

/**
 * <h1>MyTraceV1</h1>
 * MyTraceV1 을 사용해서 실제 로그를 시작하고 종료할 수 있다. 그리고 로그를 출력하고 실행시간도 측정할 수 있다.
 */
@Slf4j
@Component //싱글톤으로 사용하기 위해 스프링 빈으로 등록한다. 컴포넌트 스캔의 대상이 된다.
public class MyTraceV1 {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    /**
     * <ul>
     *     <li>로그를 시작한다.</li>
     *     <li>로그 메시지를 파라미터로 받아서 시작 로그를 출력한다.</li>
     *     <li>응답 결과로 현재 로그의 상태인 TraceStatus 를 반환한다.</li>
     * </ul>
     */
    public TraceStatus begin(String message) {
        TraceId traceId = new TraceId();
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    };

    /**
     * <ul>
     *     <li>로그를 정상 종료한다.</li>
     *     <li>파라미터로 시작 로그의 상태( TraceStatus )를 전달 받는다.
     *         이 값을 활용해서 실행 시간을 계산하고, 종료시에도 시작할 때와 동일한 로그 메시지를 출력할 수 있다.</li>
     *     <li>정상 흐름에서 호출한다.</li>
     * </ul>
     */
    public void end(TraceStatus status) {
        complete(status, null);
    };

    /**
     * <ul>
     *     <li>로그를 예외 상황으로 종료한다.</li>
     *     <li>TraceStatus , Exception 정보를 함께 전달 받아서 실행시간, 예외 정보를 포함한 결과 로그를 출력한다.</li>
     *     <li>예외가 발생했을 때 호출한다.</li>
     * </ul>
     */
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    /**
     * end() , exception()의 요청 흐름을 한곳에서 편리하게 처리한다. 실행 시간을 측정하고 로그를 남긴다.
     */
    private void complete(TraceStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();
        if (e == null) {
            log.info("[{}] {}{} time={}ms", traceId.getId(),
                    addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs);
        } else {
            log.info("[{}] {}{} time={}ms ex={}", traceId.getId(),
                    addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs, e.toString());
        }
    }

    /**
     * 다음과 같은 결과를 출력한다.
     *
     * <pre>
     *     prefix: -->
     *          level 0: ``
     *          level 1: |-->
     *          level 2: | |-->
     *
     *      prefix: <--
     *          level 0: ``
     *          level 1: |<--
     *          level 2: | |<--
     *
     *      prefix: <☓-
     *          level 0: ``
     *          level 1: |<☓-
     *          level 2: | |<☓-
     * </pre>
     */
    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append( (i == level - 1) ? "|" + prefix : "| ");
        }
        return sb.toString();
    }

}
