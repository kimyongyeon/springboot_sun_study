package com.study.yongyeon.sunday.spring.springsunday.entity;

import lombok.*;

import javax.persistence.*;

@Entity //jpa
@Table(name = "tbl_memo") //jpa
@ToString
@Getter
@Builder
@AllArgsConstructor //@Builder Require
@NoArgsConstructor //@Builder Require
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long mno;

    @Column(length = 200, nullable = false)
    private String memoText;
}
