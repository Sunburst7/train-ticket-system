<template>
  <div id="admin-outer">
    <div class="admin-inner1" :style="consoleVisible">
<!--      控制台-->
      <div class="card">
        <div class="card-top">
          <div class="button1 green"></div>
          <div class="button1 yellow"></div>
          <div class="button1 red"></div>
          <div class="button2" @click="switchUI(1)"><img src="../assets/switch.svg"></div>
        </div>
        <div class="card-bottom" id="father">
          <div class="line editLine" @click="getPoint">
            <p class="time">{{nowTime}}</p>
            <p class="identity">admin></p>
            <p class="infoNormal" id="editP" contenteditable="true" style="overflow: hidden" @keyup.enter="execute" @keyup.up="getLastCommand" spellcheck="false"></p>
          </div>
        </div>
      </div>
    </div>
    <div class="admin-inner2" :style="logVisible">
<!--      日志显示台-->
      <div class="card">
        <div class="card-top">
          <div class="button1 green"></div>
          <div class="button1 yellow"></div>
          <div class="button1 red"></div>
          <div class="button2" @click="switchUI(2)"><img src="../assets/switch.svg"></div>
        </div>
        <div class="card-bottom">
          <div class="logWindow">
            {{myLog}}
          </div>
        </div>
      </div>
    </div>
    <el-dialog
        title="选择你要读取的日志"
        :visible.sync="dialogVisible"
        width="30%"
        :before-close="handleClose">
        <input type="file" id="fileInput"></input>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="importInfo">确 定</el-button>
        </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios';
let basePost = axios.create({
  method: 'post',
  headers:{
    "Access-Control-Allow-Origin":"*",
  },
  baseURL:'http://localhost:8090',
})
export default {
  name: "Admin",
  data(){
    return{
      consoleVisible:{display:'inline'},
      logVisible:{display:'none'},
      dialogVisible:false,
      myLog:'',
      commandList:['/help','/open','/set',
        '/train/add(tid,type,startStation,endStation,startDate,endDate,state)','/train/delete(tid)','/train/modify(tid,type,startStation,endStation,startDate,endDate,state)','/train/query(tid)','/train/all',
        '/train_carriage/add(cid,tid,sequence)','/train_carriage/delete(tid,cid)','/train_carriage/modify(cid,tid,sequence)','/train_carriage/query(tid,cid)','/train_carriage/all',
        '/station/add(sid,tid,sname,arrive_date,price,mileage)','/station/delete(tid,sid)','/station/modify(sid,tid,sname,arrive_date,price,mileage)','/station/query(tid,sid)','/station/all',
        '/user/login(usercard,password)','/user/modify(usercard,uid,uname,password,email,phone,birth)','/user/register(usercard,uid,uname,password,email,phone,birth)','/user/tickets(usercard)','/user/all',
        '/carriage/add(cid,ctype,seat_number)','/carriage/delete(cid)','/carriage/modify(cid,ctype,seat_number)','/carriage/query(cid,ctype)',
        '/ticket/add(usercard,tid,startSid,arriveSid,cid,seatSequence,price)','/ticket/delete(usercard,tid)','/ticket/modify(usercard,tid,startSid,arriveSid,cid,seatSequence,price)','/ticket/query(tid,startSid,arriveSid,cid,seatSequence)','/ticket/all'
      ],
      instructions:['打印所有命令(/help-命令 打印具体命令说明)','打开文件选择器选择日志','修改欢迎界面最新消息(格式为/set-message(1-5):xxx)',
        '添加类型为type的tid次列车,startDate从startStation出发,endDate到达endStation,时间格式为yyyy-mm-dd,state=0','按照tid删除某一列车','按照tid修改某一列车车次,时间格式为yyyy-mm-dd','按照tid查询某一列车','查询所有列车',
        '给tid次列车组装上车厢号为cid的车厢，该车厢排在第sequence位','卸下tid次列车的cid号车厢','修改某个列车的车厢','查询某个列车的车厢，若cid为空查询tid次列车所有的车厢联系集','查询所有列车装载车厢的关系',
        '添加tid次列车的一个停靠站,站次为sid,距离始发站mileage公里,时间的格式为yyyy-MM-dd HH:mm:ss','删除tid次列车上站次为sid的停靠站','修改tid次列车的一个停靠站','sid为空-查询一个车次所有经停站;不为空查询某个站','查询所有停靠站',
        '检测用户身份证与密码是否匹配','修改用户信息','添加一个新用户','查询某个用户所有车票','查询所有用户',
        '添加一个车厢,ctype(0:商务车,1:一等,2:二等,3:卧铺)','根据车厢id删除一个车厢','修改一个车厢信息','三种查询模式：查询所有车厢(都为空)、查询某种类型的车厢(cid为空)、查询某个车厢(都不为空)',
        '添加一个车票,tid次列车，从第startSid到第arriveSid，座位在cid号车厢的第sequence座','退票','改签,只能更改座位、站次','cid与sequence为空查询列车某段所有车票，不为空查询某张车票','查询所有车票'
      ],
      lastCommand:'',
      nowTime:'',
    }
  },
  methods:{
    //输入框获取焦点
    getPoint(){
      //获得焦点
      document.getElementById('editP').focus();
    },

    //切换控制台到日志栏
    switchUI(type){
      if(type === 1){
        //从控制台到日志栏
        this.logVisible.display = 'inline'
        this.consoleVisible.display = 'none'
      }else {
        //从日志栏到控制台
        this.logVisible.display = 'none'
        this.consoleVisible.display = 'inline'
      }
    },

    //选择文件关闭提示
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        })
        .catch(_ => {});
    },

    //按下方向上键获得上一步的指令
    getLastCommand(){
      document.getElementById('editP').innerText = this.lastCommand;
    },

    //按下回车键执行命令
    execute(){
      let editLine =  document.getElementById('editP')
      let command = document.getElementById('editP').innerText.trim()//获取命令,去除头尾的空白符（包括换行符）
      this.lastCommand = command;
      this.createDialog('admin',command,'Normal')
//////////////////////////////////////////////////////////////////////////////////
      if(command.match('/help')){
        let twoPart = command.split('-')
        if(twoPart.length === 1){
          //查询所有命令
          this.createDialog('system','command-list: ','Normal')
          for(let i = 0;i<this.commandList.length;i++){
            this.createText(" "+this.commandList[i],'Success')
            this.createText("   "+this.instructions[i],'Success')
          }
        }else{
          //查询某个命令
          let index = this.commandList.findIndex((elements,index,array) =>{
            return elements.match(twoPart[1]);
          })
          if(index >= 0){
            this.createText(" "+this.commandList[index],'Success')
            this.createText("   "+this.instructions[index],'Success')
          }else
            this.createDialog('system','There is no such command, please enter /help to check','Warning')
        }
      }else if(command === '/open'){
        this.dialogVisible = true
      }else if(command.match('/set')){
        let index = command.substr(12,1)
        let message = command.substring(command.indexOf(':')+1,command.length)
        let target = command.substring(command.indexOf('-')+1,command.indexOf(':'))
        localStorage.setItem(target,message)
        if(localStorage.getItem(target) === message)
          this.createDialog('system',target+'修改成功','Success')
        else
          this.createDialog('system','修改失败','Warning')
      }
      else if(command.match('/train/add')){
        let data = command.substring(command.indexOf('(')+1,command.indexOf(')')).split(',')
        console.log(data)
        basePost.post('/train/add',{
          tid:data[0],
          type:data[1],
          start_station:data[2],
          end_station:data[3],
          start_date:data[4],
          end_date:data[5],
          state:data[6]
        })
        .then(successResponse=>{
          if(successResponse.status === 200 && successResponse.data.type===0)
            this.createDialog('system',successResponse.data.description,'Success')
          else
            this.createDialog('system',successResponse.data.description,'Warning')
        })
        .catch(failResponse=>{
          this.createDialog('system','错误参数输入','Fail')
        })
      }else if(command.match('/train/delete')){
        let data = command.substring(command.indexOf('(')+1,command.indexOf(')'))
        basePost.post('/train/delete','tid='+data)
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type===0)
              this.createDialog('system',successResponse.data.description,'Success')
            else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }else if(command.match('/train/modify')){
        let data = command.substring(command.indexOf('(')+1,command.indexOf(')')).split(',')
        basePost.post('/train/modify',{
          tid:data[0],
          type:data[1],
          start_station:data[2],
          end_station:data[3],
          start_date:data[4],
          end_date:data[5],
          state:data[6]
        })
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type===0)
              this.createDialog('system',successResponse.data.description,'Success')
            else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }else if(command.match('/train/query')){
        let data = command.substring(command.indexOf('(')+1,command.indexOf(')'))
        basePost.post('/train/query','tid='+data)
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type===0)
              this.createText("  "+successResponse.data.description,'Success')
            else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }else if(command === '/train/all'){
        basePost.post('/train/queryAll')
        .then(successResponse=>{
          if(successResponse.status === 200 && successResponse.data.type === 0 && successResponse.data.description !== '搜索列车为空'){
            let array = JSON.parse(successResponse.data.description)
            for(let i=0;i<array.length;i++)
              this.createText(JSON.stringify(array[i]),'Success')
          }else
            this.createDialog('system',successResponse.data.description,'Warning')
        })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      else if(command.match('/train_carriage/add')){
        let data = command.substring(command.indexOf('(')+1,command.indexOf(')')).split(',')
        basePost.post('/train_carriage/add',{
          cid:data[0],
          tid:data[1],
          sequence:data[2]
        })
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type===0)
              this.createDialog('system',successResponse.data.description,'Success')
            else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }else if(command.match('/train_carriage/delete')){
        let data = command.substring(command.indexOf('(')+1,command.indexOf(')')).split(',')
        basePost.post('/train_carriage/delete','tid='+data[0]+'&cid='+data[1])
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type===0)
              this.createDialog('system',successResponse.data.description,'Success')
            else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }else if(command.match('/train_carriage/modify')){
        let data = command.substring(command.indexOf('(')+1,command.indexOf(')')).split(',')
        basePost.post('/train_carriage/modify',{
          cid:data[0],
          tid:data[1],
          sequence:data[2]
        })
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type===0)
              this.createDialog('system',successResponse.data.description,'Success')
            else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }else if(command.match('/train_carriage/query')){
        let data = command.substring(command.indexOf('(')+1,command.indexOf(')')).split(',')
        basePost.post('/train_carriage/query','tid='+data[0]+'&cid='+data[1])
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type===0){
              let array = JSON.parse(successResponse.data.description)
              for(let i=0;i<array.length;i++){
                this.createText(" "+JSON.stringify(array[i]),'Success')
              }
            }
            else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }else if(command === '/train_carriage/all'){
        basePost.post('/train_carriage/queryAll')
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type === 0 && successResponse.data.description !== '搜索列车-车厢联系集为空'){
              let array = JSON.parse(successResponse.data.description)
              for(let i=0;i<array.length;i++)
                this.createText(" "+JSON.stringify(array[i]),'Success')
            }else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      else if(command.match('/station/add')){
        let data = command.substring(command.indexOf('(')+1,command.indexOf(')')).split(',')
        basePost.post('/station/add',{
          sid:data[0],
          tid:data[1],
          sname:data[2],
          arrive_date:data[3],
          price:data[4],
          mileage:data[5]
        })
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type===0)
              this.createDialog('system',successResponse.data.description,'Success')
            else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }else if(command.match('/station/delete')){
        let data = command.substring(command.indexOf('(')+1,command.indexOf(')')).split(',')
        basePost.post('/station/delete','tid='+data[0]+'&sid='+data[1])
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type===0)
              this.createDialog('system',successResponse.data.description,'Success')
            else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }else if(command.match('/station/modify')){
        let data = command.substring(command.indexOf('(')+1,command.indexOf(')')).split(',')
        basePost.post('/station/modify',{
          sid:data[0],
          tid:data[1],
          sname:data[2],
          arrive_date:data[3],
          price:data[4],
          mileage:data[5]
        })
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type===0)
              this.createDialog('system',successResponse.data.description,'Success')
            else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }else if(command.match('/station/query')){
        let data = command.substring(command.indexOf('(')+1,command.indexOf(')')).split(',')
        basePost.post('/station/query','tid='+data[0]+'&sid='+data[1])
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type===0){
              let array = JSON.parse(successResponse.data.description)
              for(let i=0;i<array.length;i++){
                this.createText(" "+JSON.stringify(array[i]),'Success')
              }
            }
            else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }else if(command === '/station/all'){
        basePost.post('/station/queryAll')
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type === 0 && successResponse.data.description !== '搜索停靠站为空'){
              let array = JSON.parse(successResponse.data.description)
              for(let i=0;i<array.length;i++)
                this.createText(" "+JSON.stringify(array[i]),'Success')
            }else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      else if(command.match('/user/register')){
        let data = command.substring(command.indexOf('(')+1,command.indexOf(')')).split(',')
        basePost.post('/user/register',{
          usercard:data[0],
          uid:data[1],
          uname:data[2],
          password:data[3],
          email:data[4],
          phone:data[5],
          birth:data[6],
          identity:data[7]
        })
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type===0)
              this.createDialog('system',successResponse.data.description,'Success')
            else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }else if(command.match('/user/tickets')){
        let data = command.substring(command.indexOf('(')+1,command.indexOf(')'))
        basePost.post('/user/tickets','usercard='+data)
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type===0){
              let array = JSON.parse(successResponse.data.description)
              for(let i=0;i<array.length;i++){
                this.createText(" "+array[i],'Success')
              }
            }
            else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }else if(command.match('/user/modify')){
        let data = command.substring(command.indexOf('(')+1,command.indexOf(')')).split(',')
        basePost.post('/user/modify',{
          usercard:data[0],
          uid:data[1],
          uname:data[2],
          password:data[3],
          email:data[4],
          phone:data[5],
          birth:data[6],
          identity:data[7]
        })
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type===0)
              this.createDialog('system',successResponse.data.description,'Success')
            else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }else if(command.match('/user/login')){
        let data = command.substring(command.indexOf('(')+1,command.indexOf(')')).split(',')
        basePost.post('/user/login','usercard='+data[0]+'&password='+data[1])
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type===0){
                this.createText(" "+successResponse.data.description,'Success')
            }
            else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }else if(command === '/user/all'){
        basePost.post('/user/queryAll')
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type === 0 && successResponse.data.description !== '搜索用户为空'){
                let array = JSON.parse(successResponse.data.description)
                for(let i=0;i<array.length;i++)
                  this.createText(" "+JSON.stringify(array[i]),'Success')

            }else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      else if(command.match('/carriage/add')){
        let data = command.substring(command.indexOf('(')+1,command.indexOf(')')).split(',')
        basePost.post('/carriage/add',{
          cid:data[0],
          ctype:data[1],
          seat_number:data[2]
        })
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type===0)
              this.createDialog('system',successResponse.data.description,'Success')
            else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }else if(command.match('/carriage/delete')){
        let data = command.substring(command.indexOf('(')+1,command.indexOf(')'))
        basePost.post('/carriage/delete','cid='+data[0])
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type===0)
              this.createDialog('system',successResponse.data.description,'Success')
            else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }else if(command.match('/carriage/modify')){
        let data = command.substring(command.indexOf('(')+1,command.indexOf(')')).split(',')
        basePost.post('/carriage/modify',{
          cid:data[0],
          ctype:data[1],
          seat_number:data[2]
        })
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type===0)
              this.createDialog('system',successResponse.data.description,'Success')
            else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }else if(command.match('/carriage/query')){
        let data = command.substring(command.indexOf('(')+1,command.indexOf(')')).split(',')
        basePost.post('/carriage/query','cid='+data[0]+'&ctype='+data[1])
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type===0){
              let array = JSON.parse(successResponse.data.description)
              console.log(array)
              if(data[1] === ''){
                this.createText(" "+JSON.stringify(array),'Success')
              }else {
                for(let i=0;i<array.length;i++){
                  this.createText(" "+JSON.stringify(array[i]),'Success')
                }
              }
            }
            else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      else if(command.match('/ticket/add')){
        let data = command.substring(command.indexOf('(')+1,command.indexOf(')')).split(',')
        basePost.post('/ticket/add',{
          usercard:data[0],
          tid:data[1],
          startSid:data[2],
          arriveSid:data[3],
          cid:data[4],
          seatSequence:data[5],
          price:data[6]
        })
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type===0)
              this.createDialog('system',successResponse.data.description,'Success')
            else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }else if(command.match('/ticket/delete')){
        let data = command.substring(command.indexOf('(')+1,command.indexOf(')')).split(',')
        basePost.post('/ticket/delete','usercard='+data[0]+'&tid='+data[1])
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type===0)
              this.createDialog('system',successResponse.data.description,'Success')
            else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }else if(command.match('/ticket/modify')){
        let data = command.substring(command.indexOf('(')+1,command.indexOf(')')).split(',')
        basePost.post('/ticket/modify',{
          usercard:data[0],
          tid:data[1],
          startSid:data[2],
          arriveSid:data[3],
          cid:data[4],
          seatSequence:data[5],
          price:data[6]
        })
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type===0)
              this.createDialog('system',successResponse.data.description,'Success')
            else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }else if(command.match('/ticket/query')){
        let data = command.substring(command.indexOf('(')+1,command.indexOf(')')).split(',')
        basePost.post('/ticket/query','tid='+data[0]+'&startSid='+data[1]+'&arriveSid='+data[2]+'&cid='+data[3]+'&seatSequence='+data[4])
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type===0){
              let array = JSON.parse(successResponse.data.description)
              for(let i=0;i<array.length;i++){
                this.createText(" "+JSON.stringify(array[i]),'Success')
              }
            }
            else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }else if(command === '/ticket/all'){
        basePost.post('/ticket/queryAll')
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type === 0&& successResponse.data.description !== '搜索车票为空'){
              let array = JSON.parse(successResponse.data.description)
              for(let i=0;i<array.length;i++)
                this.createText(" "+JSON.stringify(array[i]),'Success')
            }else
              this.createDialog('system',successResponse.data.description,'Warning')
          })
          .catch(failResponse=>{
            this.createDialog('system','错误参数输入','Fail')
          })
      }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      else {
        this.createDialog('system','There is no such command, please enter /help to check','Warning')
      }


      //清空输入行
      editLine.innerText = ''
      //获得焦点
      document.getElementById('editP').focus();
    },

    //获取文件选择器的内容到日志打印窗口
    importInfo(){
      let file = document.getElementById('fileInput').files[0]
      this.dialogVisible = false;
      this.$message({//搜索成功
        message: file.name+" 读取成功",
        type: 'success'
      })
      if(file){//存在
        const reader = new FileReader();
        // 读取纯文本文件,且编码格式为 utf-8
        reader.readAsText(file, 'UTF-8')

        //转换this指针
        let this_ = this;

        // 读取文件,会触发 onload 异步事件,可使用回调函数 来获取最终的值.
        reader.onload = function () {
          this_.myLog = this.result;
        }
      }
    },

    //向控制台创建一行对话（有时间、角色、内容）
    createDialog(identity,message,level){
      // 创建一行消息的容器
      let divElement = document.createElement("div");

      // 创建时间提示
      let pElement_time = document.createElement("p");
      let nowDate = new Date();
      let hours = nowDate.getHours();
      let minutes = nowDate.getMinutes();
      let seconds = nowDate.getSeconds();
      let timeText= document.createTextNode(hours+":"+minutes+":"+seconds)
      pElement_time.appendChild(timeText)
      pElement_time.setAttribute("class","time")

      // 创建身份提示
      let pElement_identity = document.createElement("p");
      let identityText = document.createTextNode(identity+">");
      pElement_identity.appendChild(identityText)
      pElement_identity.setAttribute('class','identity')

      // 创建消息提示
      let pElement_message = document.createElement("p");
      let messageText = document.createTextNode(message);
      pElement_message.appendChild(messageText)
      pElement_message.setAttribute('class','info'+level)

      divElement.appendChild(pElement_time)
      divElement.appendChild(pElement_identity)
      divElement.appendChild(pElement_message)
      divElement.setAttribute("class","line");

      let fatherElement = document.getElementById("father")
      fatherElement.insertBefore(divElement,fatherElement.lastChild)//将文本节点添加在最后一个子节点前
    },

    //向控制台创建一行文本（没有时间、角色）
    createText(message,level){
      // 创建一行消息的容器
      let divElement = document.createElement("div");

      // 创建消息提示
      let pElement_message = document.createElement("p");
      let messageText = document.createTextNode(message);
      pElement_message.appendChild(messageText)
      pElement_message.setAttribute('class','info'+level)

      divElement.appendChild(pElement_message)

      divElement.setAttribute("class","line");

      let fatherElement = document.getElementById("father")
      fatherElement.insertBefore(divElement,fatherElement.lastChild)
    },
  },
  mounted() {
    this.createText('HuHao\'s CLI  version:2.0','Normal')
    //创建hello banner
    this.createText(`          __                     __    __`,'Success')
    this.createText(`        /   /_      ___     /  /   /  /    ___`,'Success')
    this.createText(`      /   __  \\  /  _  \\  /  /   /  /   / __  \\`,'Success')
    this.createText(`    /  /  /  / /    __/ /  /   /  /  /  /_/  /`,'Success')
    this.createText(`  /_/ /_/   \\___/ /_/   /_/   \\____/`,'Success')

    this.createDialog('system','Type \'/help\' + ENTER -- for available commands.','Normal')
    //获得焦点
    document.getElementById('editP').focus();

    setInterval(()=>{
      this.nowTime = new Date().getHours()+":"+new Date().getMinutes()+":"+new Date().getSeconds()
    },1000)

  }
}
</script>

<style>
#admin-outer{
  display: flex;
  flex-direction: column;
}
.admin-inner1{
  background: #00c6ff;
}
.admin-inner2{
  background: #00c6ff;
}
.card{
  width: calc(100vw - 40px);
  height: calc(100vh - 40px);
  border-top-right-radius: 10px;
  border-top-left-radius: 10px;
  margin: 20px;
  padding: 0;
  background: #30353A;
}
.card-top{
  width: calc(100% - 5px);
  height: 10%;
  background: #E0E8F0;
  border-top-right-radius: 10px;
  border-top-left-radius: 10px;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  padding-left: 5px;
}
.card-top .button1{
  margin: 7px;
  width: 20px;
  height: 20px;
  border-radius: 10px;
}
.card-top .button2{
  margin: 7px 7px 7px 1350px;
  width: 20px;
  height: 20px;
  border-radius: 10px;
  background: #0072ff;
  display: flex;
  justify-content: center;
  align-items: center;
}
.card-top .button2:hover{
  transform: scale(1.5);
  transition: 0.5s ;
}
.card-top .button1.green{
  background: #3BB662;
}
.card-top .button1.yellow{
  background: #E5C30F;
}
.card-top .button1.red{
  background: #E75448;
}
.card-bottom{
  width: calc(100% - 5px);
  height: 90%;
  display: flex;
  flex-direction: column;
  flex-wrap: nowrap;
  overflow-y: auto;
}
.line{
  display: flex;
  flex-direction: row;
  width: calc(100% - 10px);
  height: 20px;
  margin:2px 5px;
  word-wrap: break-word;
}
.logWindow{
  width: calc(100% - 10px);
  height: calc(100% - 10px);
  margin: 5px;
  word-wrap: break-word;
  white-space: pre-wrap;/*保留空白符*/
  overflow-y: auto;
  color: white;
  font-family: 'Comic Sans MS', cursive, sans-serif;
}
.line p{
  margin: 0 0 0 0;
  color: white;
  font-family: 'Comic Sans MS', cursive, sans-serif;
  outline: none;
  word-wrap: break-word;
}
.line .time{
  max-width: 5%;
  min-width: 5%;
}
.line .identity{
  margin-right: 5px;
  margin-left: 5px;
  max-width: 5%;
  min-width: 5%;
}
/*正常的消息样式*/
.line .infoNormal{
  min-width: 90%;
  max-width: 100%;
  white-space: pre-wrap;/*保留空白符*/
}
/*成功的消息样式*/
.line .infoSuccess{
  min-width: 90%;
  max-width: 100%;
  white-space: pre-wrap;/*保留空白符*/
  color: #3BB662;
}
/*警告的消息样式*/
.line .infoWarning{
  min-width: 90%;
  max-width: 100%;
  white-space: pre-wrap;/*保留空白符*/
  color: #E5C30F;
}
/*失败的消息样式*/
.line .infoFail{
  min-width: 90%;
  max-width: 100%;
  white-space: pre-wrap;/*保留空白符*/
  color: #E75448;
}
::-webkit-scrollbar{
  width: 0;
}
</style>
