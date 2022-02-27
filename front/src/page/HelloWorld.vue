<template>
  <div id="container">
    <div id="header">
      <my-header @openFatherDrawer="openDrawer" @open-message="openMessageFrame" @admin-register="openRegisterBox" :buttonUid="buttonUid" :notLogin="notLogin"></my-header>
    </div>

    <div id="main">
      <div class="divider-div">
        <el-divider><span style="font-family: 'Comic Sans MS', cursive, sans-serif;color: #00c6ff;font-size: 30px">热门目的地</span></el-divider>
      </div>
      <div class="picture-div">
        <el-card :body-style="{ padding: '0px',width: '300px',height:'260px',color:'#00c6ff'}">
          <img  src="../assets/beijing.jpg">
          <div style="padding: 14px;">
            <span style="font-family: 'Comic Sans MS', cursive, sans-serif;font-weight: bold;font-size: 20px">北京  天坛</span>
          </div>
        </el-card>
      </div>
      <div class="picture-div">
        <el-card :body-style="{ padding: '0px',width: '300px',height:'260px',color:'#00c6ff'}">
          <img src="../assets/chengdu.jpg">
          <div style="padding: 15px;">
            <span style="font-family: 'Comic Sans MS', cursive, sans-serif;font-weight: bold;font-size: 20px">成都  杜甫草堂</span>
          </div>
        </el-card>
      </div>
      <div class="picture-div">
        <el-card :body-style="{ padding: '0px',width: '300px',height:'260px',color:'#00c6ff'}">
          <img src="../assets/shanghai.jpg">
          <div style="padding: 15px;">
            <span style="font-family: 'Comic Sans MS', cursive, sans-serif;font-weight: bold;font-size: 20px">上海  步行街</span>
          </div>
        </el-card>
      </div>
      <div class="picture-div">
        <el-card :body-style="{ padding: '0px',width: '300px',height:'260px',color:'#00c6ff'}">
          <img src="../assets/guangzhou.jpg">
          <div style="padding: 15px;">
            <span style="font-family: 'Comic Sans MS', cursive, sans-serif;font-weight: bold;font-size: 20px">广州  广州塔</span>
          </div>
        </el-card>
      </div>
      <div class="picture-div">
        <el-card :body-style="{ padding: '0px',width: '300px',height:'260px',color:'#00c6ff'}">
          <img src="../assets/hangzhou.png">
          <div style="padding: 15px;">
            <span style="font-family: 'Comic Sans MS', cursive, sans-serif;font-weight: bold;font-size: 20px">杭州  灵隐寺</span>
          </div>
        </el-card>
      </div>
      <div class="picture-div">
        <el-card :body-style="{ padding: '0px',width: '300px',height:'260px',color:'#00c6ff'}">
          <img src="../assets/nanjing.jpg">
          <div style="padding: 15px;">
            <span style="font-family: 'Comic Sans MS', cursive, sans-serif;font-weight: bold;font-size: 20px">南京  中山陵</span>
          </div>
        </el-card>
      </div>
      <div class="picture-div">
        <el-card :body-style="{ padding: '0px',width: '300px',height:'260px',color:'#00c6ff'}">
          <img src="../assets/xian.png">
          <div style="padding: 15px;">
            <span style="font-family: 'Comic Sans MS', cursive, sans-serif;font-weight: bold;font-size: 20px">西安  大雁塔</span>
          </div>
        </el-card>
      </div>
      <div class="picture-div">
        <el-card :body-style="{ padding: '0px',width: '300px',height:'260px',color:'#00c6ff'}">
          <img src="../assets/jinan.jpg">
          <div style="padding: 15px;">
            <span style="font-family: 'Comic Sans MS', cursive, sans-serif;font-weight: bold;font-size: 20px">济南  趵突泉</span>
          </div>
        </el-card>
      </div>
    </div>

    <div class="infoTab">
      <div class="window">
        <div class="title"><span>最新发布</span></div>
        <div class="content">
          <el-popover
            placement="right"
            width="400"
            trigger="click">
            <span>{{this.specificMessage[0]}}</span>
            <div class="news" slot="reference"><img src="../assets/圆点.svg" style="margin:0 5px"><span>{{this.newMessage[0]}}</span></div>
          </el-popover>
          <el-popover
            placement="right"
            width="400"
            trigger="click">
            <span>{{this.specificMessage[1]}}</span>
            <div class="news" slot="reference"><img src="../assets/圆点.svg" style="margin:0 5px"><span>{{this.newMessage[1]}}</span></div>
          </el-popover>
          <el-popover
            placement="right"
            width="400"
            trigger="click">
            <span>{{this.specificMessage[2]}}</span>
            <div class="news" slot="reference"><img src="../assets/圆点.svg" style="margin:0 5px"><span>{{this.newMessage[2]}}</span></div>
          </el-popover>
          <el-popover
            placement="right"
            width="400"
            trigger="click">
            <span>{{this.specificMessage[3]}}</span>
            <div class="news" slot="reference"><img src="../assets/圆点.svg" style="margin:0 5px"><span>{{this.newMessage[3]}}</span></div>
          </el-popover>
          <el-popover
            placement="right"
            width="400"
            trigger="click">
            <span>{{this.specificMessage[4]}}</span>
            <div class="news" slot="reference"><img src="../assets/圆点.svg" style="margin:0 5px"><span>{{this.newMessage[4]}}</span></div>
          </el-popover>
        </div>
      </div>
      <div class="window">
        <div class="title"><span>失信名单</span></div>
        <div class="content">
          <div v-for="(people,index) in dishonest" :key="index" class="dishonest"><img src="../assets/圆点.svg" style="margin:0 5px">
            <div style="width: 20%"><span>{{people.name}}</span></div>
            <div style="display: flex;justify-content: flex-end;width: 80%"><span style="margin-left: 100px">{{people.id}}</span></div>
          </div>
        </div>
      </div>
      <div class="window link">
        <div class="title"><span>友情链接</span></div>
        <div class="content link">
          <div class="news"><img src="../assets/圆点.svg" style="margin:0 5px"><a href="http://www.china-railway.com.cn/"><img width="250px" src="../assets/link05.png"></a></div>
          <div class="news"><img src="../assets/圆点.svg" style="margin:0 5px"><a href="http://www.china-ric.com/index.jhtml?"><img width="250px" src="../assets/link02.png"></a></div>
          <div class="news"><img src="../assets/圆点.svg" style="margin:0 5px"><a href="https://www.cre.cn/"><img width="250px" src="../assets/link04.png"></a></div>
        </div>
      </div>
    </div>

    <div id="footer">
      <div>
        <div><p style="color: white">版权所有©2008-2021 中国铁道科学研究院集团有限公司<br>京ICP备05020493号-4 | ICP证：京B2-20202537</p></div>
      </div>
    </div>

    <el-dialog :visible.sync="drawer" :before-close="handleClose">
      <div slot="title" style="display: flex;flex-direction: row;justify-content: flex-start;align-items: center">
        <img style="margin-right: 5px" src="../assets/登录.svg"><span>{{title}}</span>
      </div>
      <div id="menu-container">
        <div class="loginPanel" :style="loginVisible">
          <h3 style="margin-left: 20px">Login With Identity-card And Password</h3>
          <div class="labelAndInput">
            <label class="label">身份证</label>
            <div class="inputDiv"><input class="input" v-model="User.usercard" spellcheck="false"></input>{{User.usercard.length}}/18</div>
          </div>
          <div class="labelAndInput">
            <label class="label">密码</label>
            <div class="inputDiv"><input class="input" type="password" v-model="User.password" spellcheck="false"></input>{{User.password.length}}</div>
          </div>
          <div class="labelAndInput" @click="changePanel(2)">
            <h4>还未注册？点击注册</h4>
          </div>
          <div style="width: 100%;display: flex;flex-direction: row;justify-content: center;align-items: center">
            <el-button type="primary" @click="submitLogin">登录</el-button>
            <el-button @click="resetForm">清空</el-button>
          </div>
        </div>
        <div class="registerPanel" :style="registerVisible">
          <div class="labelAndInput">
            <label class="label">身份证</label>
            <div class="inputDiv"><input class="input" v-model="newUser.usercard" spellcheck="false"></input>{{newUser.usercard.length}}/18</div>
          </div>
          <div class="labelAndInput">
            <label class="label">真实姓名</label>
            <div class="inputDiv"><input class="input" v-model="newUser.uname" spellcheck="false"></input>{{newUser.uname.length}}/5</div>
          </div>
          <div class="labelAndInput">
            <label class="label">邮箱</label>
            <div class="inputDiv"><input class="input" v-model="newUser.email" spellcheck="false"></input>{{newUser.email.length}}</div>
          </div>
          <div class="labelAndInput">
            <label class="label">密码</label>
            <div class="inputDiv"><input class="input" type="password" v-model="newUser.password" spellcheck="false"></input>{{newUser.password.length}}</div>
          </div>
          <div style="width: 100%;display: flex;flex-direction: row;justify-content: center;align-items: center">
            <el-button type="primary" @click="submitRegister">注册</el-button>
            <el-button @click="resetForm">清空</el-button>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import MyHeader from "../components/MyHeader";
import $ from 'jquery'
import axios from 'axios';
let basePost = axios.create({
  method: 'post',
  headers:{
    "Access-Control-Allow-Origin":"*",
  },
  baseURL:'http://localhost:8090',
})
export default {
  name: 'HelloWorld',
  components: {MyHeader},
  data () {
    return {
      drawer:false,
      direction: 'rtl',
      notLogin:true,
      newMessage:[],
      specificMessage:[],
      buttonUid:'',
      activeName:'first',
      dishonest:[
        {name:'张三',id:'522530********2933'},
        {name:'李四',id:'454818********2933'},
        {name:'钱六',id:'796161********2973'},
        {name:'赵一',id:'181616********2933'},
        {name:'王七',id:'895952********2743'},
        {name:'张三一',id:'471115********2663'},
        {name:'李四一',id:'454811********9331'},
        {name:'钱六一',id:'662184********3111'},
        {name:'赵一一',id:'797979********000x'},
        {name:'王七一',id:'461165********2933'},
        {name:'张三',id:'522530********2933'},
        {name:'李四',id:'454818********2933'},
        {name:'钱六',id:'796161********2973'},
        {name:'赵一',id:'181616********2933'},
        {name:'王七',id:'895952********2743'},
        {name:'张三一',id:'471115********2663'},
        {name:'李四一',id:'454811********9331'},
        {name:'钱六一',id:'662184********3111'},
        {name:'赵一一',id:'797979********000x'},
        {name:'王七一',id:'461165********2933'},
      ],
      switchPanel:false,
      loginVisible:{display:'inline'},
      registerVisible:{display:'none'},
      inAnimation:{animation:'none'},
      User:{
        usercard:'',
        password:'',
      },
      newUser:{
        usercard:'',
        uid:'',
        uname:'',
        password: '',
        email:'',
        phone:'',
        birth:'',
        identity:''
      },
      idErrorInfo:'',
    }
  },
  methods:{
    //打开登录注册窗口抽屉
    openDrawer(){
      this.drawer = true
    },

    checkIDCard(idcode){
      // 加权因子
      var weight_factor = [7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2];
      // 校验码
      var check_code = ['1', '0', 'X' , '9', '8', '7', '6', '5', '4', '3', '2'];

      var code = idcode + "";
      var last = idcode[17];//最后一个

      var seventeen = code.substring(0,17);

      // ISO 7064:1983.MOD 11-2
      // 判断最后一位校验码是否正确
      var arr = seventeen.split("");
      var len = arr.length;
      var num = 0;
      for(var i = 0; i < len; i++){
        num = num + arr[i] * weight_factor[i];
      }

      // 获取余数
      var resisue = num%11;
      var last_no = check_code[resisue];

      // 格式的正则
      // 正则思路
      /*
      第一位不可能是0
      第二位到第六位可以是0-9
      第七位到第十位是年份，所以七八位为19或者20
      十一位和十二位是月份，这两位是01-12之间的数值
      十三位和十四位是日期，是从01-31之间的数值
      十五，十六，十七都是数字0-9
      十八位可能是数字0-9，也可能是X
      */
      var idcard_patter = /^[1-9][0-9]{5}([1][9][0-9]{2}|[2][0][0|1][0-9])([0][1-9]|[1][0|1|2])([0][1-9]|[1|2][0-9]|[3][0|1])[0-9]{3}([0-9]|[X])$/;

      // 判断格式是否正确
      var format = idcard_patter.test(idcode);

      // 返回验证结果，校验码和格式同时正确才算是合法的身份证号码
      return last === last_no && format ? true : false;
    },

    //切换登录注册面板
    changePanel(type){
      if(type === 1){
        this.loginVisible.display = 'inline'
        this.registerVisible.display = 'none'
        this.switchPanel = false
      }else {
        this.loginVisible.display = 'none'
        this.registerVisible.display = 'inline'
        this.switchPanel = true
      }
    },

    //关闭登录注册界面
    handleClose(done) {
      this.changePanel(1)
      done()
    },

    //清空
    resetForm(){
      this.User.usercard = ''
      this.User.password = ''
      this.newUser.usercard =''
      this.newUser.uname= ''
      this.newUser.email = ''
      this.newUser.password = ''
    },
    //登录提交
    submitLogin() {
          basePost.post('/user/login','usercard='+this.User.usercard+'&password='+this.User.password)
            .then(successResponse =>{
              if(successResponse.status === 200){
                let Message = successResponse.data//对象
                if(Message.type === 0){
                  let user = JSON.parse(Message.description)//转化成user对象
                  this.buttonUid = user.uid
                  sessionStorage.setItem('mainUser',Message.description)
                  this.notLogin = false
                  this.drawer = false
                }else {
                  this.$message({
                    type: 'warning',
                    message: '身份证或密码错误'
                  })
                }
              }
            })
            .catch(failResponse => {
              this.$message({
                type: 'warning',
                message: '系统错误'
              })
            })
    },

    //注册提交
    submitRegister() {
      if(this.newUser.usercard.length === 18 && this.checkIDCard(this.newUser.usercard)) {
        if(this.newUser.email.match(/[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/) !== null){
          if(this.newUser.uname.length <= 5 && this.newUser.uname!==''){
            if(this.newUser.password !== ''){
              basePost.post('user/register',{
                usercard:this.newUser.usercard,
                uid:this.newUser.uid,
                uname:this.newUser.uname,
                password:this.newUser.password,
                email:this.newUser.email,
                phone:this.newUser.phone,
                birth:this.newUser.birth,
                identity:this.newUser.identity,
              })
                .then(successResponse =>{
                  if(successResponse.status === 200){
                    let Message = successResponse.data//对象
                    if(Message.type === 0){
                      this.$message({
                        type: 'success',
                        message: this.newUser.usercard+' 注册成功'
                      })
                      this.changePanel(1)
                      this.User.usercard = this.newUser.usercard
                      this.User.password = this.newUser.password
                      this.newUser.usercard =''
                      this.newUser.uid = ''
                      this.newUser.uname = ''
                      this.newUser.password = ''
                      this.newUser.email = ''
                      this.newUser.phone = ''
                      this.newUser.birth = ''
                      this.newUser.identity = ''
                    }else{
                      this.$message.error({
                        message:'注册失败'
                      })
                    }
                  }
                })
            }else
              this.$message.warning('密码不能为空')
          }else
            this.$message.warning('真实姓名的长度必须小于等于5位且不为空')
        }else
          this.$message.warning('邮箱不符合格式')
      }else
        this.$message.warning('身份证格式不正确')
    },

    //打开消息窗口
    openMessageFrame(message){
      this.$notify.info({
        title: '信息',
        dangerouslyUseHTMLString: true,
        message: message,
        position: 'top-left'
      });
    },

    /**
     * 打开管理员登录窗口
     */
    openRegisterBox(){
      this.$prompt('请输入管理员密码', '管理员登录', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
      }).then(({ value }) => {
        if(value === 'admin'){//管理员密码
          this.$router.push('/admin')
        }
        this.$message({
          type: 'success',
          showClose: true,
          message: '欢迎你，管理员'
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消输入'
        });
      });
    },



  },

  computed:{
    title:function (){
      return this.switchPanel === false?'登录':'注册'
    },
  },

  watch:{
    newUser:{
      handler:function (val){
        let timeArray = val.birth.split('-')
        let myBirth = new Date(parseInt(timeArray[0]),parseInt(timeArray[1]),parseInt(timeArray[2]))
        val.identity = Date.now() - myBirth>18*31536000*1000?'0':'1'
        console.log(val.identity)
      },
      deep:true
    },
    User:{
      handler:function (val){
        if(isNaN(this.User.usercard)){
          this.idErrorInfo = '输入身份证必须为数字'
        }
      },
      deep:true
    }
  },
  mounted() {
    this.newMessage.push(window.localStorage.getItem('message1'),
      window.localStorage.getItem('message2'),
      window.localStorage.getItem('message3'),
      window.localStorage.getItem('message4'),
      window.localStorage.getItem('message5'))
    this.specificMessage.push(window.localStorage.getItem('smessage1'),
      window.localStorage.getItem('smessage2'),
      window.localStorage.getItem('smessage3'),
      window.localStorage.getItem('smessage4'),
      window.localStorage.getItem('smessage5'))
    function topNavScroll(){
      //获取当前窗口滚动条顶部所在的像素值 并取整
      const topScroll = Math.floor($(window).scrollTop());
      //定义滚动条只要大于0 背景透明度就变成1 并且增加转换时间为1s
      if(topScroll>48){
        $('#header').css('transition','0.5s');
        $('#header').css('box-shadow','rgba(50,50,93,0.25) 0 2px 5px -1px, rgba(0,0,0,0.3) 0 1px 3px -1px');
        $('#header').css('background','rgba(255,255,255,0.8)');
      }
      else{
        $('#header').css('transition','0.5s');
        $('#header').css('box-shadow','none');
        $('#header').css('background','white');
      }
    }
    $(window).on('scroll',function() {
      topNavScroll();
    });
  },
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
#container{
  display: flex;
  flex-direction: column;
}
#header{
  height: 48px;
  width: calc(100% - 64px);
  display: flex;
  position: fixed;
  padding: 8px 32px;
  background: white;
  z-index: 5;
}

#main{
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
  flex-wrap: wrap;
  padding: 30px 20px;
  margin-top: 60px;
}
.divider-div{
  width: 100%;
  padding: 20px 40px;
  z-index: 1;
}
.picture-div{
  margin: 30px 33px;
}
.picture-div:hover{
  transform:scale(1.1);
  transition: 0.5s ;
}
.infoTab{
  padding: 30px 30px;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
}
.window{
  width: calc(33% - 40px);
  height: 300px;
  margin: 0 20px;
  display: flex;
  flex-direction: column;
  border: 1px lightgray solid;
}
.title{
  display: flex;
  justify-content: center;
  align-items: center;
  height: 30px;
  padding: 10px;
  background: #0072ff;
  color: white;
}
.content{
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: flex-start;
  height: 270px;
  padding: 10px 0;
  overflow: hidden;
}
.news{
  display: flex;
  justify-content: flex-start;
  align-items: center;
  width: 100%;
  height: 50px;
}
.dishonest{
  display: flex;
  justify-content: flex-start;
  align-items: center;
  width: 100%;
  min-height: 50px !important;
  animation: roll 10s linear infinite normal;
}
#footer{
  display: flex;
  justify-content: center;
  align-items: center;
    /* fallback for old browsers */
  background: -webkit-linear-gradient(to right, #0072ff, #00c6ff);  /* Chrome 10-25, Safari 5.1-6 */
  background: linear-gradient(to right, #0072ff, #00c6ff); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  padding: 20px 30px 20px 80px;
}
#menu-container{
  height: 100%;
}
.loginPanel{
  width: 100%;
  height: 700px;
}
.registerPanel{
  width: 100%;
  height: 700px;
}
.labelAndInput{
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  margin: 20px 20px 30px 20px;
}
.label{
  margin-bottom: 5px;
  position: relative;
  top: 20px;
  animation: moveUp 1s;
  animation-fill-mode:forwards;
  height: 20px;
}
.inputDiv{
  width: 65%;
  display: flex;
  flex-direction: row;
}
.input{
  border-bottom: 1px #0072ff solid;
  border-top: none;
  border-left: none;
  border-right: none;
  outline: none;
  width: 90%;
  color: #0072ff;
  font-size: 20px;
}
@keyframes roll {
  0% {-webkit-transform: translate3d(0, 0, 0);transform: translate3d(0, 0, 0)}
  100% {-webkit-transform: translate3d(0, -270px, 0);transform: translate3d(0, -270px, 0)}
}
@keyframes moveUp {
  0% {-webkit-transform: translate3d(0, 0, 0);transform: translate3d(0, 0, 0)}
  100% {-webkit-transform: translate3d(0, -20px, 0);transform: translate3d(0, -20px, 0)}
}
</style>
