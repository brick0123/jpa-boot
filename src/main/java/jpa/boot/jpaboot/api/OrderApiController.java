package jpa.boot.jpaboot.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import jpa.boot.jpaboot.domain.Address;
import jpa.boot.jpaboot.domain.Order;
import jpa.boot.jpaboot.domain.OrderItem;
import jpa.boot.jpaboot.domain.OrderStatus;
import jpa.boot.jpaboot.dto.OrderSearchRequestDto;
import jpa.boot.jpaboot.repository.OrderRepository;
import jpa.boot.jpaboot.repository.order.query.OrderFlatDto;
import jpa.boot.jpaboot.repository.order.query.OrderQueryDto;
import jpa.boot.jpaboot.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

  private final OrderRepository orderRepository;
  private final OrderQueryRepository orderQueryRepository;

  @GetMapping("/api/v1/orders")
  public List<Order> ordersV1() {
    List<Order> all = orderRepository.findAllByString(new OrderSearchRequestDto());
    for (Order order : all) {
      order.getMember().getName();
      order.getDelivery().getAddress();

      List<OrderItem> orderItems = order.getOrderItems();
      orderItems.stream().forEach(o -> o.getItem().getName());
    }
    return all;
  }

  @GetMapping("/api/v2/orders")
  public List<OrderDto> ordersV2() {
    List<Order> orders = orderRepository.findAllByString(new OrderSearchRequestDto());
    List<OrderDto> result = orders
        .stream()
        .map(o -> new OrderDto(o))
        .collect(Collectors.toList());

    return result;
  }

  @GetMapping("/api/v3/orders")
  public List<OrderDto> ordersV3() {
    List<Order> orders = orderRepository.findAllWithItem();

    List<OrderDto> result = orders
        .stream()
        .map(o -> new OrderDto(o))
        .collect(Collectors.toList());

    return result;
  }

  @GetMapping("/api/v3.1/orders")
  public List<OrderDto> ordersV3_page(
      @RequestParam(value = "offset", defaultValue = "0") int offset,
      @RequestParam(value = "limit", defaultValue = "100") int limit
  ) {
    List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

    return orders
        .stream()
        .map(OrderDto::new)
        .collect(Collectors.toList());
  }

  @GetMapping("/api/v4/orders")
  public List<OrderQueryDto> ordersV4() {
    return orderQueryRepository.findOrderQueryDto();
  }

  @GetMapping("/api/v5/orders")
  public List<OrderQueryDto> ordersV5() {
    return orderQueryRepository.findAlByDto_optimization();
  }

  @GetMapping("/api/v6/orders")
  public List<OrderFlatDto> ordersV6() {
    return orderQueryRepository.findAlByDto_flat();
  }

  @Data
  static class OrderDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDto> orderItems;

    public OrderDto(Order order) {
      orderId = order.getId();
      name = order.getMember().getName();
      orderDate = order.getOrderDate();
      orderStatus = order.getOrderStatus();
      address = order.getDelivery().getAddress();
      orderItems = order.getOrderItems().stream()
      .map(OrderItemDto::new)
      .collect(Collectors.toList());
    }
  }

  @Getter
  static class OrderItemDto {

    private String itemName; // 상품명
    private int orderPrice; // 주문가격
    private int count; // 주문 수량

    public OrderItemDto(OrderItem orderItem) {
      itemName = orderItem.getItem().getName();
      orderPrice = orderItem.getOrderPrice();
      count = orderItem.getCount();
    }
  }
}
