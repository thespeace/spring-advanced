package thespeace.springAdvanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * <h1>구현체</h1>
 */
@Slf4j
public class StrategyLogic1 implements Strategy{

    @Override
    public void call() {
        log.info("비즈니스 로직1 실행");
    }
}
