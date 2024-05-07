package org.zerock.b01.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "item")
public class ItemImage implements Comparable<ItemImage> {
    // Comparable<BoardImage> @OneToMany 처리에서 순번에 맞게 정렬하기 위함
    // JPA에서 게시글을 중심으로 해석하는지, 첨부파일 중심으로 해석하는지에 따라서 다른 결과가 나옴
    // @ManyToOne을 이용하는 것은 게시물+댓글 관계임 -> 다른 엔티티 객체의 참조로 fk를 가지는 쪽에서 하는 방식(Reply)
    // @OneToMany는 하나의 게시물은 많은 이미지를 가진다. 로 해석 -> pk를 가진 쪽에서 사용함.(Board)
    // 1. 상위 엔티티에서 하위 엔티티를 관리한다.
    // 2. JPA를 상위 엔티티 기준으로 생성한다. , 하위 엔티티에 대한 Repository의 생성이 잘못된 것은 아니지만 하위 엔티티들의 변경은 상위 엔티티에도 반영 되어야 함.
    // 3. 상위 엔티티 상태가 변경되면 하위 엔티티들의 상태들도 같이 처리
    // 4. 상위 엔티티 하나와 하위 엔티티 여러개를 처리하는 경우 N+1문제가 발생됨(주의)

    @Id
    private String uuid;

    private String fileName;

    private int ord;

    @ManyToOne
    private Item item;


    @Override
    public int compareTo(ItemImage other) {
        // 파일의 순서를 정렬한다.
        return this.ord - other.ord;
    }

    public void changeItem(Item item){
        // board 객체 삭제시 참조 변경용
        this.item = item;
    }

}
