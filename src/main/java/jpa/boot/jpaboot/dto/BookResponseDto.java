package jpa.boot.jpaboot.dto;


import jpa.boot.jpaboot.domain.item.Book;
import jpa.boot.jpaboot.domain.item.Item;
import lombok.Getter;

@Getter
public class BookResponseDto {

    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;

    public BookResponseDto(Book entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.price = entity.getPrice();
        this.stockQuantity = entity.getStockQuantity();
        this.author = entity.getAuthor();
        this.isbn = entity.getIsbn();
    }
}
