package thespeace.springAdvanced.trace;

import java.util.UUID;

/**
 * <h2>TraceId 클래스</h2>
 * 로그 추적기는 트랜잭션ID와 깊이를 표현하는 방법이 필요하다.<br>
 * 여기서는 트랜잭션ID와 깊이를 표현하는 level을 묶어서 `TraceId`라는 개념을 만들었다.<br>
 * TraceId 는 단순히 `id`(트랜잭션ID)와 `level`정보를 함께 가지고 있다.<p>
 *
 * <pre>
 *     [796bccd9] OrderController.request()     //트랜잭션ID:796bccd9, level:0
 *     [796bccd9] |-->OrderService.orderItem()  //트랜잭션ID:796bccd9, level:1
 *     [796bccd9] | |-->OrderRepository.save()  //트랜잭션ID:796bccd9, level:2
 * </pre>
 */
public class TraceId {

    private String id;
    private int level;

    public TraceId() {
        this.id = createId();
        this.level = 0;
    }

    private TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    /**
     * <h2>로그의 트랜잭션ID</h2>
     * ab99e16f-3cde-4d24-8241-256108c203a2 //생성된 UUID <br>
     * ab99e16f //앞 8자리만 사용
     */
    private String createId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * 다음 TraceId 를 만든다. 예제 로그를 잘 보면 깊이가 증가해도 트랜잭션ID는 같다. 대신에 깊이가 하나 증가한다.
     * <pre>
     *     실행 코드: new TraceId(id, level + 1)
     *
     *     [796bccd9] OrderController.request()
     *     [796bccd9] |-->OrderService.orderItem() //트랜잭션ID가 같다. 깊이는 하나 증가한다.
     * </pre>
     *
     * 따라서 createNextId() 를 사용해서 현재 TraceId 를 기반으로 다음 TraceId 를 만들면 id 는 기존과 같고,
     * level 은 하나 증가한다.
     */
    public TraceId createNextId() {
        return new TraceId(id, level + 1);
    }

    /**
     * createNextId() 의 반대 역할을 한다. id 는 기존과 같고, level 은 하나 감소한다.
     */
    public TraceId createPreviousId() {
        return new TraceId(id, level - 1);
    }

    /**
     * 첫 번째 레벨 여부를 편리하게 확인할 수 있는 메서드.
     */
    public boolean isFirstLevel() {
        return level == 0;
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }
}
