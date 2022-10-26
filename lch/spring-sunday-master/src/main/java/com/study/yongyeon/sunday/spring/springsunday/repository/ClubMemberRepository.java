package com.study.yongyeon.sunday.spring.springsunday.repository;

import com.study.yongyeon.sunday.spring.springsunday.entity.ClubMember;
import com.study.yongyeon.sunday.spring.springsunday.entity.ClubMemberSocial;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, String> {
    /**
     * entity graph
     * https://blog.leocat.kr/notes/2019/05/26/spring-data-using-entitygraph-to-customize-fetch-graph
     */
    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query(value = "select cm from ClubMember cm where cm.email =:email and cm.fromSocial =:social")
    Optional<ClubMember> findByEmail(@Param("email") String email, @Param("social") ClubMemberSocial social);
}
