<template>
  <div style="display: flex;flex-direction:row;justify-content: center;align-items: center;padding: 50px">
    <div class="outside">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="列车车次">
          <el-input v-model="form.tid"></el-input>
        </el-form-item>
        <el-form-item label="列车类型">
          <el-select v-model="form.type" placeholder="请选择列车类型">
            <el-option label="高铁" value="G"></el-option>
            <el-option label="动车" value="D"></el-option>
            <el-option label="特快" value="T"></el-option>
            <el-option label="快速" value="K"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="始发站">
          <el-input v-model="form.start_station"></el-input>
        </el-form-item>
        <el-form-item label="终点站">
          <el-input v-model="form.end_station"></el-input>
        </el-form-item>
        <el-form-item label="发时">
          <el-date-picker type="date" placeholder="选择日期" v-model="form.start_date" style="width: 100%;"></el-date-picker>
        </el-form-item>
        <el-form-item label="到时">
          <el-date-picker type="date" placeholder="选择日期" v-model="form.end_date" style="width: 100%;"></el-date-picker>
        </el-form-item>
        <el-form-item label="列车状态">
          <el-select v-model="form.state" placeholder="请选择列车类型">
            <el-option label="正常" value="0"></el-option>
            <el-option label="停运" value="1"></el-option>
          </el-select>
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
  name: "TrainAdd",
  data(){
    return{
      form:{
        tid:'',
        type: '',
        start_station: '',
        end_station: '',
        start_date:'',
        end_date:'',
        state:''
      }
    }
  },
  methods:{
    onSubmit(){
      basePost.post('/train/add',{
        tid:this.form.tid,
        type:this.form.type,
        start_station:this.form.start_station,
        end_station:this.form.end_station,
        start_date:this.form.start_date,
        end_date:this.form.end_date,
        state:this.form.state,
      })
        .then(successResponse=>{
          if(successResponse.status === 200 && successResponse.data.type===0){
            this.$message.success(this.form.tid+'次列车添加成功')
          }
          else
            this.$message.warning(this.form.tid+'次列车添加失败')
        })
        .catch(failResponse=>{
          this.$message.error('系统错误')
        })
      this.form.tid = ''
      this.form.type = ''
      this.form.start_station = ''
      this.form.end_station = ''
      this.form.start_date = ''
      this.form.end_date = ''
      this.form.state = ''
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
