package aop.advanced.trace.logtrace;

import aop.advanced.trace.TraceId;
import aop.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FieldLogTrace implements  LogTrace{

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    private  TraceId traceHolder; // traceId 동기화할 곳 동시성이슈 생길 수 있음


    @Override
    public TraceStatus begin(String message) {
        syncTraceId();
        TraceId traceId = traceHolder;
        Long startTimeMs = System.currentTimeMillis();
        //로그 출력
        log.info("[{}]{}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    private void syncTraceId() {
        if(traceHolder == null) {
            traceHolder = new TraceId();
        } else {
            traceHolder = traceHolder.createNextId();
        }
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);

    }

    @Override
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);

    }

    private void complete(TraceStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();

        if (e == null) {
            log.info("[{}]{}{} time={}ms",
                    traceId.getId(),
                    addSpace(COMPLETE_PREFIX,
                            traceId.getLevel()),
                    status.getMessage(),
                    resultTimeMs);
        }else  {
            log.info("[{}]{}{} time={}ms ex={}",
                    traceId.getId(),
                    addSpace(EX_PREFIX,
                            traceId.getLevel()),
                    status.getMessage(),
                    resultTimeMs,
                    e.toString());
        }
    }

    // 들어갔다가 마지막에 나오는것 로그가 끝난다는것
    private void releaseTraceId() {
        if(traceHolder.isFirstLevel()) {
            traceHolder = null; // destroy
        } else {
            traceHolder = traceHolder.createPreviousId();
        }
    }

    private static String addSpace(String prefix, int level) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < level; i++) {
            sb.append((i == level - 1 ) ? "|" + prefix : "|  ");
        }
        return sb.toString();
    }
}
