package com.example.backend.repository;

import com.example.backend.entity.Ticket;
import com.example.backend.key.TicketKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, TicketKey> {

    /**
     * 车票实体基本的删改查方法
     */
    @Modifying
    @Transactional
    int deleteTicketByUsercardAndTid(String usercard,String tid);

    //改方法在下方改签车票

    /**
     * 查询一段车的某种车票
     */
    @Query("select new Ticket(t.usercard,t.tid,t.startSid,t.arriveSid,t.cid,t.seatSequence,t.price,t.paid)  " +
            "from Ticket t,Carriage c,Train_Carriage tc " +
            "where t.tid=?3 and t.tid=tc.tid and tc.cid = c.cid and t.cid=tc.cid and" +
            "((t.startSid<=?1 and t.arriveSid>=?2) or (t.startSid>?1 and t.arriveSid<?2) or (t.startSid<=?1 and t.arriveSid>?1) or (t.startSid<?2 and t.arriveSid>=?2)) and c.ctype=?4")
    List<Ticket> findTicketsByTidAndStartSidAndArriveSidAndType(int startSid, int arriveSid, String tid,int ctype);

    List<Ticket> findTicketsByTidAndStartSidAndArriveSid(String tid,int startSid,int arriveSid);
    //查询某个车票
    Ticket findTicketByTidAndStartSidAndArriveSidAndCidAndSeatSequence(String tid,int startSid,int arriveSid,int cid,int seatSequence);

    //购票——查询某个人的车票
    Ticket findTicketByUsercardAndTidAndStartSidAndArriveSid(String usercard,String tid,int startSid,int arriveSid);
    //改签——查询某人的车票
    Ticket findTicketByUsercardAndTid(String usercard,String tid);
    //用户中心-查询某人的车票
    List<Ticket> findTicketsByUsercard(String usercard);
    //管理员查询所有车票
    List<Ticket> findAllByOrderByTidAsc();


    /**
     * 根据起始站和终点站id、列车车次、座位类型查询y已购座位数量
     * @param startSid 起始站id
     * @param arriveSid 终点站id
     * @param tid 列车车次
     * @param ctype 座位类型
     * @return 已购座位数量
     */
    @Query("select count(t.usercard) " +
            "from Ticket t,Carriage c,Train_Carriage tc " +
            "where t.tid=?3 and t.tid=tc.tid and tc.cid = c.cid and t.cid=tc.cid and" +
            "((t.startSid<=?1 and t.arriveSid>=?2) or (t.startSid>?1 and t.arriveSid<?2) or (t.startSid<=?1 and t.arriveSid>?1) or (t.startSid<?2 and t.arriveSid>=?2)) and c.ctype=?4")
    int findSeatNumber(int startSid, int arriveSid, String tid,int ctype);


    /**
     * 查询某列车次某种座位总数
     * @param tid 列车车次
     * @param ctype 座位种类
     * @return 某种座位总数
     */
    @Query(value = "select ifnull(sum(c.seat_number),0) from Carriage c,Train_Carriage tc where tc.tid=?1 and c.cid=tc.cid and c.ctype=?2",nativeQuery = true)
    int findAllSeatNumber(String tid,int ctype);



    /**
     * 改签车票,只能更改站次，座位
     * @return 1 改签成功
     */
    @Modifying
    @Transactional
    @Query("update Ticket set startSid=?3,arriveSid=?4,cid=?5,seatSequence=?6,price=?7 where usercard = ?1 and tid=?2")
    int modifyTicket(String usercard,String tid,int startSid,int arriveSid,int cid,int seatSequence,float price);

}
