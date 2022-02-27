package com.example.backend.repository;

import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,String>, Serializable {

    User findUserByUsercard(String usercard);

    /**
     * 登录查找
     * @param usercard
     * @return null身份证号或密码错误
     */
    User findByUsercardAndPassword(String usercard,String password);

    List<User> findAllByOrderByUsercardAsc();

    //注册使用save()方法

    /**
     *  修改用户信息
     * @return 1修改成功 0修改不成功
     */
    @Query("update User set uid=?2,password=?3,email=?4,phone=?5,birth=?6,identity=?7,verified=?8 where usercard = ?1")
    @Modifying
    @Transactional
    int modifyByUsercard(String usercard,
                         String uid, String password, String email, String phone, Date birth,int identity,int verified);
}
