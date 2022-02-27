package com.example.backend.web;

import com.example.backend.entity.Carriage;
import com.example.backend.entity.Message;
import com.example.backend.repository.CarriageRepository;
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

/**
 * 负责车厢实体的增删改查功能
 */
@Api(tags = {"车厢"})
@RestController
@RequestMapping(value = {"/carriage"})
public class CarriageController {

    @Autowired
    CarriageRepository carriageRepository;

    ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping(value = {"/add"})
    String CarriageAdd(@RequestBody Carriage carriage) throws JsonProcessingException {
        if(carriageRepository.findCarriageByCid(carriage.getCid()) == null){
            logger.info("添加车厢: "+carriageRepository.save(carriage));
            return objectMapper.writeValueAsString(new Message(0));
        }else
            return objectMapper.writeValueAsString(new Message(2));

    }

    @PostMapping(value ={"/delete"})
    String CarriageDelete(int cid) throws JsonProcessingException {
        if(carriageRepository.deleteCarriageByCid(cid) == 1){
            logger.info("删除车厢: "+cid);
            return objectMapper.writeValueAsString(new Message(0));
        }else
            return objectMapper.writeValueAsString(new Message(3));
    }

    @PostMapping(value ={"/modify"})
    String CarriageModify(@RequestBody Carriage carriage) throws JsonProcessingException {
        if(carriageRepository.modifyCarriage(carriage.getCid(),carriage.getCtype(),carriage.getSeat_number()) == 1){
            logger.info("修改车厢: "+carriage);
            return objectMapper.writeValueAsString(new Message(0));
        }else
            return objectMapper.writeValueAsString(new Message(4));
    }

    @PostMapping(value ={"/query"})//接收的参数不是对象的话用x-www-form传参数
    String CarriageQuery(String cid,String ctype) throws JsonProcessingException {
    //三种查询模式：查询所有车厢、查询某种类型的车厢、查询某个车厢
        if(cid.equals("") && ctype.equals("")){
            List<Carriage> carriageList = carriageRepository.findAllByOrderByCtypeAsc();
            if(!carriageList.isEmpty()){
                StringBuilder log = new StringBuilder("检查所有车厢：\n");
                for(Carriage c:carriageList){
                    log.append(c.toString()).append("\n");
                }
                logger.info(String.valueOf(log));
                return objectMapper.writeValueAsString(new Message(0, objectMapper.writeValueAsString(carriageList)));
            }else
                return objectMapper.writeValueAsString(new Message(5));
        }else if (cid.equals("") && ctype.length()!=0){
            List<Carriage> carriageList = carriageRepository.findCarriagesByCtype(Integer.parseInt(ctype));
            if(!carriageList.isEmpty()){
                StringBuilder log = new StringBuilder("检查所有类型为"+ctype+"车厢：\n");
                for(Carriage c:carriageList){
                    log.append(c.toString()).append("\n");
                }
                logger.info(String.valueOf(log));
                return objectMapper.writeValueAsString(new Message(0, objectMapper.writeValueAsString(carriageList)));
            }else
                return objectMapper.writeValueAsString(new Message(5));
        }else {
            Carriage c = carriageRepository.findCarriageByCid(Integer.parseInt(cid));
            if(c != null){
                logger.info("检查车厢"+c);
                return objectMapper.writeValueAsString(new Message(0, objectMapper.writeValueAsString(c)));
            }else
                return objectMapper.writeValueAsString(new Message(5));
        }
    }



}
