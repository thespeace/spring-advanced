package thespeace.springAdvanced.trace.mytrace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import thespeace.springAdvanced.trace.TraceId;
import thespeace.springAdvanced.trace.TraceStatus;

/**
 * <h1>MyTraceV2</h1>
 */
@Slf4j
@Component
public class MyTraceV2 {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";


    public TraceStatus begin(String message) {
        TraceId traceId = new TraceId();
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    };

    /**
     * <ul>
     *     <li>기존 TraceId 에서 createNextId() 를 통해 다음 ID를 구한다.</li>
     *     <li>createNextId() 의 TraceId 생성 로직은 다음과 같다.
     *         <ul>
     *             <li>트랜잭션ID는 기존과 같이 유지한다.</li>
     *             <li>깊이를 표현하는 Level은 하나 증가한다. ( 0 -> 1 )</li>
     *         </ul>
     *     </li>
     * </ul>
     */
    public TraceStatus beginSync(TraceId beforeTraceId, String message) {
        TraceId nextId = beforeTraceId.createNextId();
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", nextId.getId(), addSpace(START_PREFIX, nextId.getLevel()), message);
        return new TraceStatus(nextId, startTimeMs, message);
    };

    public void end(TraceStatus status) {
        complete(status, null);
    };

    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

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

    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append( (i == level - 1) ? "|" + prefix : "| ");
        }
        return sb.toString();
    }

}
