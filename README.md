# train-ticket-system
基于Vue2与Springboot的火车售票系统
数据库课设：火车售票系统
一个功能完整的火车售票系统，该系统分为客户端与管理员端：
* 客户端功能：登录注册，查询车票、购买车票、退票
* 管理员端功能：发布相关的列车信息，后台监测，用户的审核

技术栈：
* 前端使用Vue-CLI构建Vue2项目，使用vue-router控制路由，部分组件使用ElementUI，同时利用Axios实现异步请求与响应。
* 后端采用Java+SpringBoot框架，利用Spring-Data-JPA设计持久层，MySQL存储数据

安装说明：
1. cd ./front

2. 启动前下载npm包
npm i

3. 启动前端:
npm run dev

4. 运行backend中的BackendApplication.java 启动后端

5. 别忘了导入数据库文件，但是数据库里的火车发车时间与到站时间需要修改


项目预览：

1. 管理员身份审核：
![image](https://user-images.githubusercontent.com/65580753/156556254-67c8a63a-e4f1-4165-8d42-45fc881d65a2.png)

2. 用户中心
![image](https://user-images.githubusercontent.com/65580753/156556431-396193a1-bd55-4270-924e-235e984013a6.png)

3. 购票大厅
![image](https://user-images.githubusercontent.com/65580753/156556651-c4417925-e2d8-41b4-a4bd-0a5a00316039.png)

4. 登录注册
![image](https://user-images.githubusercontent.com/65580753/156556888-e2078d1e-c334-4def-97ca-a42960724640.png)


