package org.zerock.b01.dto;
import lombok.Data;

@Data
public class MemberJoinDTO {

    private String m_id;
    private String m_pw;
    private String m_email;
    private boolean m_del;
    private boolean m_social;
    private String m_name;

}
