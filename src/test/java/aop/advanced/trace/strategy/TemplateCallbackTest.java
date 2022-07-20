package aop.advanced.trace.strategy;

import aop.advanced.trace.strategy.code.template.Callback;
import aop.advanced.trace.strategy.code.template.TimeLogTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class TemplateCallbackTest {

    /*
     * 템플릿 콜백 패턴 - 익명 내부 클래스
     *  */
    @Test
    void callbackV1() {
        TimeLogTemplate template = new TimeLogTemplate();

        template.execute(new Callback() {
            @Override
            public void call() {
                log.info("비지니스 로직1 실행");
            }
        });

        template.execute(new Callback() {
            @Override
            public void call() {
                log.info("비지니스 로직2 실행");
            }
        });
    }

    /*
     * 템플릭 콜백 패턴 - 람다
     * */
    void callbackV2() {
        TimeLogTemplate template = new TimeLogTemplate();
        template.execute(() -> log.info("비지니스 로직1 실행"));
        template.execute(() -> log.info("비지니스 로직2 실행"));
    }
}

