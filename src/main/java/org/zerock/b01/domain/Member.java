package org.zerock.b01.domain;


import lombok.*;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "roleSet")
public class Member extends BaseEntity{

    @Id
    private String m_id;
    private String m_pw;
    private String m_email;
    private boolean m_del;
    private boolean m_social;

    private String m_name;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    public void changePassword(String m_pw){
        this.m_pw = m_pw;
    }

    public void changeEmail(String m_email){
        this.m_email = m_email;
    }

    public void changeDel(boolean m_del){
        this.m_del = m_del;
    }

    public void addRole(MemberRole memberRole){
        this.roleSet.add(memberRole);
    }

    public void clearRoles() {
        this.roleSet.clear();
    }

    public void changeSocial(boolean m_social){this.m_social = m_social;}
}