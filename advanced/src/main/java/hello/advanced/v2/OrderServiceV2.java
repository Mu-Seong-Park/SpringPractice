package hello.advanced.v2;

import hello.advanced.hellotrace.HelloTraceV1;
import hello.advanced.hellotrace.HelloTraceV2;
import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV2 {

    private final OrderRepositoryV2 repository;
    private final HelloTraceV2 trace;

    public void orderItem(TraceId beforeId, String itemId) {
        TraceStatus status = null;
        try{
            status = trace.beginSync(beforeId,"OrderService.orderItem()");
            repository.save(status.getTraceId(),itemId);
            trace.end(status);
        } catch (Exception e) {
            trace.exception(status,e);
            throw e;// 예외를 다시 꼭 던져주어야 한다.
        }

    }

}
