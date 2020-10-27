package jpa.boot.jpaboot.service;

import jpa.boot.jpaboot.domain.Delivery;
import jpa.boot.jpaboot.domain.Member;
import jpa.boot.jpaboot.domain.Order;
import jpa.boot.jpaboot.domain.OrderItem;
import jpa.boot.jpaboot.domain.item.Item;
import jpa.boot.jpaboot.repository.ItemRepository;
import jpa.boot.jpaboot.repository.MemberRepository;
import jpa.boot.jpaboot.repository.OrderRepository;
import jpa.boot.jpaboot.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id = " + itemId));

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();
    }

    // 검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);

    }
}
