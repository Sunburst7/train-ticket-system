package com.example.backend.repository;

import com.example.backend.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

public interface TrainRepository extends JpaRepository<Train,String> {

    /**
     * 火车车次实体类的基本删、改、查方法
     */
    @Modifying
    @Transactional
    int deleteTrainByTid(String tid);

    @Modifying
    @Transactional
    @Query("update Train set type=?2,start_station=?3,end_station=?4,start_date=?5,end_date=?6,state=?7 where tid=?1")
    int modifyTrain(String tid, char type, String startStation, String endStation, Date startDate,Date endDate,int state);

    List<Train> findAllByOrderByTid();

    Train findTrainByTid(String tid);

}
