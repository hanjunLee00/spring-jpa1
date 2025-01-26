package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter
@Setter
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    //Cascade 속성으로 한 번에 모두 저장 및 삭제 가능 (persist 전파하는 원리)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) //Many 로 끝나는 매핑관계는 기본 패치 전략이 LAZY
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY) //One 으로 끝나는 매핑관계는 기본 패치 전략이 EAGER => 패치타입을 바꿔주는게 좋음
    @JoinColumn(name="delivery_id")
    private Delivery delivery;

    private LocalDateTime deliveryTime;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문상태 [ORDER, CANCEL]

    //==연관관계 메서드==// 양방향 관계일 때, 위치는 핵심적으로 컨트롤하는쪽으로
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void  addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }
}
