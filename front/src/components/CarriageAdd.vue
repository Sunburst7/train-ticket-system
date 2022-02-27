<template>
  <div style="display: flex;flex-direction:row;justify-content: center;align-items: center;padding: 50px">
    <div class="outside">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="车厢id">
          <el-input v-model="form.cid"></el-input>
        </el-form-item>
        <el-form-item label="车厢类型">
          <el-select v-model="form.ctype" placeholder="请选择车厢类型" clearable>
            <el-option label="商务座" value="0"></el-option>
            <el-option label="一等座" value="1"></el-option>
            <el-option label="二等座" value="2"></el-option>
            <el-option label="卧铺" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="座位数">
          <el-input v-model="form.seat_number"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">立即创建</el-button>
          <el-button>取消</el-button>
        </el-form-item>
      </el-form>
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
  name: "CarriageAdd",
  data(){
    return{
      form:{
        cid:'',
        ctype:'',
        seat_number:''
      }
    }
  },
  methods:{
    onSubmit(){
      basePost.post('/carriage/add',{
        cid:this.form.cid,
        ctype:this.form.ctype,
        seat_number:this.form.seat_number
      })
        .then(successResponse=>{
          if(successResponse.status === 200 && successResponse.data.type===0){
            this.$message.success('添加成功')
            location.reload()
          }

          else
            this.$message.warning('添加失败')
        })
        .catch(failResponse=>{
          this.$message.error('系统错误')
        })
      this.form.cid = ''
      this.form.ctype = ''
      this.form.seat_number = ''
    }
  }
}
</script>

<style scoped>
.outside{
  width: calc(100% - 100px);
  height: 550px;
}
</style>
