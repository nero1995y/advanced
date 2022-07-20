package aop.advanced.trace.strategy.code.stategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StrategyLogic1 implements Strategy{
    @Override
    public void call() {
        log.info("비지니스 로직1 실행");
    }
}
