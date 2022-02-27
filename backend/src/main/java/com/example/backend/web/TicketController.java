package com.example.backend.web;

import com.example.backend.entity.*;
import com.example.backend.repository.CarriageRepository;
import com.example.backend.repository.StationRepository;
import com.example.backend.repository.TicketRepository;
import com.example.backend.repository.TrainCarriageRepository;
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

import java.util.ArrayList;
import java.util.List;

/**
 * 车票实体的增删改查、推荐座位算法方法
 */
@Api(tags = {"车票"})
@RestController
@RequestMapping(value = {"/ticket"})
public class TicketController {

    private final TicketRepository ticketRepository;

    @Autowired
    TrainCarriageRepository trainCarriageRepository;

    @Autowired
    CarriageRepository carriageRepository;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    public TicketController(TicketRepository ticketRepository){
        this.ticketRepository = ticketRepository;
    }

    ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 购买车票
     */
    @ApiOperation(value = "购票", notes = "购买车票的接口")
    @PostMapping(value = {"/add"})
    String TicketAdd(String usercard,String tid,String startSname,String arriveSname,int carriageSequence,int seatSequence,float price,String paid) throws JsonProcessingException {
        Station s1 = stationRepository.findStationByTidAndSname(tid,startSname);
        Station s2 = stationRepository.findStationByTidAndSname(tid,arriveSname);
        Train_Carriage train_carriage = trainCarriageRepository.findTrain_CarriageByTidAndSequence(tid,carriageSequence);
        if(ticketRepository.findTicketByUsercardAndTid(usercard,tid) == null||ticketRepository.findTicketByUsercardAndTid(usercard,tid).getPaid() == 0){
            Ticket t;
            if(paid.equals(""))
                t = new Ticket(usercard,tid,s1.getSid(),s2.getSid(),train_carriage.getCid(),seatSequence,price);
            else
                t = new Ticket(usercard,tid,s1.getSid(),s2.getSid(),train_carriage.getCid(),seatSequence,price,1);
            logger.info("添加车票: "+ticketRepository.save(t));
            return objectMapper.writeValueAsString(new Message(0));
        }else
            return objectMapper.writeValueAsString(new Message(2));

    }

    /**
     * 退票
     */
    @ApiOperation(value = "退票", notes = "退票的接口")
    @PostMapping(value ={"/delete"})
    String TicketDelete(String usercard,String tid) throws JsonProcessingException {
        if(ticketRepository.deleteTicketByUsercardAndTid(usercard, tid) == 1){
            logger.info("删除车票: "+usercard+" "+tid);
            return objectMapper.writeValueAsString(new Message(0));
        }else
            return objectMapper.writeValueAsString(new Message(3));
    }

    /**
     * 改签
     */
    @ApiOperation(value = "改签", notes = "改签的接口，只能更改座位、站次")
    @PostMapping(value ={"/modify"})
    String TicketModify(String usercard,String tid,String startSname,String arriveSname,int carriageSequence,int seatSequence,float price) throws JsonProcessingException {
        Station s1 = stationRepository.findStationByTidAndSname(tid,startSname);
        Station s2 = stationRepository.findStationByTidAndSname(tid,arriveSname);
        Train_Carriage train_carriage = trainCarriageRepository.findTrain_CarriageByTidAndSequence(tid,carriageSequence);
        if(ticketRepository.findTicketByUsercardAndTid(usercard,tid) != null){
            float oldPrice = ticketRepository.findTicketByUsercardAndTid(usercard,tid).getPrice();
            logger.info("改签车票: "+ticketRepository.modifyTicket(usercard,tid,s1.getSid(),s2.getSid(),train_carriage.getCid(),seatSequence,price));//save可以覆盖已有的实体
            if(oldPrice - price < 0)
                return objectMapper.writeValueAsString(new Message(0,String.valueOf(price-oldPrice)));
            else
                return objectMapper.writeValueAsString(new Message(0,"0"));
        }else
            return objectMapper.writeValueAsString(new Message(4));
    }

    @PostMapping(value ={"/query"})//接收的参数不是对象的话用x-www-form传参数
    String TicketQuery(String tid,String startSid,String arriveSid,String cid,String seatSequence) throws JsonProcessingException {
        //查询一段车的车票(某车次从第2个站到第3个站)
        if(cid.equals("") && seatSequence.equals("")){
            List<Ticket> tickets = ticketRepository.findTicketsByTidAndStartSidAndArriveSid(tid,Integer.parseInt(startSid),Integer.parseInt(arriveSid));
            if(!tickets.isEmpty()){
                StringBuilder log = new StringBuilder("查询"+tid+"从"+startSid+"到"+arriveSid+"的所有车票\n");
                for(Ticket t:tickets)
                    log.append(t.toString()).append("\n");
                logger.info(String.valueOf(log));
                return objectMapper.writeValueAsString(new Message(0, objectMapper.writeValueAsString(tickets)));
            }else
                return objectMapper.writeValueAsString(new Message(5));
        }else{
            //查询某张车票
            Ticket t = ticketRepository.findTicketByTidAndStartSidAndArriveSidAndCidAndSeatSequence
                    (tid,Integer.parseInt(startSid),Integer.parseInt(arriveSid),Integer.parseInt(cid),Integer.parseInt(seatSequence));
            if(t != null){
                logger.info("查询车票"+t);
                return objectMapper.writeValueAsString(new Message(0, objectMapper.writeValueAsString(t)));
            }else
                return objectMapper.writeValueAsString(new Message(5));
        }
    }

    @PostMapping(value = {"/queryAll"})
    String TicketQueryAll() throws JsonProcessingException {
        List<Ticket> ticketList = ticketRepository.findAllByOrderByTidAsc();
        if(ticketList.isEmpty()){
            logger.info("搜索车票为空");
            return objectMapper.writeValueAsString(new Message(0,"搜索车票为空"));
        }else {
            return objectMapper.writeValueAsString(new Message(0,objectMapper.writeValueAsString(ticketList)));
        }
    }

    @ApiOperation(value = "推荐座位算法", notes = "推荐座位")
    @PostMapping(value = {"/recommend"})
    String recommendSeat(String tid,String startSname,String arriveSname,int type,int num) throws JsonProcessingException {
        logger.info("请求座位信息:"+tid+" "+startSname+" "+arriveSname+" "+tid+" "+num);
        int requireNum = num;
        String[] seat = new String[requireNum];//存放结果
        Station s1 = stationRepository.findStationByTidAndSname(tid,startSname);
        Station s2 = stationRepository.findStationByTidAndSname(tid,arriveSname);
        List<Train_Carriage> tcList = trainCarriageRepository.findTrain_CarriagesByTid(tid);//该车次所有的车厢联系集
        List<Ticket> ticketList = ticketRepository.findTicketsByTidAndStartSidAndArriveSidAndType(s1.getSid(),s2.getSid(),tid,type);//该车段所有的座位类型为type的车票
        //检测列表
        List<String> detectList = new ArrayList<>();
        for(Ticket ticket:ticketList){
            detectList.add(ticket.getCid()+","+ticket.getSeatSequence());
        }

        for(Train_Carriage train_carriage:tcList){
            if(requireNum == 0)
                break;
            Carriage carriage = carriageRepository.findCarriageByCid(train_carriage.getCid());
            if(carriage.getCtype() == type){//是需要的类型车厢
                int carriageSequence = train_carriage.getSequence();//该车厢的第次
                int allSeatNumber = carriage.getSeat_number();//该车厢的座位总数
                int[][] seatMatrix = new int[allSeatNumber/5][5];//表示车厢的二维矩阵
                int[] temp = new int[allSeatNumber/5];// 定义一个辅助数组,记录每排已购座位的个数
                //初始化该二维数组,有人做为1，空位为0
                for(int i=0;i<allSeatNumber/5;i++) {
                    temp[i] = 0;
                    for(int m=0;m<5;m++) {
                        if(detectList.contains(carriage.getCid()+","+(i*5+m+1))){
                            seatMatrix[i][m] = 1;
                            temp[i]++;
                        }
                        else
                            seatMatrix[i][m] = 0;
                    }
                }
                boolean flag = false;//连续分配是否成功
                for(int i = 0;i<allSeatNumber/5;i++){
                    if (requireNum == 0)
                        break;
                    if(requireNum>5-temp[i]){//该车厢该行座位不足够
                        continue;
                    }else {
                        int m = requireNum;
                        for(int j = 1;j<=m;j++){
                            seat[j-1] = carriageSequence+"车,"+(i+1)+"排,"+(temp[i]+j)+"座";
                            requireNum--;
                        }
                        flag = true;
                    }
                }
                if(!flag){//随机分配
                    for(int i = 0;i<allSeatNumber/5;i++){
                        if(requireNum == 0)
                            break;
                        for(int j = 0;j<5;j++){
                            if (requireNum == 0)
                                break;
                            if(seatMatrix[i][j] == 0){
                                seat[requireNum-1] = carriageSequence+"车,"+(i+1)+"排,"+(j+1)+"座";
                                requireNum--;
                            }
                        }
                    }
                }

            }

        }
        return objectMapper.writeValueAsString(new Message(0, objectMapper.writeValueAsString(seat)));
    }
}
