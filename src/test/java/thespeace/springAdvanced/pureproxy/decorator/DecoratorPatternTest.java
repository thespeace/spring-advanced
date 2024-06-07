package thespeace.springAdvanced.pureproxy.decorator;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import thespeace.springAdvanced.pureproxy.decorator.code.*;

@Slf4j
public class DecoratorPatternTest {

    /**
     * <h1>데코레이터 패턴을 도입하기 전</h1>
     * 클래스 의존 관계 : `Client -> Component <- RealComponent`<br>
     * 런타임 객체 의존 관계 : `client -( operation() )-> realComponent`<p><p>
     */
    @Test
    void noDecorator() {
        RealComponent realComponent = new RealComponent();
        DecoratorPatternClient client = new DecoratorPatternClient(realComponent);
        client.execute();
    }

    /**
     * <h1>데코레이터 패턴(응답 값을 꾸며주는) 도입</h1>
     * 프록시를 활용해서 부가 기능을 추가해보자.<br>
     * 이렇게 프록시로 부가 기능을 추가하는 것을 데코레이터 패턴이라 한다.<p><p>
     *
     * 클래스 의존 관계 : `Client -> Component <- RealComponent and MessageDecorator`<br>
     * 런타임 객체 의존 관계 : `client -( operation() )-> messageDecorator -( operation() )-> realComponent`<p><p>
     */
    @Test
    void decorator1() {
        Component realComponent = new RealComponent();
        Component messageDecorator = new MessageDecorator(realComponent);
        DecoratorPatternClient client = new DecoratorPatternClient(messageDecorator);
        client.execute();
    }

    /**
     * <h1>데코레이터 패턴(실행 시간을 측정) 추가</h1>
     * 기존 데코레이터에 더해서 실행 시간을 측정하는 기능까지 추가해보자.
     *
     * 클래스 의존 관계 : `Client -> Component <- RealComponent and MessageDecorator and TimeDecorator`<br>
     * 런타임 객체 의존 관계 : `client -( operation() )-> timeDecorator -( operation() )-> messageDecorator -( operation() )-> realComponent`<p><p>
     *
     * client -> timeDecorator -> messageDecorator -> realComponent 의 객체 의존관계를 설정하고, 실행한다.
     */
    @Test
    void decorator2() {
        Component realComponent = new RealComponent();
        Component messageDecorator = new MessageDecorator(realComponent);
        Component timeDecorator = new TimeDecorator(messageDecorator);
        DecoratorPatternClient client = new DecoratorPatternClient(timeDecorator);
        client.execute();
    }
}
