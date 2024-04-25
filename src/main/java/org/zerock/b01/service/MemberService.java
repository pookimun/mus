package org.zerock.b01.service;

import org.zerock.b01.dto.MemberJoinDTO;

public interface MemberService {
    static class M_idExistException extends Exception {
    }

    void join(MemberJoinDTO memberJoinDTO)throws M_idExistException ;
}