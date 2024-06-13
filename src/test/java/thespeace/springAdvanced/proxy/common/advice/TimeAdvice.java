package thespeace.springAdvanced.proxy.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor; //주의!
import org.aopalliance.intercept.MethodInvocation;

/**
 * <h1>Advice 만들기</h1>
 * Advice 는 프록시에 적용하는 부가 기능 로직이다. 이것은 JDK 동적 프록시가 제공하는 InvocationHandler 와
 * CGLIB가 제공하는 MethodInterceptor 의 개념과 유사한다. 둘을 개념적으로 추상화 한 것이다. 프록시 팩토리를
 * 사용하면 둘 대신에 Advice 를 사용하면 된다.<p><p>
 *
 * Advice 를 만드는 방법은 여러가지가 있지만, 기본적인 방법은 다음 인터페이스를 구현하면 된다.<p><p>
 *
 * <h2>MethodInterceptor - 스프링이 제공하는 코드</h2>
 * <pre>
 * package org.aopalliance.intercept;
 *
 * public interface MethodInterceptor extends Interceptor {
 *     Object invoke(MethodInvocation invocation) throws Throwable;
 * }
 * </pre>
 * <ul>
 *     <li>MethodInvocation invocation
 *         <ul>
 *             <li>내부에는 다음 메서드를 호출하는 방법, 현재 프록시 객체 인스턴스, args , 메서드 정보 등이
 *                 포함되어 있다. 기존에 파라미터로 제공되는 부분들이 이 안으로 모두 들어갔다고 생각하면 된다.</li>
 *         </ul>
 *     </li>
 *     <li>CGLIB의 MethodInterceptor 와 이름이 같으므로 패키지 이름에 주의하자.
 *         <ul>
 *             <li>참고로 여기서 사용하는 org.aopalliance.intercept 패키지는
 *                 스프링 AOP 모듈(spring-aop)안에 들어있다</li>
 *         </ul>
 *     </li>
 *     <li>MethodInterceptor 는 Interceptor 를 상속하고 Interceptor 는 Advice 인터페이스를 상속한다.</li>
 * </ul>
 */
@Slf4j
public class TimeAdvice implements MethodInterceptor {

    /**
     * <ul>
     *     <li>TimeAdvice 는 앞서 설명한 MethodInterceptor 인터페이스를 구현한다. 패키지 이름에 주의하자.</li>
     *     <li>Object result = invocation.proceed()
     *         <ul>
     *             <li>invocation.proceed() 를 호출하면 target 클래스를 호출하고 그 결과를 받는다.</li>
     *             <li>그런데 기존에 보았던 코드들과 다르게 target 클래스의 정보가 보이지 않는다.
     *                 target 클래스의 정보는 MethodInvocation invocation 안에 모두 포함되어 있다.</li>
     *             <li>그 이유는 바로 다음에 확인할 수 있는데, 프록시 팩토리로 프록시를 생성하는 단계에서
     *                 이미 target 정보를 파라미터로 전달받기 때문이다.</li>
     *         </ul>
     *     </li>
     * </ul>
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        Object result = invocation.proceed();

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeProxy 종료 resultTime={}ms", resultTime);
        return result;
    }
}
