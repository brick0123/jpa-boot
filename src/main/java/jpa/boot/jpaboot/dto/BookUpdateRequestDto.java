package jpa.boot.jpaboot.dto;

import jpa.boot.jpaboot.domain.item.Book;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // test
public class BookUpdateRequestDto {

    private String name;
    private int price;
    private int stockQuantity;

    @Builder
    public BookUpdateRequestDto(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}
