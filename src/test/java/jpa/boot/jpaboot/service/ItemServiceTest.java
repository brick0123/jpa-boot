package jpa.boot.jpaboot.service;

import jpa.boot.jpaboot.domain.item.Book;
import jpa.boot.jpaboot.domain.item.Item;
import jpa.boot.jpaboot.dto.BookResponseDto;
import jpa.boot.jpaboot.repository.ItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    EntityManager em;

    @Test
    void 상품_수정_불러오기() {
        // given
        String name = "JPA";
        int price = 10000;
        int stockQuantity = 10;
        String author = "한진호";
        String isbn = "111A";

        Item item = createItem(name, price, stockQuantity, author, isbn);

        // when
        itemService.saveItem(item);
        BookResponseDto one = itemService.findOne(item.getId());

        // then
        assertThat(name).isEqualTo(one.getName());
        assertThat(price).isEqualTo(one.getPrice());
        assertThat(stockQuantity).isEqualTo(one.getStockQuantity());
        assertThat(author).isEqualTo(one.getAuthor());
        assertThat(isbn).isEqualTo(one.getIsbn());
    }

    @Test
    @DisplayName("존재하지 않는 상품 조회 시 예외가 발생해야한다.")
    void 상품_조회_예외() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> itemService.findOne(1L)
        );

    }

    private Item createItem(String name, int price, int stockQuantity, String author, String isbn) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        book.setAuthor(author);
        book.setIsbn(isbn);
        em.persist(book);
        return book;
    }

}