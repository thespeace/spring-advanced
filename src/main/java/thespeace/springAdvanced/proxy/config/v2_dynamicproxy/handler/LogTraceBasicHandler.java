package thespeace.springAdvanced.proxy.config.v2_dynamicproxy.handler;

import thespeace.springAdvanced.trace.TraceStatus;
import thespeace.springAdvanced.trace.logtrace.LogTrace;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * <h1>InvocationHandler 인터페이스를 구현해서 JDK 동적 프록시에서 사용</h1>
 * <ul>
 *     <li>private final Object target : 프록시가 호출할 대상이다.</li>
 *     <li>String message = method.getDeclaringClass().getSimpleName() + "." ...
 *         <ul>
 *             <li>LogTrace 에 사용할 메시지이다. 프록시를 직접 개발할 때는 "OrderController.request()" 와
 *                 같이 프록시마다 호출되는 클래스와 메서드 이름을 직접 남겼다. 이제는 Method 를 통해서 호출되는
 *                 메서드 정보와 클래스 정보를 동적으로 확인할 수 있기 때문에 이 정보를 사용하면 된다.</li>
 *         </ul>
 *     </li>
 * </ul>
 */
public class LogTraceBasicHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace logTrace;

    public LogTraceBasicHandler(Object target, LogTrace logTrace) {
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
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
