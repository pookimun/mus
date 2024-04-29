package org.zerock.b01.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Getter
@Setter
@ToString
public class Cart extends BaseEntity{

    @Id
    @Column
    private Long c_no;

    @Column(nullable = false)
    private int c_count;

    @Column(nullable = false)
    private String c_size;

    @Column(nullable = false)
    private String c_color;

    @Column(nullable = false)
    private Date c_date;




}
