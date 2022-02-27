<template>
  <div>
    <div id="header">
      <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="jump">
        <el-menu-item :disabled="disable" index="buy"><i class="el-icon-s-ticket"></i><template #title>售票中心</template></el-menu-item>
        <el-menu-item index="orders"><i class="el-icon-s-management"></i><template #title>我的订单</template></el-menu-item>
      </el-menu>
      <div id="userList-div">
        <div style="display: flex;flex-direction: row;align-items: center">
          <span style="margin-right: 30px">乘车候选人</span>
          <el-checkbox-group v-model="buyersList" @change="storeBuyersID" :min="1" style="display: flex;flex-direction: row">
            <el-card v-for="(id,index) in usersList" :key="index" class="user-card">
              <el-checkbox  :label="id" :key="index" >{{text(id,index)}}</el-checkbox>
            </el-card>
          </el-checkbox-group>
        </div>
      </div>
      <div id="button-div">
        <el-button type="primary" @click="dialogVisible = true" icon="el-icon-plus">添加候选人</el-button>
      </div>
    </div>
    <div id="major">
      <router-view></router-view>
    </div>
    <div>
      <el-dialog
        title="输入身份证号后点击发送发送邮箱验证码"
        :visible="dialogVisible"
        width="30%"
      >
        <div>
          <div><span>身份证号</span><el-input v-model="newBuyerID" placeholder="输入身份证号"></el-input></div>
          <div><span>邮箱验证码</span><el-input v-model="emailCode" placeholder="输入邮箱验证码"></el-input></div>
        </div>
        <template #footer>
            <span class="dialog-footer">
              <el-button @click="dialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="sendEmail">发送验证码</el-button>
              <el-button type="primary" @click="verify">验 证</el-button>
            </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import axios from "axios";

let basePost = axios.create({
  method: 'post',
  headers:{
    "Access-Control-Allow-Origin":"*",
  },
  baseURL:'http://localhost:8090',
})
export default {
  name: "Main",
  data() {
    return {
      activeIndex: 'buy',
      dialogVisible:false,
      newBuyerID:'',
      emailCode:'',
      rightCode:'',
      usersList:[
        JSON.parse(sessionStorage.getItem('mainUser')).usercard
      ],
      buyersList:[],
      disable:false
    };
  },
  methods: {
    jump(key){
        if(key === 'buy')
          this.$router.push({
            name:'Buy'
          })
        else if(key === 'orders')
          this.$router.push({
            name:'Orders'
        })
    },

    //创建候车人名片文本
    text(str,index){
      if(index === 0){
        return '本人: '+str
      }else
        return '候选人: '+str
    },

    //向后端发送发邮件请求
    sendEmail(){
      if(this.newBuyerID !== ''){
        this.rightCode = ''
        basePost.post('/email/send','to='+this.newBuyerID)
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type === 0){
              this.rightCode = successResponse.data.description
            }else
              this.$message.warning('不存在该用户')
          })
          .catch(failResponse=>{
            this.$message.error('系统错误')
          })
      }else
        this.$message.warning('请先输入用户身份证')
    },

    //验证邮件验证码
    verify(){
      if(this.rightCode !== ''){
        if(this.emailCode === this.rightCode){
          this.dialogVisible = false
          if(this.usersList.length <= 3 && this.usersList.indexOf(this.newBuyerID) === -1){
            this.rightCode = ''
            this.$message({
              showClose: true,
              message: '添加购票人成功',
              type: 'success'
            });
            this.usersList.push(this.newBuyerID)
          }else {
            this.$message({
              showClose: true,
              message: '最多添加三名购票人且不能重复',
              type: 'warning'
            });
          }
          this.newBuyerID = ''
          this.emailCode = ''
        }else {
          this.$message({
            showClose: true,
            message: '验证码错误',
            type: 'warning'
          });
        }
      }else
        this.$message.warning('请先发送验证码')
    },

    //改变sessionStorage中的值
    storeBuyersID(){
      sessionStorage.setItem('buyers',this.buyersList)
    }

  },
  mounted() {
    this.buyersList.push(this.usersList[0])
    console.log(JSON.parse(sessionStorage.getItem('mainUser')))
    if(JSON.parse(sessionStorage.getItem('mainUser')).verified === 0){
      this.disable = true
      this.$router.push({
        name:'Orders',
        path:'/order',
      })
    }else{
      this.$router.push({
        name:'Buy',
        path:'/buy',
      })
    }
    sessionStorage.setItem('buyers',this.buyersList)
  }
}
</script>

<style scoped>
#header{
  display: flex;
  padding: 10px 30px;
  box-shadow: rgba(50,50,93,0.25) 0 2px 5px -1px, rgba(0,0,0,0.3) 0 1px 3px -1px;
}
#userList-div{
  margin-left: 200px;
  margin-right:30px;
  width: 835px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}
#button-div{
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;
}
.user-card{
  animation: come-out 1s;
  margin-right:10px;
  background: papayawhip;
}
#major{
  width: calc(100% - 80px);
  height: 1260px;
  margin: 0;
  padding: 50px 40px;
  background: #00c6ff;  /* fallback for old browsers */
  background: -webkit-linear-gradient(to right, #0072ff, #00c6ff);  /* Chrome 10-25, Safari 5.1-6 */
  background: linear-gradient(to right, #0072ff, #00c6ff); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
}
@keyframes come-out
{
  0%   {transform: scale(0)}
  /*25%  {transform: scale(0.25)}*/
  /*50%  {transform: scale(0.5)}*/
  /*75%  {transform: scale(0.75)}*/
  100% {transform: scale(1)}
}
</style>
