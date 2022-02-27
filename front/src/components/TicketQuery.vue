<template>
  <div class="outside">
    <div class="search">
      <el-form :inline="true" :model="form" class="form-inline">
        <el-form-item label="列车车次">
          <el-input v-model="form.tid" placeholder="请输入列车车次"></el-input>
        </el-form-item>
        <el-form-item label="始发站次">
          <el-input v-model="form.start_sid" placeholder="请输入始发站次"></el-input>
        </el-form-item>
        <el-form-item label="到达站次">
          <el-input v-model="form.arrive_sid" placeholder="请输入到达站次"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="table">
      <el-table
        :data="tableDate"
      >
        <el-table-column
          prop="usercard"
          label="身份证"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="tid"
          label="列车车次"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="startSid"
          label="始发站次"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="arriveSid"
          label="到达站次"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="cid"
          label="车厢id"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="seatSequence"
          label="座位信息"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="price"
          label="价格"
          align="center"
        >
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
  name: "TicketQuery",
  data(){
    return{
      form:{
        tid:'',
        start_sid:'',
        arrive_sid:''
      },
      tableDate:[],
    }
  },
  methods: {
    onSubmit() {
      this.tableDate = []
      basePost.post('/ticket/query','tid='+this.form.tid+'&startSid='+this.form.start_sid+'&arriveSid='+this.form.arrive_sid+'&cid='+''+'&seatSequence='+'')
        .then(successResponse=>{
          if(successResponse.status === 200 && successResponse.data.type===0){
            this.$message.success('查询成功')
            let array = JSON.parse(successResponse.data.description)
              for(let i=0;i<array.length;i++){
                this.tableDate.push(array[i])
              }
          }
          else
            this.$message.warning(this.form.tid+'次列车不存在')
        })
        .catch(failResponse=>{
          this.$message.error('系统错误')
        })
    },


    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        })
        .catch(_ => {});
    },


  },
  mounted() {
    basePost.post('/ticket/queryAll')
      .then(successResponse=>{
        if(successResponse.status === 200 && successResponse.data.type === 0 && successResponse.data.description !== '搜索列车为空'){
          this.$message.success('默认查询成功')
          let array = JSON.parse(successResponse.data.description)
          for(let i=0;i<array.length;i++){
            this.tableDate.push(array[i])
          }
        }else
          this.$message.warning('暂无列车')
      })

  },
}
</script>

<style scoped>
.outside{
  display: flex;
  flex-direction: column;
  padding: 10px;
  width: 100%;
  height: 650px;
}
.search{
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  width: calc(100% - 40px);
  height: 10%;
  padding: 20px;
}
.table{
  width: 100%;
  height: 90%;
}
</style>

