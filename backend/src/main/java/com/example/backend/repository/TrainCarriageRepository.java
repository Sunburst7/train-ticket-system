package com.example.backend.repository;

import com.example.backend.entity.Train;
import com.example.backend.entity.Train_Carriage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TrainCarriageRepository extends JpaRepository<Train_Carriage, Integer> {

    /**
     * 车厢列车联系集实体的基本删改查功能
     */
    //删除某一列车的所有车厢联系
    @Modifying
    @Transactional
    int deleteTrain_CarriagesByTid(String tid);
    //删除某个联系集实体
    @Modifying
    @Transactional
    int deleteTrain_CarriageByCidAndTid(int cid,String tid);

    @Modifying
    @Transactional
    @Query("update Train_Carriage set tid=?2,sequence=?3 where cid=?1")
    int modifyTrainCarriage(int cid,String tid,int sequence);

    //查找一列车所有的车厢id
    List<Train_Carriage> findTrain_CarriagesByTid(String tid);
    //根据cid查询某个联系集
    Train_Carriage findTrain_CarriageByCid(int cid);
    //根据列车车次和车厢次序查询某个联系集
    Train_Carriage findTrain_CarriageByTidAndSequence(String tid,int sequence);

    //查找所有联系集
    List<Train_Carriage> findAllByOrderByTidAsc();

}
