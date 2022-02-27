package com.example.backend.web;

import com.example.backend.entity.Message;
import com.example.backend.entity.Station;
import com.example.backend.entity.Train;
import com.example.backend.repository.StationRepository;
import com.example.backend.repository.TicketRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 车站控制器类,负责车站的增删改查、根据起始站，终点站，日期查询列车信息、换乘
 */
@Api(tags = {"列车经停站"})
@RestController
@RequestMapping(value = {"/station"})
public class StationController {

    @Autowired
    StationRepository stationRepository;

    @Autowired
    TicketRepository ticketRepository;

    ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping(value = {"/add"})
    String StationAdd(@RequestBody Station station) throws JsonProcessingException {
        if(stationRepository.findStationBySidAndTid(station.getSid(),station.getTid()) == null ){
            logger.info("添加经停站: "+stationRepository.save(station));
            return objectMapper.writeValueAsString(new Message(0));
        }else
            return objectMapper.writeValueAsString(new Message(2));
    }

    @PostMapping(value = {"/delete"})
    String StationDelete(String tid,int sid) throws JsonProcessingException {
        if(stationRepository.deleteStationByTidAndSid(tid, sid) == 1){
            logger.info("删除经停站: "+tid+" "+sid);
            return objectMapper.writeValueAsString(new Message(0));
        }else
            return objectMapper.writeValueAsString(new Message(3));
    }

    @PostMapping(value = {"/modify"})
    String StationModify(@RequestBody Station station) throws JsonProcessingException {
        if(stationRepository.modifyStation(station.getSid(),station.getTid(),station.getSname(),station.getArrive_date(),station.getPrice(),station.getMileage()) == 1){
            logger.info("修改经停站: "+station);
            return objectMapper.writeValueAsString(new Message(0));
        }else
            return objectMapper.writeValueAsString(new Message(4));
    }

    @ApiOperation(value = "管理员查询")
    @PostMapping(value = {"/query"})
    String StationQuery(String tid,String sid)throws JsonProcessingException{
        //查询一个车次所有经停站
        if(sid.equals("")){
            List<Station> stationList = stationRepository.findStationsByTid(tid);
            if(!stationList.isEmpty())
            {
                StringBuilder log = new StringBuilder("检查"+tid+"次列车所有经停站：\n");
                for(Station s:stationList)
                    log.append(s.toString()).append("\n");
                logger.info(String.valueOf(log));
                return objectMapper.writeValueAsString(new Message(0, objectMapper.writeValueAsString(stationList)));
            }else
                return objectMapper.writeValueAsString(new Message(5));

        }else {
            //查询某个站
            Station station = stationRepository.findStationBySidAndTid(Integer.parseInt(sid),tid);
            if(station != null){
                logger.info("查询列车"+station);
                return objectMapper.writeValueAsString(new Message(0, objectMapper.writeValueAsString(station)));
            }else
                return objectMapper.writeValueAsString(new Message(5));
        }
    }

    //查询所有停靠站
    @PostMapping(value = {"/queryAll"})
    String StationQueryAll() throws JsonProcessingException {
        List<Station> stationList = stationRepository.findAllByOrderByTidAsc();
        if(stationList.isEmpty()){
            logger.info("搜索停靠站为空");
            return objectMapper.writeValueAsString(new Message(0,"搜索停靠站为空"));
        }else {
            return objectMapper.writeValueAsString(new Message(0,objectMapper.writeValueAsString(stationList)));
        }
    }

    /**
     *根据起始站，终点站，日期，（列车车次）查询列车信息
     */
    @ApiOperation(value = "用户查询", notes = "根据起始站，终点站，日期，（列车车次）查询列车信息")
    @PostMapping(value = {"/specificQuery"})
    String SpecificQuery(String startStation, String endStation, Date date, String tid) throws JsonProcessingException {
        List<String> stationInfo;
        if(tid == null){
            stationInfo = stationRepository.findStation(startStation,endStation,date);
        }else{
            stationInfo = stationRepository.findStationByTid(startStation,endStation,date,tid);
        }

        if(!stationInfo.isEmpty()){
            //查询列车座位情况
            for(String s:stationInfo){
                List<String> temp = new ArrayList<>(Arrays.asList(s.split(",")));
                for(int i = 0;i<4;i++){
                    int total = ticketRepository.findAllSeatNumber(temp.get(0),i);
                    int remain = total-ticketRepository.findSeatNumber(Integer.parseInt(temp.get(2)),Integer.parseInt(temp.get(4)),temp.get(0),i);
                    temp.add(Integer.toString(remain));
                }
                //将原来的信息调整为带有座位数的信息
                stationInfo.set(stationInfo.indexOf(s),temp.toString());
            }
            StringBuilder log = new StringBuilder("查询"+date+"从"+startStation+"到"+endStation+"的列车信息\n");
            for(String s:stationInfo)
                log.append(s).append("\n");
            logger.info(String.valueOf(log));
            return objectMapper.writeValueAsString(new Message(0, objectMapper.writeValueAsString(stationInfo)));
        } else
            return objectMapper.writeValueAsString(new Message(5));
    }

    /**
     * 根据列车车次，查询两个站之间的经停站,
     */
    @ApiOperation(value = "查询经过站", notes = "根据列车车次，查询两个站之间的经停站")
    @PostMapping(value = {"/transferQuery"})
    String TransferQuery(String tid,int start_sid,int end_sid) throws JsonProcessingException {
        List<Station> stationList = stationRepository.findStationByTidAndSidGreaterThanEqualAndSidLessThanEqual(tid, start_sid, end_sid);

        if(!stationList.isEmpty()){
            StringBuilder log = new StringBuilder("查询"+tid+"次列车"+start_sid+"到"+end_sid+"之间的中转站\n");
            for(Station s:stationList)
                log.append(s.toString()).append("\n");
            logger.info(String.valueOf(log));
            return objectMapper.writeValueAsString(new Message(0, objectMapper.writeValueAsString(stationList)));
        }else
            return objectMapper.writeValueAsString(new Message(5));
    }

    /**
     * 根据起始站，终点站，日期查询换乘列车信息
     */
    @ApiOperation(value = "换乘查询", notes = "根据起始站，终点站，日期查询换乘列车信息")
    @PostMapping(value = {"/changeQuery"})
    String ChangeQuery(String startStation, String endStation, Date date) throws JsonProcessingException {
        List<String> changeInfo = stationRepository.findChangeStation(startStation,endStation,date);
        if(!changeInfo.isEmpty()){
            StringBuilder log = new StringBuilder("查询"+date+"从"+startStation+"到"+endStation+"的换乘列车");
            for(String s:changeInfo)
                log.append(s.toString()).append("\n");
            logger.info(String.valueOf(log));
            return objectMapper.writeValueAsString(new Message(0, objectMapper.writeValueAsString(changeInfo)));
        }else
            return objectMapper.writeValueAsString(new Message(5));
    }
}
