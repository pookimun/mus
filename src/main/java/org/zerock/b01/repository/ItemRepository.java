package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b01.domain.Item;
import org.zerock.b01.repository.search.ItemSearch;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item,Long>,ItemSearch {
//    @Query (value = "select now()",nativeQuery = true)
  //  String getTime();

    @EntityGraph (attributePaths = {"itemImageSet"})
    @Query("select i from Item i where i.ino =:ino")
    Optional<Item> findByIdWithImages(Long ino);
}
