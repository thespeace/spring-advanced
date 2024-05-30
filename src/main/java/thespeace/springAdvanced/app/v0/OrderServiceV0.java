package thespeace.springAdvanced.app.v0;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service //컴포넌트 스캔의 대상이 된다.
@RequiredArgsConstructor
public class OrderServiceV0 {

    private final OrderRepositoryV0 orderRepository;

    /**
     * 실무에서는 복잡한 비즈니스 로직이 서비스 계층에 포함되지만, 예제에서는 단순함을 위해서 리포지토리에
     * 저장을 호출하는 코드만 있다.
     */
    public void orderItem(String itemId) {
        orderRepository.save(itemId);
    }
}
