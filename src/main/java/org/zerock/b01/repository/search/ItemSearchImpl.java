package org.zerock.b01.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import org.zerock.b01.domain.Item;

import org.zerock.b01.domain.QItem;

import java.util.List;

public class ItemSearchImpl extends QuerydslRepositorySupport implements ItemSearch {
    public ItemSearchImpl(){
        super(Item.class);

    }
    @Override
    public Page<Item>search1(Pageable pageable){
        QItem item=QItem.item; //Q 도메인 객체
        JPQLQuery<Item> query = from(item); //select
        query.where(item.i_name.contains("1")); //where 타이틀
        //paging
        this.getQuerydsl().applyPagination(pageable,query);
        List<Item> list=query.fetch();
        long count =query.fetchCount();
        return null;
    }

    @Override
        public Page<Item> searchAll(String[] types, String keyword, Pageable pageable) {

        QItem item=QItem.item;
        JPQLQuery<Item> query = from(item); //select

        if( (types != null && types.length > 0) && keyword != null ){ //검색 조건과 키워드가 있다면

            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (

            for(String type: types){

                switch (type){
                    case "n":
                        booleanBuilder.or(item.i_name.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(item.i_color.contains(keyword));
                        break;
                    case "s":
                        booleanBuilder.or(item.i_size.contains(keyword));
                        break;
                }
            }//end for
            query.where(booleanBuilder);
        }//end if

        //bno > 0
        query.where(item.ino.gt(0L));

        //paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<Item> list = query.fetch();

        long count = query.fetchCount();

        return new PageImpl<>(list, pageable, count);

    }
}
