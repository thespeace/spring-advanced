package thespeace.springAdvanced.proxy.config.v2_dynamicproxy.handler;

import org.springframework.util.PatternMatchUtils;
import thespeace.springAdvanced.trace.TraceStatus;
import thespeace.springAdvanced.trace.logtrace.LogTrace;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * <h1>JDK 동적 프록시에 메서드 이름 필터 기능 추가</h1>
 * 요구사항에 의해 해당 URL(http://localhost:8080/v1/no-log)을 호출 했을때는
 * 로그가 남으면 안된다.<br>
 * 이런 문제를 해결하기 위해 메서드 이름을 기준으로 특정 조건을 만족할 때만 로그를
 * 남기는 기능을 개발해보자.
 */
public class LogTraceFilterHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace logTrace;
    private final String[] patterns;

    public LogTraceFilterHandler(Object target, LogTrace logTrace, String[] patterns) {
        this.target = target;
        this.logTrace = logTrace;
        this.patterns = patterns;
    }

    /**
     * <ul>
     *     <li>특정 메서드 이름이 매칭 되는 경우에만 LogTrace 로직을 실행한다.
     *         이름이 매칭되지 않으면 실제 로직을 바로 호출.</li>
     *     <li>스프링이 제공하는 PatternMatchUtils.simpleMatch(..) 를 사용하면
     *         단순한 매칭 로직을 쉽게 적용할 수 있다.
     *         <ul>
     *             <li>xxx : xxx가 정확히 매칭되면 참</li>
     *             <li>xxx* : xxx로 시작하면 참</li>
     *             <li>*xxx : xxx로 끝나면 참</li>
     *             <li>*xxx* : xxx가 있으면 참</li>
     *         </ul>
     *     </li>
     *     <li>String[] patterns : 적용할 패턴은 생성자를 통해서 외부에서 받는다.</li>
     * </ul>
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //메서드 이름 필터
        String methodName = method.getName();

        if(!PatternMatchUtils.simpleMatch(patterns, methodName)) {
            return method.invoke(target, args);
        }

        TraceStatus status = null;
        try {
            String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
            status = logTrace.begin(message);

            //로직 호출
            Object result = method.invoke(target, args);
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
