package thespeace.springAdvanced.trace.strategy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import thespeace.springAdvanced.trace.strategy.code.strategy.ContextV2;
import thespeace.springAdvanced.trace.strategy.code.strategy.Strategy;
import thespeace.springAdvanced.trace.strategy.code.strategy.StrategyLogic1;
import thespeace.springAdvanced.trace.strategy.code.strategy.StrategyLogic2;

@Slf4j
public class ContextV2Test {

    /**
     * <h2>전략 패턴 적용</h2>
     * Context 와 Strategy 를 '선 조립 후 실행'하는 방식이 아니라
     * Context 를 실행할 때 마다 전략을 인수로 전달한다.<br>
     * 클라이언트는 Context 를 실행하는 시점에 원하는 Strategy 를 전달할 수 있다.
     * 따라서 이전 방식과 비교해서 원하는 전략을 더욱 유연하게 변경할 수 있다.<br>
     * 테스트 코드를 보면 하나의 Context 만 생성한다.
     * 그리고 하나의 Context 에 실행 시점에 여러 전략을 인수로 전달해서 유연하게 실행하는 것을 확인할 수 있다.
     */
    @Test
    void strategyV1() {
        ContextV2 context = new ContextV2();
        context.execute(new StrategyLogic1());
        context.execute(new StrategyLogic2());
    }

    /**
     * <h2>전략 패턴 익명 내부 클래스 1</h2>
     * 여기도 물론 익명 내부 클래스를 사용할 수 있다.<br>
     * 코드 조각을 파라미터로 넘긴다고 생각하면 더 자연스럽다.
     */
    @Test
    void strategyV2() {
        ContextV2 context = new ContextV2();
        context.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        });
        context.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직2 실행");
            }
        });
    }

    /**
     * <h2>전략 패턴 익명 내부 클래스 2, 람다</h2>
     */
    @Test
    void strategyV3() {
        ContextV2 context = new ContextV2();
        context.execute(() -> log.info("비즈니스 로직1 실행"));
        context.execute(() -> log.info("비즈니스 로직2 실행"));
    }


}
