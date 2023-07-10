package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes, Model model) {

        //검증 오류 결과 보관 객체
        Map<String, String> errors = new HashMap<>();

        //검증 로직
        if(!StringUtils.hasText(item.getItemName())) {
            //아이템에 글자가 없는 경우
            errors.put("itemName", "상품 이름은 필수입니다!!");
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            //가격이 없거나 너무 작거나 혹은 너무 큰 경우
            errors.put("price", "상품 가격은 1000원 이상, 1000000 이하의 값을 허용합니다.");
        }
        if(item.getQuantity() == null || item.getQuantity() >= 9999) {
            errors.put("quantity", "수량은 9,999미만의 값을 허용합니다.");
        }

        //복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null) {
            int result = item.getPrice() * item.getQuantity();
            if(result < 10000) {
                errors.put("globalError","주문 가격은 10,000원 이상이어야 합니다. 현재 값 = "+result);
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if(hasError(errors)) {
            model.addAttribute("errors", errors);
            return "validation/v2/addForm";
        }
        
        //검증 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    private static boolean hasError(Map<String, String> errors) {
        return !errors.isEmpty();
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

