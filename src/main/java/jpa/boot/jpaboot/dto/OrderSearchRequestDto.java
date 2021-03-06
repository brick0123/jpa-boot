package jpa.boot.jpaboot.dto;

import jpa.boot.jpaboot.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearchRequestDto {

    private String memberName;
    private OrderStatus orderStatus;
}
