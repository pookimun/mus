package org.zerock.b01.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.b01.domain.*;
import org.zerock.b01.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public class NoticeSearchImpl extends QuerydslRepositorySupport implements NoticeSearch {

    public NoticeSearchImpl(){
        super(Notice.class);
    }

    @Override
    public Page<NoticeListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {

        QNotice notice = QNotice.notice;

        JPQLQuery<Notice> noticeJPQLQuery = from(notice);

        if( (types != null && types.length > 0) && keyword != null ){

            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (

            for(String type: types){

                switch (type){
                    case "t":
                        booleanBuilder.or(notice.n_title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(notice.n_content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(notice.n_writer.contains(keyword));
                        break;
                }
            }//end for
            noticeJPQLQuery.where(booleanBuilder);
        }
        noticeJPQLQuery.groupBy(notice);
        getQuerydsl().applyPagination(pageable, noticeJPQLQuery); //paging

        JPQLQuery<Notice> select = noticeJPQLQuery.select(notice);

        List<Notice> noticeList = select.fetch();

        List<NoticeListAllDTO> dtoList = noticeList.stream().map(notice1 -> {

            NoticeListAllDTO dto = NoticeListAllDTO.builder()
                    .nno(notice1.getNno())
                    .n_title(notice1.getN_title())
                    .n_writer(notice1.getN_writer())
                    .regDate(notice1.getRegDate())
                    .build();

            //NoticeImage를 NoticeImageDTO 처리할 부분
            List<NoticeImageDTO> noticeImages = notice1.getImageSet().stream().sorted()
                    .map(noticeImage -> NoticeImageDTO.builder()
                            .uuid(noticeImage.getUuid())
                            .fileName(noticeImage.getFileName())
                            .ord(noticeImage.getOrd())
                            .build()
                    ).collect(Collectors.toList());

            dto.setNoticeImages(noticeImages);
            return dto;
        }).collect(Collectors.toList());

        long totalCount = noticeJPQLQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, totalCount);
    }

}

















