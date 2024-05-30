package thespeace.springAdvanced.app.v2;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thespeace.springAdvanced.trace.TraceId;
import thespeace.springAdvanced.trace.TraceStatus;
import thespeace.springAdvanced.trace.mytrace.MyTraceV2;

@Service
@RequiredArgsConstructor
public class OrderServiceV2 {

    private final OrderRepositoryV2 orderRepository;
    private final MyTraceV2 trace;

    public void orderItem(TraceId traceId, String itemId) {

        TraceStatus status = null;
        try {
            status = trace.beginSync(traceId, "OrderService.orderItem()");
            orderRepository.save(status.getTraceId(), itemId);
            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }

    }
}
