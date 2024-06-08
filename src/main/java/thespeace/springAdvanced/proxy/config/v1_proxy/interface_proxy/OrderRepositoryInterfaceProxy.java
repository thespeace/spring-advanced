package thespeace.springAdvanced.proxy.config.v1_proxy.interface_proxy;

import lombok.RequiredArgsConstructor;
import thespeace.springAdvanced.proxy.app.v1.OrderRepositoryV1;
import thespeace.springAdvanced.trace.TraceStatus;
import thespeace.springAdvanced.trace.logtrace.LogTrace;

/**
 * <ul>
 *     <li>프록시를 만들기 위해 인터페이스를 구현하고 구현한 메서드에 LogTrace 를 사용하는 로직을 추가한다.
 *         지금까지는 OrderRepositoryImpl 에 이런 로직을 모두 추가해야했다. 프록시를 사용한 덕분에 이 부분을
 *         프록시가 대신 처리해준다. 따라서 OrderRepositoryImpl 코드를 변경하지 않아도 된다.</li>
 *     <li>OrderRepositoryV1 target : 프록시가 실제 호출할 원본 리포지토리의 참조를 가지고 있어야 한다.</li>
 * </ul>
 */
@RequiredArgsConstructor
public class OrderRepositoryInterfaceProxy implements OrderRepositoryV1 {

    private final OrderRepositoryV1 target;
    private final LogTrace logTrace;

    @Override
    public void save(String itemId) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("OrderRepository.request()");

            //target 호출
            target.save(itemId);
            logTrace.end(status);

        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
