package com.study.yongyeon.sunday.spring.springsunday.repository;

import com.study.yongyeon.sunday.spring.springsunday.entity.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long>{
    //Read
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);
    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

    //delete
    void deleteMemoByMnoLessThan(Long num);

    //JPQL
    @Query("select m from Memo m order by m.mno desc")
    List<Memo> getListDesc();




}
