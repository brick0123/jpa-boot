package jpa.boot.jpaboot.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import jpa.boot.jpaboot.domain.Address;
import jpa.boot.jpaboot.domain.Order;
import jpa.boot.jpaboot.domain.OrderStatus;
import jpa.boot.jpaboot.dto.OrderSearchRequestDto;
import jpa.boot.jpaboot.repository.OrderRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * xToOne (ManyToOne, OneToOne) Order Order -> Member Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

  private final OrderRepository orderRepository;

  @GetMapping("/api/v1/simple-orders")
  public List<Order> ordersV1() {
    List<Order> all = orderRepository.findAllByString(new OrderSearchRequestDto());
    return all;
  }

  @GetMapping("/api/v2/simple-orders")
  public List<SimpleOrderDto> ordersV2() {
    List<Order> orders = orderRepository.findAllByString(new OrderSearchRequestDto());

    return orders.stream()
        .map(SimpleOrderDto::new)
        .collect(Collectors.toList());
  }

  @GetMapping("/api/v3/simple-orders")
  public List<SimpleOrderDto> ordersV3() {
//    List<Order> orders = orderRepository.findAllByString(new OrderSearchRequestDto());
    List<Order> orders = orderRepository.findAllWithMemberDelivery();

    return orders.stream()
        .map(SimpleOrderDto::new)
        .collect(Collectors.toList());
  }

  @Data
  static class SimpleOrderDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public SimpleOrderDto(Order order) {
      orderId = order.getId();
      name = order.getMember().getName();
      orderDate = order.getOrderDate();
      orderStatus = order.getOrderStatus();
      address = order.getDelivery().getAddress();
    }
  }
}
