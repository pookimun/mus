package org.zerock.b01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.b01.domain.BoardImage;

public interface BoardImageRepository extends JpaRepository<BoardImage, String> {

}
