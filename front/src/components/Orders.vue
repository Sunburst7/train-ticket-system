<template xmlns="http://www.w3.org/1999/html">
  <div id="orders-Outer">
    <el-card class="box-card">
      <el-descriptions class="margin-top" title="用户信息" :column="3" border>
        <template slot="extra">
          <el-button type="primary" size="small" icon="el-icon-setting" @click="modifyOrSave">{{buttonLabel}}</el-button>
        </template>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user"></i>
            身份证
          </template>
          {{mainUser.usercard}}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user"></i>
            昵称
          </template>
          <el-input v-model="mainUser.uid"  :readonly="writeDisabled"></el-input>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-view"></i>
            密码
          </template>
          <el-input v-model="mainUser.password" show-password :readonly="writeDisabled"></el-input>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user"></i>
            姓名
          </template>
          {{mainUser.uname}}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            手机号
          </template>
          <el-input v-model="mainUser.phone" :readonly="writeDisabled"></el-input>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-time"></i>
            出生日期
          </template>
          <el-date-picker
            v-model="mainUser.birth"
            type="date"
            :readonly="writeDisabled"
            placeholder="选择日期">
          </el-date-picker>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-message"></i>
            邮箱
          </template>
          <el-input v-model="mainUser.email" :readonly="writeDisabled"></el-input>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-message"></i>
            身份
          </template>
          <el-input v-model="identityLabel" :readonly="writeDisabled"></el-input>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card class="box-card">
      <div class="verify">
        <div class="verifyBox leftleft">
          <strong>身份认证</strong>
        </div>
        <div class="verifyBox left">
          <el-image :src="pictureSrc" style="width: 520px;height: 148px" :preview-src-list="srcList" fit="contain">
            <div slot="error" class="image-slot" style="display: flex;justify-content: center;align-items: center;height: 100%">
              <i class="el-icon-picture-outline"></i>
            </div>
          </el-image>
        </div>
        <div class="verifyBox middle">
          <el-upload
            :disabled="uploadDisabled"
            name="avatar"
            action="http://localhost:8090/user/avatar"
            :data="{usercard:mainUser.usercard}"
            :on-success="success"
            list-type="picture-card"
            ref="upload"
            :limit="1"
            :before-upload="beforeAvatarUpload"
            :auto-upload="false">
            <i slot="default" class="el-icon-plus"></i>
            <div slot="file" slot-scope="{file}">
              <img
                class="el-upload-list__item-thumbnail"
                :src="file.url" alt=""
              >
              <span class="el-upload-list__item-actions">
                <span
                  class="el-upload-list__item-preview"
                  @click="handlePictureCardPreview(file)"
                >
                  <i class="el-icon-zoom-in"></i>
                </span>
                <span
                  v-if="!disabled"
                  class="el-upload-list__item-delete"
                  @click="handleRemove(file)"
                >
                  <i class="el-icon-delete"></i>
                </span>
              </span>
            </div>
          </el-upload>
        </div>
        <div class="verifyBox right">
          <el-button :disabled="uploadDisabled" type="success" @click="submitUpload">上传</el-button>
        </div>
      </div>

      <el-dialog :visible.sync="dialogVisible">
        <img width="100%" :src="dialogImageUrl" alt="">
      </el-dialog>
    </el-card>

    <el-card class="box-card1">
      <div class="ticketContainer">
        <div v-for="(info,index) in tableDate" :key="index" class="ticket-card">
          <div class="ticketInfo picture">
            <img src="../assets/火车票.svg">
          </div>

          <div class="pictureRight">
            <div class="ticketInfo start"><strong>{{info.startStation}}站</strong></div>
            <div style="display: flex;flex-direction: column;width: 40%;height: 30%">
              <div class="ticketInfo trainID">{{info.trainID}}</div>
              <div class="ticketInfo"><img src="../assets/rightArrow.svg"></div>
            </div>
            <div class="ticketInfo end"><strong>{{info.endStation}}站</strong></div>

            <div class="ticketInfo start-date">{{info.startDate}}</div>
            <div class="ticketInfo end-date">{{info.endDate}}</div>
          </div>


          <div class="ticketInfo price">{{info.price}}元</div>
          <div class="ticketInfo seatInfo">{{info.seatInfo}}</div>

          <div class="ticketInfo button">
            <el-button type="primary" @click="open" size="small"  :disabled="Boolean(info.paid)">支付</el-button>
            <el-button type="primary" @click="refund(info)" size="small" >退票</el-button>
          </div>
          <div class="ticketInfo info">
            <span>买票请到正规渠道购买</span>
            <span>中国铁路祝您旅途愉快</span>
          </div>
          <el-dialog
            title="提示"
            :visible.sync="dialogVisible1"
            width="30%"
          >
            <div style="display: flex;flex-direction: column;align-items: center">
              <h2>请扫码支付{{info.price}}元</h2>
              <img src="../assets/QRCode.png">
            </div>
            <span slot="footer" class="dialog-footer">
              <el-button type="primary" @click="payfor(info)">支 付</el-button>
            </span>
          </el-dialog>
        </div>
      </div>

    </el-card >
  </div>
</template>

<script>
import axios from "axios";
import Ticket from "./ticket";

let basePost = axios.create({
  method: 'post',
  headers:{
    "Access-Control-Allow-Origin":"*",
  },
  baseURL:'http://localhost:8090',
})
export default {
  name: "Orders",
  components: {Ticket},
  data(){
    return{
      mainUser:JSON.parse(sessionStorage.getItem('mainUser')),
      writeDisabled:true,
      tableDate:[],
      dialogVisible1:false,

      imgList:[],
      dialogImageUrl: '',
      dialogVisible: false,
      disabled: false,
      uploadDisabled:false,
      pictureSrc:'',
      srcList:[]
    }
  },

  methods:{
    //打开支付界面
    open(){
      this.dialogVisible1 = true
    },

    //对用户信息进行修改或保存
    modifyOrSave(){
      //进行修改
      if(this.writeDisabled === true){
        this.writeDisabled = false
      }else {
        //进行保存
        basePost.post('/user/modify',{
          usercard:this.mainUser.usercard,
          uid:this.mainUser.uid,
          uname:this.mainUser.uname,
          password: this.mainUser.password,
          email:this.mainUser.email,
          phone:this.mainUser.phone,
          birth:this.mainUser.birth,
          identity:this.mainUser.identity,
          verified:this.mainUser.verified
        }).then(successResponse =>{
          if(successResponse.status === 200 && successResponse.data.type === 0){
            this.$message.success('修改成功')
            sessionStorage.removeItem('mainUser')
            sessionStorage.setItem('mainUser',JSON.stringify(this.mainUser))
          }else
            this.$message.warning('修改失败')
        }).catch(failResponse =>{
          this.$message.error('系统错误')
        })
        this.writeDisabled = true
      }
    },

    refresh(){
      this.tableDate = []
      basePost.post('/user/tickets','usercard='+JSON.parse(sessionStorage.getItem('mainUser')).usercard)
        .then(successResponse=>{
          if(successResponse.status === 200 && successResponse.data.type === 0){
            let array = JSON.parse(successResponse.data.description)
            for(let i = 0;i<array.length;i++){
              this.tableDate.push(array[i])
            }
          }
        })
    },
    //支付
    payfor(info) {
      let infoArray = []//格式为 1车,1排,1座
      infoArray.push(info.seatInfo.substring(0,info.seatInfo.indexOf('车')+1))
      infoArray.push(info.seatInfo.substring(info.seatInfo.indexOf('车')+1),info.seatInfo.indexOf('排')+1)
      infoArray.push(info.seatInfo.substring(info.seatInfo.indexOf('排')+1),info.seatInfo.length)
      let seatSequence = (parseInt(infoArray[1]) - 1) * 5 + parseInt(infoArray[2])
      basePost.post('/ticket/add', 'usercard=' + this.mainUser.usercard + '&tid=' + info.trainID + '&startSname=' + info.startStation + '&arriveSname=' + info.endStation +
          '&carriageSequence=' + parseInt(infoArray[0]) + '&seatSequence=' + seatSequence + '&price=' + info.price + '&paid=1')
      .then(successResponse=>{
        console.log(successResponse.data)
        if(successResponse.status === 200 && successResponse.data.type === 0){
          this.$message.success('支付成功')
          setInterval(()=>{},2000)
          location.reload()
          this.$router.push({name:'Orders'})
        }
        else
          this.$message.warning('支付失败')
      })
      .catch(failresponse =>{
        this.$message.error('系统错误')
      })
      this.dialogVisible1 = false//关闭窗口

    },

    //退票
    refund(info){
      //未出发可退票
      if(new Date(info.startDate)>Date.now()){
        basePost.post('/ticket/delete','usercard='+this.mainUser.usercard+'&tid='+info.trainID)
          .then(successResponse=>{
            if(successResponse.status === 200 && successResponse.data.type === 0){
              this.$message.success('退票成功')
              this.refresh()
            }else
              this.$message.warning('退票失败')
          })
          .catch(failResponse=>{
            this.$message.error('系统错误')
          })
      }else
        this.$message.warning('已出发无法退票')

    },

    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error('上传图片只能是 JPG格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    },
    handleRemove(file) {
      console.log(file);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    //上传图片
    submitUpload() {
      this.$refs.upload.submit();
    },
    //图片上传成功时调用
    success(response, file, fileList){
      this.pictureSrc = '/static/user/'+response
      this.srcList.push(this.pictureSrc)
    }


  },

  computed:{
    buttonLabel:function (){
      return this.writeDisabled === true?'修改':'保存'
    },
    identityLabel:function (){
      return this.mainUser.identity===1?'未成年人':'成年人'
    },
  },

  created() {
    this.tableDate = []
    basePost.post('/user/tickets','usercard='+JSON.parse(sessionStorage.getItem('mainUser')).usercard)
    .then(successResponse=>{
      if(successResponse.status === 200 && successResponse.data.type === 0){
        let array = JSON.parse(successResponse.data.description)
        for(let i = 0;i<array.length;i++){
          this.tableDate.push(array[i])
        }
      }
    })

    if(this.mainUser.verified !== 0){
      this.pictureSrc = '/static/user/'+this.mainUser.usercard+'.jpg'
      this.uploadDisabled = true
      this.srcList.push(this.pictureSrc)
    }
    console.log(this.pictureSrc)
  },
  mounted() {
    if(this.mainUser.verified !== 0){
      this.pictureSrc = '/static/user/'+this.mainUser.usercard+'.jpg'
      this.uploadDisabled = true
      this.srcList.push(this.pictureSrc)
    }
    console.log(this.pictureSrc)

  }

}
</script>

<style scoped>
#orders-Outer{
  background: white;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  padding: 20px 20px;
  height: calc(100% - 40px);
}
.box-card {
  width: calc(100% - 40px);
  margin: 10px 0;
}
.box-card1 {
  width: calc(100% - 40px);
  height: 755px;
  margin: 10px 0;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: flex-start;
  align-items: flex-start;
}
.verify{
  width: 1319px;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
}
.leftleft{
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  width: 10%;
  height: 50%;
}
.left{
  display: flex;
  justify-content: center;
  align-items: center;
  width: 30%;
  height: 100%;
}
.middle{
  display: flex;
  justify-content: center;
  align-items: center;
  width: 50%;
  height: 100%;
}
.right{
  display: flex;
  justify-content: center;
  align-items: center;
  width: 10%;
  height: 100%;
}
.ticketContainer{
  width: 100%;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: center;
  align-items: flex-start;
  overflow-y: auto;
}
.ticket-card{
  width: 600px;
  height: 250px;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
  margin: 20px 28px 20px 28px;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  flex-wrap: wrap;
  background: #2BC0E4;  /* fallback for old browsers */
  background: -webkit-linear-gradient(to right, #EAECC6, #2BC0E4);  /* Chrome 10-25, Safari 5.1-6 */
  background: linear-gradient(to right, #EAECC6, #2BC0E4); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  animation: come-out1 1s;
}
.ticketInfo{
  display: flex;
  justify-content: center;
  align-items: center;
}
.picture{
  width: 20%;
  height: 50%;
}
.pictureRight{
  width: 80%;
  height: 50%;
  display: flex;
  flex-wrap: wrap;
  flex-direction: row;
  justify-content: center;
  align-items: center;
}
.start{
  width: 30%;
  height: 60%;
}
.trainID{
  width: 100%;
  height: 50%;
  display: flex;
  justify-content: center;
  align-items: flex-end;
}
.end{
  width: 30%;
  height: 60%;
}
.start-date{
  width: 50%;
  height: 40%;
}
.end-date{
  width: 50%;
  height: 40%;
}
.price{
  padding-left: 100px;
  width: 30%;
  height: 20%;
}
.seatInfo{
  padding-left: 100px;
  width: 30%;
  height: 20%;
}
.info{
  width: 80%;
  height: 30%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.button{
  flex-direction: row;
  width: calc(20% - 5px);
  height: 30%;
  margin-left: 5px;
}
@keyframes come-out1 {
  0% {transform: scale(0)}
  100% {transform: scale(1)}
}
</style>
