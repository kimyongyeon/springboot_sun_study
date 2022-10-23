package com.study.yongyeon.sunday.spring.springsunday.repository;

import com.study.yongyeon.sunday.spring.springsunday.entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemoRepositoryTest {
    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass(){
        System.out.println("get name Repository" + memoRepository.getClass().getName());
    }
    @Test
    public void Insert테스트_사전작업으로_Dummy_추가(){
        //given
        IntStream
            .rangeClosed(1,100)
            .forEach( i ->
                //then
                memoRepository.save(Memo.builder().memoText("Sample : "+i).build())
            );
    }

    @Test
    public void select테스트_FindById(){
        //when
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);
        System.out.println("=======================");

        //then
        result.ifPresent(System.out::println);
    }

    @Transactional
    @Test
    public void select테스트_getById(){
        //when
        Long mno = 100L;

        Memo result = memoRepository.getById(mno);
        System.out.println("=======================");

        //then
        System.out.println(result);
    }

    @Test
    public void update테스트(){
        //when
        Memo memo = Memo.builder().mno(100L).memoText("update Text").build();

        //then
        System.out.println(memoRepository.save(memo));
    }

    @Test
    public void delete테스트(){
        //when
        Long mno = 100L;

        //then
        memoRepository.deleteById(mno);
    }

    @Test
    public void PageDefault테스트(){
        //given
        Pageable pageable = PageRequest.of(0,10);
        Page<Memo> result = memoRepository.findAll(pageable);

        //then
        System.out.println(result);
        System.out.println("=======================");
        System.out.println("getTotalPages : "       + result.getTotalPages());
        System.out.println("getTotalElements : "    + result.getTotalElements());
        System.out.println("getNumber : "           + result.getNumber());
        System.out.println("getSize : "             + result.getSize());

        System.out.println("hasNext? : "            + result.hasNext());
        System.out.println("isFirst? : "            + result.isFirst());
    }

    @Test
    public void PageDefault테스트_반복(){
        //given
        Pageable pageable = PageRequest.of(0,10);
        Page<Memo> result = memoRepository.findAll(pageable);

        //then
        result.forEach(System.out::println);
    }

    @Test
    public void Sort테스트(){
        //given
        Sort orderByMnoDesc = Sort.by("mno").descending();
        Sort orderByMemoAsc = Sort.by("memoText").ascending();

        Sort sortAll = orderByMnoDesc.and(orderByMemoAsc);

        Pageable pageable = PageRequest.of(0, 10, sortAll);

        //then
        memoRepository.findAll(pageable).forEach(System.out::println);
    }

    @Test
    public void QueryMethod_select테스트_Naming() {
        List<Memo> memoList = memoRepository.findByMnoBetweenOrderByMnoDesc(70L, 80L);
        memoList.forEach(System.out::println);
    }

    @Test
    public void QueryMethod_select테스트_Pageble(){
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("mno").descending());
        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageRequest);
        result.get().forEach(System.out::println);
    }
    @Transactional
    @Commit
    @Test
    public void QueryMethod_delete테스트(){
        memoRepository.deleteMemoByMnoLessThan(10L);
    }
}
