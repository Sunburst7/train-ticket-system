package com.example.backend.web;

import com.example.backend.entity.Message;
import com.example.backend.entity.Train;
import com.example.backend.entity.Train_Carriage;
import com.example.backend.repository.TrainCarriageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

/**
 * 对列车-车厢联系集的增删改查功能
 */
@Api(tags = {"列车-车厢联系集"})
@RestController
@RequestMapping(value = {"/train_carriage"})
public class TrainCarriageController {

    @Autowired
    TrainCarriageRepository trainCarriageRepository;

    ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping(value = {"/add"})
    String tcAdd(@RequestBody Train_Carriage tc) throws JsonProcessingException {
        if(trainCarriageRepository.findTrain_CarriageByCid(tc.getCid()) == null){
            logger.info("添加车厢: "+trainCarriageRepository.save(tc));
            return objectMapper.writeValueAsString(new Message(0));
        }else
            return objectMapper.writeValueAsString(new Message(2));

    }

    @PostMapping(value ={"/delete"})
    String tcDelete(String tid,String cid) throws JsonProcessingException {
        if(cid.equals("")){
            //删除某一列车所有的车厢联系
            if(trainCarriageRepository.deleteTrain_CarriagesByTid(tid) == 1){
                logger.info("删除"+tid+"次列车所有车厢联系");
                return objectMapper.writeValueAsString(new Message(0));
            }else
                return objectMapper.writeValueAsString(new Message(3));
        }else {
            //删除某个车厢列车联系集实体
            if(trainCarriageRepository.deleteTrain_CarriageByCidAndTid(Integer.parseInt(cid),tid) == 1){
                logger.info("删除"+tid+"的车厢"+cid);
                return objectMapper.writeValueAsString(new Message(0));
            }else
                return objectMapper.writeValueAsString(new Message(3));
        }
    }

    @PostMapping(value ={"/modify"})
    String tcModify(@RequestBody Train_Carriage tc) throws JsonProcessingException {
        if(trainCarriageRepository.modifyTrainCarriage(tc.getCid(),tc.getTid(),tc.getSequence()) ==1){
            logger.info("修改列车-车厢联系: "+tc);
            return objectMapper.writeValueAsString(new Message(0));
        }else
            return objectMapper.writeValueAsString(new Message(4));
    }

    @PostMapping(value ={"/query"})//接收的参数不是对象的话用x-www-form传参数
    String tcQuery(String tid,String cid) throws JsonProcessingException {
        if(tid.equals("") && !cid.equals("")){
            //根据cid查询某个联系集
            Train_Carriage tc = trainCarriageRepository.findTrain_CarriageByCid(Integer.parseInt(cid));
            if(tc != null){
                logger.info("查询列车-车厢联系："+tc);
                return objectMapper.writeValueAsString(new Message(0, objectMapper.writeValueAsString(tc)));
            }else
                return objectMapper.writeValueAsString(new Message(5));
        }
        else if(cid.equals("") && !tid.equals("")){
            //查找一列车所有的列车-车厢
            List<Train_Carriage> tcList = trainCarriageRepository.findTrain_CarriagesByTid(tid);
            if(!tcList.isEmpty()){
                StringBuilder log = new StringBuilder("查询"+tid+"的所有车厢\n");
                for(Train_Carriage tc:tcList)
                    log.append(tc.toString()).append("\n");
                logger.info(String.valueOf(log));
                return objectMapper.writeValueAsString(new Message(0, objectMapper.writeValueAsString(tcList)));
            }else
                return objectMapper.writeValueAsString(new Message(5));
        }else
            return objectMapper.writeValueAsString(new Message(6));
    }

    @PostMapping(value = {"/queryAll"})
    String tcQueryAll() throws JsonProcessingException {
        List<Train_Carriage> tcList = trainCarriageRepository.findAllByOrderByTidAsc();
        if(tcList.isEmpty()){
            logger.info("搜索列车-车厢联系集为空");
            return objectMapper.writeValueAsString(new Message(0,"搜索列车-车厢联系集为空"));
        }else {
            return objectMapper.writeValueAsString(new Message(0,objectMapper.writeValueAsString(tcList)));
        }
    }
}
