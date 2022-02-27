package com.example.backend;

import com.example.backend.entity.Carriage;
import com.example.backend.entity.Station;
import com.example.backend.entity.Ticket;
import com.example.backend.entity.Train_Carriage;
import com.example.backend.repository.*;
import com.example.backend.web.TicketController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    CarriageRepository carriageRepository;

    @Autowired
    TrainCarriageRepository trainCarriageRepository;

    @Test
    void contextLoads() {
//        System.out.println(userRepository.modifyByUsercard
//                ("340403200101142610",
//                        "sunburst7",
//                        "12345",
//                        "12345@qq.com",
//                        "13605544506",
//                        new Date(2001-1900,0,15),0));
//        System.out.println(userRepository.findByUsercardAndPassword("340403200101142610","12345"));
//
//        List<String> s = stationRepository.findStation("北京","徐州",new Date(2021-1900,7,9));
//        for(String str:s)
//            System.out.println(str);
//
//        List<Station> stations = stationRepository.findStationByTidAndSidGreaterThanEqualAndSidLessThanEqual("K1153",1,3);
//        for(Station station:stations)
//            System.out.println(station);
//
//        System.out.println(ticketRepository.findSeatNumber(2,3,"K1153",4));
//        System.out.println(ticketRepository.findAllSeatNumber("K1153",4));
//        System.out.println(ticketRepository.modifyTicket("100000000000000000","K1153",1,4,1,1, 301.6F));
//
//        System.out.println(ticketRepository.save(new Ticket("000000000000000000","K1153",1,3,"test2",1,2,200.0F)));
//        System.out.println(ticketRepository.save(new Ticket("000000000000000000","K1153",1,3,"test2",1,2,200.0F)));
//        System.out.println(ticketRepository.deleteTicketByUsercardAndTid("000000000000000000","K1153"));
//        System.out.println(ticketRepository.findTicketsByTidAndStartSidAndArriveSid("K1153",1,4));
//
//        List<Carriage> carriageList = carriageRepository.findAllByOrderByCtypeAsc();
//        for(Carriage carriage:carriageList){
//            System.out.println(carriage);
//        }
//
//        List<String> changeList = stationRepository.findChangeStation("北京","福州",new Date(2021-1900,7,9));
//        for(String str:changeList)
//            System.out.println(str);
//        System.out.println(trainCarriageRepository.save(new Train_Carriage(1,"G1",1)));
        System.out.println(ticketRepository.save(new Ticket("100000000000000001","G1",1,5,1,1,1500,0)));

    }

}
