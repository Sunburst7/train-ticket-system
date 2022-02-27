package com.example.backend.repository;

import com.example.backend.entity.Carriage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CarriageRepository extends JpaRepository<Carriage, Integer> {

    /**
     * 对车厢实体的删改查方法
     */

    @Modifying
    @Transactional
    int deleteCarriageByCid(int cid);

    @Modifying
    @Transactional
    @Query("update Carriage set ctype=?2,seat_number=?3 where cid=?1")
    int modifyCarriage(int cid,int ctype,int seat_number);

    Carriage findCarriageByCid(int cid);


    //根据车厢种类查询所有车厢
    List<Carriage> findCarriagesByCtype(int ctype);

    //按照车厢类型排序，输出所有车厢
    List<Carriage> findAllByOrderByCtypeAsc();


}
