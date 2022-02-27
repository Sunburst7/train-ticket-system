<template>
    <div class="outside">
      <div class="table">
        <el-table
          :data="tableDate"
          height="640"
        >
          <el-table-column
            prop="usercard"
            label="身份证"
            align="center"
          >
          </el-table-column>
          <el-table-column
            prop="uid"
            label="昵称"
            align="center"
          >
          </el-table-column>
          <el-table-column
            prop="uname"
            label="真实姓名"
            align="center"
          >
          </el-table-column>
          <el-table-column
            prop="verified"
            label="身份认证"
            align="center"
          >
            <template slot-scope="scope">
              <span style="margin-left: 10px">{{ scope.row.verified === 0?'未认证':'已认证'}}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="身份认证"
            align="center"
          >
            <template slot-scope="scope">
              <el-image :src="srcList[scope.$index]" style="width: 148px;height: 148px" :preview-src-list="srcList" fit="contain">
                <div slot="error" class="image-slot" style="display: flex;justify-content: center;align-items: center;height: 100%">
                  <i class="el-icon-picture-outline"></i>
                </div>
              </el-image>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button
                :disabled="scope.row.verified"
                size="mini"
                @click="Edit(scope.row)">认证成功</el-button>
            </template>
          </el-table-column>
        </el-table>
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
  name: "UserVerify",
  data(){
    return{
      form:{
        cid:'',
        ctype:''
      },
      tableDate:[],
      srcList:[],
    }
  },
  methods:{
    verifyLabel(info){
      return info === 0?'未认证':'已认证'
    },

    onSubmit() {
      this.tableDate = []
     basePost.post('/carriage/query','cid='+this.form.cid+'&ctype='+this.form.ctype)
        .then(successResponse=>{
           if(successResponse.status === 200 && successResponse.data.type===0){
            let array = JSON.parse(successResponse.data.description)
            if(this.form.ctype === ''){
              this.tableDate.push(array)
            }else {
              for(let i=0;i<array.length;i++){
                this.tableDate.push(array[i])
              }
            }
          }
          else
            this.$message.warning(this.form.tid+'次列车不存在')
        })
        .catch(failResponse=>{
          this.$message.error('系统错误')
        })
    },

    Edit(info){
      basePost.post('/user/modify',{
        usercard:info.usercard,
        uid:info.uid,
        uname:info.uname,
        password:info.password,
        email:info.email,
        phone:info.phone,
        birth:info.birth,
        identity:info.identity,
        verified:1
      })
        .then(successResponse=>{
          if(successResponse.status === 200 && successResponse.data.type===0)
            this.$message.success('验证成功')
          else
            this.$message.warning('验证失败')
        })
        .catch(failResponse=>{
          this.$message.error('系统错误')
        })

    },

  },
  created() {

  },
  mounted() {
    basePost.post('/user/queryAll')
      .then(successResponse=>{
        if(successResponse.status === 200 && successResponse.data.type === 0 && successResponse.data.description !== '搜索用户为空'){
          let array = JSON.parse(successResponse.data.description)
          for(let i=0;i<array.length;i++){
            this.tableDate.push(array[i])
            this.srcList.push('/static/user/'+array[i].usercard+'.jpg')
          }
          this.$message.success('查询成功')
        }else
          this.$message.warning('查询失败')
      })
      .catch(failResponse=>{
        this.$message.error('系统错误')
      })
  }
}
</script>

<style scoped>
.outside{
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 650px;
}
.table{
  width: 100%;
  height: 650px;
}
</style>
