package org.zerock.b01.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersPageRequestDTO {
    // 주문내역 조회 시 기간, 상품명&브랜드명으로 검색 기능
    // 페이징 기능
    // order_detail도 같이 가져온다.
    // 위의 기능들을 수행하기 위한 요청 dto

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    private String keyword;

    public Pageable getPageable(String...props) { // 정렬
        return PageRequest.of(this.page -1, this.size, Sort.by(props).descending());
    }

    private String link; // 쿼리스트링

    public String getLink() { // 해당 페이지의 쿼리스트링을 만들어 리턴하는 메서드

        if(link == null){
            StringBuilder builder = new StringBuilder();

            builder.append("page=" + this.page);

            builder.append("&size=" + this.size);


            if(keyword != null){
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword,"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                }
            }
            link = builder.toString();
        }

        return link;
    }



}
