package com.example.backend.web;

import com.example.backend.entity.Message;
import com.example.backend.entity.Ticket;
import com.example.backend.entity.Train;
import com.example.backend.repository.TrainRepository;
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

import java.util.List;

@Api(tags = {"列车"})
@RestController
@RequestMapping(value = {"/train"})
public class TrainController {

    @Autowired
    TrainRepository trainRepository;

    ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping(value = {"/add"})
    String TrainAdd(@RequestBody Train train) throws JsonProcessingException {
        if(trainRepository.findTrainByTid(train.getTid()) == null){
            logger.info("添加列车: "+trainRepository.save(train));
            return objectMapper.writeValueAsString(new Message(0));
        }else
            return objectMapper.writeValueAsString(new Message(2));

    }

    @PostMapping(value ={"/delete"})
    String TrainDelete(String tid) throws JsonProcessingException {
        if(trainRepository.deleteTrainByTid(tid) == 1){
            logger.info("删除列车: "+tid);
            return objectMapper.writeValueAsString(new Message(0));
        }else
            return objectMapper.writeValueAsString(new Message(3));
    }

    @PostMapping(value ={"/modify"})
    String TrainModify(@RequestBody Train t) throws JsonProcessingException {
        if(trainRepository.modifyTrain
                (t.getTid(),t.getType(),t.getStart_station(),t.getEnd_station(),t.getStart_date(),t.getEnd_date(),t.getState()) == 1){
            logger.info("修改列车: "+t);
            return objectMapper.writeValueAsString(new Message(0));
        }else
            return objectMapper.writeValueAsString(new Message(4));
    }

    @PostMapping(value ={"/query"})//接收的参数不是对象的话用x-www-form传参数
    String TrainQuery(String tid) throws JsonProcessingException {
        Train t = trainRepository.findTrainByTid(tid);
        if(t != null){
            logger.info("搜索列车"+t);
            return objectMapper.writeValueAsString(new Message(0, objectMapper.writeValueAsString(t)));
        }else
            return objectMapper.writeValueAsString(new Message(5));
    }

    @PostMapping(value = {"/queryAll"})
    String TrainQueryAll() throws JsonProcessingException {
        List<Train> trainList = trainRepository.findAllByOrderByTid();
        if(trainList.isEmpty()){
            logger.info("搜索列车为空");
            return objectMapper.writeValueAsString(new Message(0,"搜索列车为空"));
        }else {
            return objectMapper.writeValueAsString(new Message(0,objectMapper.writeValueAsString(trainList)));
        }
    }
}
