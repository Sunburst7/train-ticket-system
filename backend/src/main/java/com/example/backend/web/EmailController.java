package com.example.backend.web;

import com.example.backend.entity.Message;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping(value = {"/email"})
public class EmailController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String from;

    @PostMapping("/send")
    String sendEmail(String to) throws JsonProcessingException {
        //查询此用户的信息
        User user = userRepository.findUserByUsercard(to);
        if(user != null){
            SimpleMailMessage message = new SimpleMailMessage();// 创建简单邮件消息
            message.setFrom(from);//发件人
            message.setTo(user.getEmail());//收件人
            message.setSubject("验证您的身份");//主题
            Random random = new Random();
            int verifyCode = random.nextInt();
            message.setText("您的验证码为："+verifyCode);//内容
            try{
                mailSender.send(message);
                logger.info("邮件发送成功");
                return objectMapper.writeValueAsString(new Message(0,String.valueOf(verifyCode)));
            }catch (Exception e){
                logger.error("邮件发送异常",e);
                return objectMapper.writeValueAsString(new Message(7));
            }
        }else{
            logger.error("不存在此用户"+to);
            return objectMapper.writeValueAsString(new Message(7));
        }
    }
}
