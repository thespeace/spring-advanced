package thespeace.springAdvanced.trace.logtrace;

import thespeace.springAdvanced.trace.TraceStatus;

/**
 * <h1>필드 동기화 - 개발</h1>
 * 로그 추적기(mytrace)를 만들면서 다음 로그를 출력할 때 트랜잭션ID 와 level 을 동기화 하는 문제가 있었다.<br>
 * 이 문제를 해결하기 위해 TraceId 를 파라미터로 넘기도록 구현했다.<br>
 * 이렇게 해서 동기화는 성공했지만, 로그를 출력하는 모든 메서드에 TraceId 파라미터를 추가해야 하는 문제가 발생했다.<br>
 * `TraceId`를 파라미터로 넘기지 않고 이 문제를 해결할 수 있는 방법은 없을까?<p><p>
 *
 * 이런 문제를 해결할 목적으로 새로운 로그 추적기를 만들어보자.
 * 이제 프로토타입 버전이 아닌 정식 버전으로 제대로 개발해보자.
 * 향후 다양한 구현제로 변경할 수 있도록 `LogTrace`인터페이스를 먼저 만들고, 구현해보자.<p><p>
 *
 * 로그 추적기를 위한 최소한의 기능인 begin() , end() , exception() 를 정의했다.<br>
 */
public interface LogTrace {

    TraceStatus begin(String message);

    void end(TraceStatus status);

    void exception(TraceStatus status, Exception e);
}
