package org.zerock.b01.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.zerock.b01.constant.ItemSellStatus;
import org.zerock.b01.dto.ItemFormDTO;
import org.zerock.b01.exception.OutOfStockException;


@Entity
@Table(name = "item")
@Getter @Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item extends BaseEntity{

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ino; //상품  (pk)

    @Column(nullable = false) // not null
    @NotEmpty
    private String i_name; //상품명

    @Column(length = 100, nullable = false) // not null
    private int i_price; //상품 가격

    @Column(nullable = false)
    private String i_color;

    @Column(nullable = false)
    private String i_size;

    @Column(nullable = false)
    private int i_stock; //재고보유여부

    @Lob // Large Object -> CLOB, BLOB 타입으로 매핑 가능
    @Column(nullable = false)
    private String itemDetail; // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //상품 판매 상태


    public void updateItem(ItemFormDTO itemFormDto){
        this.i_name = itemFormDto.getI_name();
        this.i_price = itemFormDto.getI_price();
        this.i_stock = itemFormDto.getI_stock();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber){
        int restStock = this.i_stock - stockNumber;
        if(restStock<0){
            throw new OutOfStockException("상품의 재고가 부족 합니다. (현재 재고 수량: " + this.i_stock + ")");
        }
        this.i_stock = restStock;
    }

    public void addStock(int stockNumber){
        this.i_stock += stockNumber;
    }
}
