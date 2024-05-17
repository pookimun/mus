package org.zerock.b01.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.Notice;
import org.zerock.b01.dto.BoardListAllDTO;
import org.zerock.b01.dto.BoardListReplyCountDTO;
import org.zerock.b01.dto.NoticeListAllDTO;

public interface NoticeSearch {

    Page<NoticeListAllDTO> searchWithAll(String[] types,
                                         String keyword,
                                         Pageable pageable);

}
