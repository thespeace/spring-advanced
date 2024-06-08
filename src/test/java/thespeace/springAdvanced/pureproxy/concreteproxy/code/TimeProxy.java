package thespeace.springAdvanced.pureproxy.concreteproxy.code;

import lombok.extern.slf4j.Slf4j;

/**
 * TimeProxy 프록시는 시간을 측정하는 부가 기능을 제공한다.<br>
 * 그리고 인터페이스가 아니라 클래스인 ConcreteLogic 를 상속 받아서 만든다.
 */
@Slf4j
public class TimeProxy extends ConcreteLogic {

    private ConcreteLogic concreteLogic;

    public TimeProxy(ConcreteLogic concreteLogic) {
        this.concreteLogic = concreteLogic;
    }

    @Override
    public String operation() {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        String result = concreteLogic.operation();

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeDecorator 종료 resultTime={}ms", resultTime);
        return result;
    }
}
