package hello.itemservice.domain.item;

import lombok.Getter;
import lombok.Setter;

//@Data -> 핵심 도메인에서는 매우 위험하다. , 내용 숙지 필수.
@Getter @Setter
public class Item {

    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item() {

    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }


}
