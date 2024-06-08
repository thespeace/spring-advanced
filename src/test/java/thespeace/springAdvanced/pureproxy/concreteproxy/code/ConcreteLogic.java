package thespeace.springAdvanced.pureproxy.concreteproxy.code;

import lombok.extern.slf4j.Slf4j;

/**
 * ConcreteLogic 은 인터페이스가 없고, 구체 클래스만 있다. 여기에 프록시를 도입해야 한다.
 */
@Slf4j
public class ConcreteLogic {

    public String operation() {
        log.info("ConcreteLogic 실행");
        return "data";
    }
}
