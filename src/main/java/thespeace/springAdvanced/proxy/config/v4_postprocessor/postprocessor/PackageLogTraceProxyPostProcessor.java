package thespeace.springAdvanced.proxy.config.v4_postprocessor.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * <h1>빈 후처리기 - 적용</h1>
 * 빈 후처리기를 사용해서 실제 객체 대신 프록시를 스프링 빈으로 등록해보자.<br>
 * 이렇게 하면 수동으로 등록하는 빈은 물론이고, 컴포넌트 스캔을 사용하는 빈까지 모두 프록시를 적용할 수 있다.<br>
 * 더 나아가서 설정 파일에 있는 수 많은 프록시 생성 코드도 한번에 제거할 수 있다
 * <p><p>
 *
 * <h2>PackageLogTraceProxyPostProcessor</h2>
 * <ul>
 *     <li>PackageLogTraceProxyPostProcessor 는 원본 객체를 프록시 객체로 변환하는 역할을 한다.
 *         이때 프록시 팩토리를 사용하는데, 프록시 팩토리는 advisor 가 필요하기 때문에 이 부분은
 *         외부에서 주입 받도록 했다.</li>
 *     <li>모든 스프링 빈들에 프록시를 적용할 필요는 없다. 여기서는 특정 패키지와 그 하위에 위치한
 *         스프링 빈들만 프록시를 적용한다. 여기서는 hello.proxy.app 과 관련된 부분에만 적용하면 된다.
 *         다른 패키지의 객체들은 원본 객체를 그대로 반환한다.</li>
 *     <li>프록시 적용 대상의 반환 값을 보면 원본 객체 대신에 프록시 객체를 반환한다. 따라서 스프링 컨테이너에
 *         원본 객체 대신에 프록시 객체가 스프링 빈으로 등록된다. 원본 객체는 스프링 빈으로 등록되지 않는다.</li>
 * </ul>
 */
@Slf4j
public class PackageLogTraceProxyPostProcessor implements BeanPostProcessor {

    private final String basePackage;
    private final Advisor advisor;

    public PackageLogTraceProxyPostProcessor(String basePackage, Advisor advisor) {
        this.basePackage = basePackage;
        this.advisor = advisor;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.info("param beanName={} bean={}", beanName, bean.getClass());

        //프록시 적용 대상 여부 체크
        //프록시 적용 대상이 아니면 원본을 그대로 반환
        String packageName = bean.getClass().getPackageName();
        if(!packageName.startsWith(basePackage)) {
            return bean;
        }

        //프록시 대상이면 프록시를 만들어서 반환
        ProxyFactory proxyFactory = new ProxyFactory(bean);
        proxyFactory.addAdvisor(advisor);

        Object proxy = proxyFactory.getProxy();
        log.info("create proxy: target={} proxy={}", bean.getClass(), proxy.getClass());
        return proxy;
    }
}
