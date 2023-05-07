package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;


@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {
    private final ItemRepository itemRepository;

    //
//    @Autowired
//    public BasicItemController(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    }

    @GetMapping
    public String items(Model model) {
        List<Item> items =  itemRepository.findAll();
        model.addAttribute("items",items);
        return "basic/items";
    }

    /**
     * 테스트용 데이터
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA",10000,10));
        itemRepository.save(new Item("itemB",20000,20));
    }


    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {


        return "basic/addForm";
    }

   // @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item",item);

        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {
//@ModelAttribute는 2가지를 같이 처리(모델 객체를 만들어주고, 자동으로 뷰에 넣어주는 역할까지)
//위의 model 파라미터도 생략가능.
        itemRepository.save(item);
//        model.addAttribute("item",item); 자동추가로 생략가능.

        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        //name을 생략하면
        //첫 글자를 소문자로 바꾸고 만들어준다.
        //HelloData면 helloData, Item 이면 item
        itemRepository.save(item);
        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV4(Item item) {
        //우리가 만든 클래스면 modelattribute 생략가능,
        //primitive 타입은 어노테이션을 생략하면 RequestParam이 붙는다.
        itemRepository.save(item);
        return "basic/item";
    }


    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId,item);

        return "redirect:/basic/items/{itemId}";
    }

    //PRG 패턴, Post redirect Get -> Post add가 새로고침 되엇을 때 여러번 호출되는 것을 막을 수 있다.
    //@PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/"+item.getId();
    }

    //
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item saveItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId",saveItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }

}
