package org.zerock.b01.service;

import org.zerock.b01.dto.CartDetailDTO;

public interface CartService {

    Long addCart(CartDetailDTO cartDetailDTO, String m_email);


}
