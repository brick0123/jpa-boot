package jpa.boot.jpaboot.service;

import jpa.boot.jpaboot.domain.Address;
import jpa.boot.jpaboot.domain.Member;
import jpa.boot.jpaboot.domain.Order;
import jpa.boot.jpaboot.domain.OrderStatus;
import jpa.boot.jpaboot.domain.item.Book;
import jpa.boot.jpaboot.domain.item.Item;
import jpa.boot.jpaboot.exception.NotEnoughStockException;
import jpa.boot.jpaboot.repository.ItemRepository;
import jpa.boot.jpaboot.repository.MemberRepository;
import jpa.boot.jpaboot.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    void 상품주문() {
        // given
        Member member = createMember();

        Item book = createItem("시골 JPA", 10000, 10);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        Order getOrder = orderRepository.findOne(orderId);

        // then

        // 주문 상품 주문 상태는 ORDER
        assertThat(getOrder.getOrderStatus()).isEqualTo(OrderStatus.ORDER);
        // 주문한 상품 종류의 개수가 동일해야 된다
        assertThat(getOrder.getOrderItems().size()).isEqualTo(1);
        // 주문 가격은 가격 + 수량이다
        assertThat(getOrder.getTotalPrice()).isEqualTo(10000 * orderCount);
        // 주문 수량만큼 재고가 줄어야 한다
        assertThat(book.getStockQuantity()).isEqualTo(8);
    }

    @Test()
    void 상품주문_재고수량초과() {
        // given
        Member member = createMember();
        Item item = createItem("시골 JPA", 10000, 10);

        int orderCount = 11;

        // when
        Assertions.assertThrows(NotEnoughStockException.class,
                () -> orderService.order(member.getId(), item.getId(), orderCount),
                "재고 수량 부족 예외가 발생해야 한다.");
    }

    @Test
    void 주문취소() {
        // given
        Member member = createMember();
        Item book = createItem("시골 JPA", 10000, 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        // when
        orderService.cancelOrder(orderId);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertThat(getOrder.getOrderStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(book.getStockQuantity()).isEqualTo(10);
    }


    private Item createItem(String name, int price, int stockQuantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("member1");
        Address address = new Address("seoul", "gang-su", "12345");
        member.setAddress(address);
        em.persist(member);
        return member;
    }

}