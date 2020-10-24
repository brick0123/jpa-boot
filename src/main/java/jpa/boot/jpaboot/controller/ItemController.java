package jpa.boot.jpaboot.controller;

import jpa.boot.jpaboot.domain.item.Book;
import jpa.boot.jpaboot.domain.item.Item;
import jpa.boot.jpaboot.dto.BookResponseDto;
import jpa.boot.jpaboot.dto.BookUpdateRequestDto;
import jpa.boot.jpaboot.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form) {
        Book book = new Book();
        book.setName(form.getName());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getAuthor());
        book.setStockQuantity(form.getStockQuantity());
        book.setPrice(form.getPrice());

        itemService.saveItem(book);

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

//        BookForm form = new BookForm();
//        form.setId(item.getId());
//        form.setName(item.getName());
//        form.setPrice(item.getPrice());
//        form.setStockQuantity(item.getStockQuantity());
//        form.setAuthor(item.getAuthor());
//        form.setIsbn(item.getIsbn());

        model.addAttribute("form", responseDto);
        return "items/updateItemForm";
    }

    @PostMapping("items/{itemId}/edit")
    public String updateItem(@PathVariable("itemId") Long itemId, @ModelAttribute("form") BookUpdateRequestDto requestDto) {

//        itemService.saveItem();
        return "redirect:/items";
    }
}
