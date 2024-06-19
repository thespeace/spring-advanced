package thespeace.springAdvanced.proxy.config.v6_aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import thespeace.springAdvanced.trace.TraceStatus;
import thespeace.springAdvanced.trace.logtrace.LogTrace;

/**
 * <h1>@Aspect 프록시 - 적용</h1>
 * <ul>
 *     <li>@Aspect : 애노테이션 기반 프록시를 적용할 때 필요하다.</li>
 *     <li>@Around("execution(* hello.proxy.app..*(..))")
 *         <ul>
 *             <li>@Around 의 값에 포인트컷 표현식을 넣는다. 표현식은 AspectJ 표현식을 사용한다.</li>
 *             <li>@Around 의 메서드는 어드바이스( Advice )가 된다.</li>
 *         </ul>
 *     </li>
 *     <li>ProceedingJoinPoint joinPoint : 어드바이스에서 살펴본 MethodInvocation invocation 과
 *         유사한 기능이다. 내부에 실제 호출 대상, 전달 인자, 그리고 어떤 객체와 어떤 메서드가 호출되었는지
 *         정보가 포함되어 있다.</li>
 *     <li>joinPoint.proceed() : 실제 호출 대상( target )을 호출한다.</li>
 * </ul>
 */
@Slf4j
@Aspect
public class LogTraceAspect {

    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    @Around("execution(* thespeace.springAdvanced.proxy.app..*(..))") // == pointcut
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable { // == advice

        TraceStatus status = null;
        // log.info("target={}", joinPoint.getTarget()); //실제 호출 대상
        // log.info("getArgs={}", joinPoint.getArgs()); //전달인자
        // log.info("getSignature={}", joinPoint.getSignature()); //join point 시그니처

        try {
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(message);

            //로직 호출
            Object result = joinPoint.proceed();

            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
