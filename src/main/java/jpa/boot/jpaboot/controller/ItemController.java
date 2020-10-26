package jpa.boot.jpaboot.controller;

import jpa.boot.jpaboot.domain.item.Book;
import jpa.boot.jpaboot.domain.item.Item;
import jpa.boot.jpaboot.dto.BookResponseDto;
import jpa.boot.jpaboot.dto.BookSaveRequestDto;
import jpa.boot.jpaboot.dto.BookUpdateRequestDto;
import jpa.boot.jpaboot.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookSaveRequestDto());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(@ModelAttribute BookSaveRequestDto requestDto) {

        itemService.saveItem(requestDto.toEntity());
        // TODO 해결하기

        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long id, Model model) {
//        Book item = (Book)itemService.findOne(itemId);
        BookResponseDto responseDto = itemService.findOne(id);

        model.addAttribute("form", responseDto);
        return "items/updateItemForm";
    }

    @PostMapping("items/{itemId}/edit")
    public String updateItem(@PathVariable("itemId") Long id, @ModelAttribute("form") BookUpdateRequestDto requestDto) {

        itemService.update(id, requestDto);
        return "redirect:/items";
    }
}
