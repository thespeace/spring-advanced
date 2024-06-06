package thespeace.springAdvanced.pureproxy.proxy;

import org.junit.jupiter.api.Test;
import thespeace.springAdvanced.pureproxy.proxy.code.CacheProxy;
import thespeace.springAdvanced.pureproxy.proxy.code.ProxyPatternClient;
import thespeace.springAdvanced.pureproxy.proxy.code.RealSubject;

/**
 * <h1>프록시 패턴 예제 코드</h1>
 */
public class ProxyPatternTest {

    /**
     * <h2>프록시 패턴 적용 전</h2>
     * 클래스 의존 관계 : `Client -> Subject <- RealSubject`<br>
     * 런타임 객체 의존 관계 : `client -( operation() )-> realSubject`<p><p>
     *
     * client.execute() 를 3번 호출한다. 데이터를 조회하는데 1초가 소모되므로 총 3초의 시간이 걸린다.<br>
     * 그런데 이 데이터가 한번 조회하면 변하지 않는 데이터라면 어딘가에 보관해두고 이미 조회한 데이터를
     * 사용하는 것이 성능상 좋다. 이런 것을 캐시라고 한다.<br>
     * 프록시 패턴의 주요 기능은 접근 제어이다. 캐시도 접근 자체를 제어하는 기능 중 하나이다.<br>
     * 이미 개발된 로직을 전혀 수정하지 않고, 프록시 객체를 통해서 캐시를 적용해보자.
     */
    @Test
    void noProxyTest() {
        RealSubject realSubject = new RealSubject();
        ProxyPatternClient client = new ProxyPatternClient(realSubject);
        client.execute();
        client.execute();
        client.execute();
    }

    /**
     * <h2>프록시 패턴 적용 후</h2>
     * 클래스 의존 관계 : `Client -> Subject <- RealSubject and Proxy`<br>
     * 런타임 객체 의존 관계 : `client -( operation() )-> proxy -( operation() )-> realSubject`<p><p>
     *
     * realSubject 와 cacheProxy 를 생성하고 둘을 연결한다. 결과적으로 cacheProxy 가 realSubject 를 참조하는
     * 런타임 객체 의존관계가 완성된다. 그리고 마지막으로 client 에 realSubject 가 아닌 cacheProxy 를 주입한다.
     * 이 과정을 통해서 client -> cacheProxy -> realSubject 런타임 객체 의존 관계가 완성된다.<p><p>
     *
     * cacheProxyTest() 는 client.execute() 을 총 3번 호출한다. 이번에는 클라이언트가 실제 realSubject를
     * 호출하는 것이 아니라 cacheProxy 를 호출하게 된다.<p><p>
     *
     * client.execute()을 3번 호출하면 다음과 같이 처리된다.
     * <ol>
     *     <li>client의 cacheProxy 호출 cacheProxy에 캐시 값이 없다. realSubject를 호출, 결과를 캐시에 저장 (1초)</li>
     *     <li>client의 cacheProxy 호출 cacheProxy에 캐시 값이 있다. cacheProxy에서 즉시 반환 (0초)</li>
     *     <li>client의 cacheProxy 호출 cacheProxy에 캐시 값이 있다. cacheProxy에서 즉시 반환 (0초)</li>
     * </ol>
     * 결과적으로 캐시 프록시를 도입하기 전에는 3초가 걸렸지만, 캐시 프록시 도입 이후에는 최초에 한번만 1초가 걸리고,
     * 이후에는 거의 즉시 반환한다.<p><p>
     *
     * <h2>정리</h2>
     * 프록시 패턴의 핵심은 RealSubject 코드와 클라이언트 코드를 전혀 변경하지 않고, 프록시를 도입해서 접근 제어를
     * 했다는 점이다.<br>
     * 그리고 클라이언트 코드의 변경 없이 자유롭게 프록시를 넣고 뺄 수 있다.<br>
     * 실제 클라이언트 입장에서는 프록시 객체가 주입되었는지, 실제 객체가 주입되었는지 알지 못한다.
     */
    @Test
    void cacheProxyTest() {
        RealSubject realSubject = new RealSubject();
        CacheProxy cacheProxy = new CacheProxy(realSubject);
        ProxyPatternClient client = new ProxyPatternClient(cacheProxy);
        client.execute();
        client.execute();
        client.execute();
    }
}
