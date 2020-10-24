package jpa.boot.jpaboot.service;


import jpa.boot.jpaboot.domain.*;
import jpa.boot.jpaboot.domain.item.Book;
import jpa.boot.jpaboot.domain.item.Item;
import jpa.boot.jpaboot.dto.BookResponseDto;
import jpa.boot.jpaboot.dto.BookUpdateRequestDto;
import jpa.boot.jpaboot.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public Long update(Long id, BookUpdateRequestDto requestDto) {
        Item item = itemRepository.findOne(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id = " + id));
        item.update(requestDto.getName(), requestDto.getPrice(), requestDto.getStockQuantity());
        return id;
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public BookResponseDto findOne(Long id) {
        Item item = itemRepository.findOne(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 없습니다. id = " + id));
        return new BookResponseDto((Book)item);
    }
}
