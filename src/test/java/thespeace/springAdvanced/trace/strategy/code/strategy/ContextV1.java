package thespeace.springAdvanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * <h1>변하지 않는 로직을 가지고 있는 템플릿 역할</h1>
 * 필드에 전략을 보관하는 방식으로 전략 패턴에서는 이것을 컨텍스트(문맥)이라 한다.<br>
 * 쉽게 이야기해서 컨텍스트(문맥)는 크게 변하지 않지만, 그 문맥 속에서 strategy 를 통해
 * 일부 전략이 변경된다 생각하면 된다.<p><p>
 *
 * `Context`는 내부에 `Strategy strategy`필드를 가지고 있다.<br>
 * 이 필드에 변하는 부분인 Strategy 의 구현체를 주입하면 된다.<br>
 * 전략 패턴의 핵심은 `Context`는 `Strategy`인터페이스에만 의존한다는 점이다.
 * 덕분에 `Strategy` 의 구현체를 변경하거나 새로 만들어도 `Context` 코드에는 영향을 주지 않는다.<p><p>
 *
 * 어디서 많이 본 코드 같지 않은가? 그렇다. 바로 스프링에서 의존관계 주입에서 사용하는 방식이 바로 전략 패턴이다.
 */
@Slf4j
public class ContextV1 {

    private Strategy strategy;

    /**
     * <h2>전략을 주입</h2>
     */
    public ContextV1(Strategy strategy) {
        this.strategy = strategy;
    }

    public void execute() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        strategy.call(); //위임
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }
}
