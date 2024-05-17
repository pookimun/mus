package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.Notice;
import org.zerock.b01.repository.search.BoardSearch;
import org.zerock.b01.repository.search.NoticeSearch;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeSearch {

    @EntityGraph(attributePaths = {"imageSet"})
    // @EntityGraph에는 attributePaths라는 속성을 이용해서 같이 로딩해야 하는 속성 명시
    @Query("select n from Notice n where n.nno =:nno")
    Optional<Notice> findByIdWithImages(Long nno);

}
