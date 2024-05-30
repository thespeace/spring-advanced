package thespeace.springAdvanced.trace;

/**
 * <h1>TraceStatus 클래스</h1>
 * 로그의 상태 정보를 나타낸다.<p><p>
 *
 * 로그를 시작하면 끝이 있어야 한다.
 * <pre>
 *     [796bccd9] OrderController.request() //로그 시작
 *     [796bccd9] OrderController.request() time=1016ms //로그 종료
 * </pre>
 * TraceStatus 는 로그를 시작할 때의 상태 정보를 가지고 있다. 이 상태 정보는 로그를 종료할 때 사용된다.
 *
 * <ul>
 *     <li>traceId : 내부에 트랜잭션ID와 level을 가지고 있다.</li>
 *     <li>startTimeMs : 로그 시작시간이다. 로그 종료시 이 시작 시간을 기준으로 시작~종료까지 전체 수행 시간을 구할 수 있다.</li>
 *     <li>message : 시작시 사용한 메시지이다. 이후 로그 종료시에도 이 메시지를 사용해서 출력한다.</li>
 * </ul>
 */
public class TraceStatus {

    private TraceId traceId;
    private Long startTimeMs;
    private String message;

    public TraceStatus(TraceId traceId, Long startTimeMs, String message) {
        this.traceId = traceId;
        this.startTimeMs = startTimeMs;
        this.message = message;
    }

    public TraceId getTraceId() {
        return traceId;
    }

    public Long getStartTimeMs() {
        return startTimeMs;
    }

    public String getMessage() {
        return message;
    }
}
