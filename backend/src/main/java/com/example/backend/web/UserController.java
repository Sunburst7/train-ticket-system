package com.example.backend.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.backend.entity.*;
import com.example.backend.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户控制器类，负责用户的注册，登录，修改信息，验证身份等
 */
@Api(tags = {"用户"})
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    TrainCarriageRepository trainCarriageRepository;

    @Autowired
    CarriageRepository carriageRepository;

    @Autowired
    StationRepository stationRepository;

    ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping(value = {"/login"})//http请求中的参数不是一个对象，不需要加@RequestBody
    String UserLogin(String usercard,String password) throws JsonProcessingException {
        User user = userRepository.findByUsercardAndPassword(usercard,password);
        if(user == null)
            return objectMapper.writeValueAsString(new Message(1));
        else {
            logger.info("登录成功: "+user);
            return objectMapper.writeValueAsString(new Message(0, objectMapper.writeValueAsString(user)));
        }
    }

    @PostMapping(value = {"/register"})
    String UserRegister(@RequestBody User user) throws JsonProcessingException{
        if(userRepository.findByUsercardAndPassword(user.getUsercard(),user.getPassword()) == null){
            logger.info("注册用户: "+userRepository.save(user));
            return objectMapper.writeValueAsString(new Message(0));
        }else
            return objectMapper.writeValueAsString(new Message(2));
    }

    @PostMapping(value = {"/modify"})
    String UserModify(@RequestBody User user) throws JsonProcessingException{
        //修改成功返回1
        if(userRepository.modifyByUsercard(user.getUsercard(), user.getUid(), user.getPassword(), user.getEmail(), user.getPhone(), user.getBirth(), user.getIdentity(), user.getVerified()) == 1){
            logger.info("修改成功: "+user);
            return objectMapper.writeValueAsString(new Message(0));
        }else
            return objectMapper.writeValueAsString(new Message(4));
    }

    @PostMapping(value = {"/queryAll"})
    String UserQueryAll() throws JsonProcessingException {
        List<User> userList = userRepository.findAllByOrderByUsercardAsc();
        if(userList.isEmpty()){
            logger.info("搜索用户为空");
            return objectMapper.writeValueAsString(new Message(0,"搜索用户为空"));
        }else {
            return objectMapper.writeValueAsString(new Message(0,objectMapper.writeValueAsString(userList)));
        }
    }

    @PostMapping(value = {"/tickets"})
    String UserTickets(String usercard) throws JsonProcessingException{
        List<Ticket> ticketList = ticketRepository.findTicketsByUsercard(usercard);
        JSONArray jsonArray = new JSONArray();
        for(Ticket t:ticketList){
            List<Train_Carriage> train_carriages = trainCarriageRepository.findTrain_CarriagesByTid(t.getTid());
            int sequence = 0;

            for(Train_Carriage tc:train_carriages){
                if(tc.getCid() == t.getCid())
                    sequence = tc.getSequence();
            }
            Carriage c = carriageRepository.findCarriageByCid(t.getCid());
            String type = "";
            switch (c.getCtype()){
                case 0:
                    type = "商务座";
                    break;
                case 1:
                    type = "一等座";
                    break;
                case 2:
                    type = "二等座";
                    break;
                case 3:
                    type = "卧铺";
                    break;
            }
            String seatInfo = sequence+"车"+((t.getSeatSequence()-1)/5+1)+"排"+t.getSeatSequence()%5+"座";
            Station s1 = stationRepository.findStationBySidAndTid(t.getStartSid(),t.getTid());
            Station s2 = stationRepository.findStationBySidAndTid(t.getArriveSid(),t.getTid());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("trainID",t.getTid());
            jsonObject.put("startStation",s1.getSname());
            jsonObject.put("endStation",s2.getSname());
            jsonObject.put("startDate",s1.getArrive_date().toString());
            jsonObject.put("endDate",s2.getArrive_date().toString());
            jsonObject.put("type",type);
            jsonObject.put("seatInfo",seatInfo);
            jsonObject.put("price",t.getPrice());
            jsonObject.put("paid",t.getPaid());
            jsonArray.add(jsonObject);
        }
        logger.info(String.valueOf(jsonArray));
        return objectMapper.writeValueAsString(new Message(0, objectMapper.writeValueAsString(jsonArray)));
    }

    @PostMapping("/avatar")
    public String uploadAvatarHandler(@RequestParam("avatar") MultipartFile uploadFile,@RequestParam("usercard") String usercard) throws IOException {
        //获得项目的类路径
        String path = "E:\\idea files\\train\\front\\static\\user";
        //空文件夹在编译时不会打包进入target中
        File uploadDir = new File(path);
        if (!uploadDir.exists()) {
            System.out.println("上传头像路径不存在，正在创建...");
            uploadDir.mkdir();
        }
        if ( uploadFile != null) {
            //获得上传文件的文件名
            String oldName = uploadFile.getOriginalFilename();
            String newName = usercard+oldName.substring(oldName.indexOf('.'),oldName.length());
            System.out.println("[上传的文件名]：" + newName);
            //我的文件保存在static目录下的avatar/user
            File avatar = new File(path , newName);
            try {
                //保存图片
                uploadFile.transferTo(avatar);
                //返回成功结果，附带文件的相对路径
                return newName;
            }catch (IOException e) {
                e.printStackTrace();
                return "上传失败";
            }
        }else {
            System.out.println("上传的文件为空");
            return "文件传输错误";
        }

    }


}
