package com.example.backend.repository;

import com.example.backend.entity.Station;
import com.example.backend.key.StationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public interface StationRepository extends JpaRepository<Station, StationKey> {

    /**
     * 对车站实体的基本删、改、查方法
     */
    @Modifying
    @Transactional
    int deleteStationByTidAndSid(String tid,int sid);

    @Modifying
    @Transactional
    @Query("update Station set sname=?3,arrive_date=?4,price=?5,mileage=?6 where sid=?1 and tid=?2")
    int modifyStation(int sid, String tid, String sname, Timestamp arrive_time, float price, int mileage);

    Station findStationBySidAndTid(int sid,String tid);

    Station findStationByTidAndSname(String tid,String sname);

    List<Station> findStationsByTid(String tid);

    List<Station> findAllByOrderByTidAsc();

    /**
     * 根据起始站，终点站，日期查询列车
     * @param startStation 起始站名称
     * @param endStation 终点站名称
     * @param date 日期
     * @return 返回查询到的列车车次信息
     */
    @Query("select s1.tid,s1.sname,s1.sid,s2.sname,s2.sid,s1.arrive_date,s2.arrive_date,(s2.price-s1.price),(s2.mileage-s1.mileage) from Station s1,Station s2 where s1.tid = s2.tid and s1.sname = ?1 and s2.sname = ?2 and s1.sid < s2.sid and s1.tid in(select tid from Train where state=0 and start_date=?3)")
    List<String> findStation(String startStation, String endStation, Date date);

    /**
     * 根据起始站，终点站，日期，车次查询列车
     * @param startStation 起始站名称
     * @param endStation 终点站名称
     * @param date 日期
     * @return 返回查询到的列车车次信息
     */
    @Query("select s1.tid,s1.sname,s1.sid,s2.sname,s2.sid,s1.arrive_date,s2.arrive_date,(s2.price-s1.price),(s2.mileage-s1.mileage) from Station s1,Station s2 where s1.tid = s2.tid and s1.tid=?4 and s1.sname = ?1 and s2.sname = ?2 and s1.sid < s2.sid and s1.tid in(select tid from Train where state=0 and start_date=?3)")
    List<String> findStationByTid(String startStation, String endStation, Date date, String tid);

    /**
     * 根据列车车次，查询两个站之间的经停站
     * @param tid 车次
     * @param start_sid 始发站
     * @param end_sid 到站
     * @return 返回经停站的List
     */
    List<Station> findStationByTidAndSidGreaterThanEqualAndSidLessThanEqual(String tid,int start_sid,int end_sid);

    /**
     * 根据起始站，终点站，日期查询换乘列车信息
     * @param startStation 起始站
     * @param endStation 终点站
     * @param date 发车日期
     * @return 返回换乘车辆信息
     */
    @Query("select a.tid,a.sid,a.sname,b.sid,b.sname,t1.start_date,t1.end_date,c.tid,c.sid,c.sname,d.sid,d.sname,t2.start_date,t2.end_date " +
            "from Station a,Station b,Station c,Station d,Train t1,Train t2 " +
            "where a.tid=b.tid and c.tid=d.tid and a.tid=t1.tid and c.tid=t2.tid and " +
            "a.sname=?1 and d.sname=?2 and b.sname=c.sname and " +
            "a.sid<b.sid and c.sid<d.sid and " +
            "b.arrive_date < c.arrive_date and t1.start_date=?3"
    )
    List<String> findChangeStation(String startStation, String endStation, Date date);
}
