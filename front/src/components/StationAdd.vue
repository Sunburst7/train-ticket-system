<template>
  <div style="display: flex;flex-direction:row;justify-content: center;align-items: center;padding: 50px">
    <div class="outside">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="列车车次">
          <el-select v-model="form.tid" placeholder="请选择列车车次" clearable>
            <el-option v-for="(id,index) in tids" :key="index" :label="id" :value="id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="列车站次">
          <el-input v-model="form.sid"></el-input>
        </el-form-item>
        <el-form-item label="站名">
          <el-input v-model="form.sname"></el-input>
        </el-form-item>
        <el-form-item label="到站时间">
          <el-date-picker
            v-model="form.arrive_date"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择日期时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="价格">
          <el-input v-model="form.price"></el-input>
        </el-form-item>
        <el-form-item label="里程">
          <el-input v-model="form.mileage"></el-input>
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
        sid:'',
        tid:'',
        sname:'',
        arrive_date:'',
        price:'',
        mileage:''
      },
      tids:[]
    }
  },
  methods:{
    onSubmit(){
      basePost.post('/station/add',{
        sid:this.form.sid,
        tid:this.form.tid,
        sname:this.form.sname,
        arrive_date:this.form.arrive_date,
        price:this.form.price,
        mileage:this.form.mileage
      })
        .then(successResponse=>{
          if(successResponse.status === 200 && successResponse.data.type===0){
            this.$message.success(this.form.tid+'次列车中转站添加成功')
            location.reload()
          }
          else
            this.$message.warning(this.form.tid+'次列车经停站添加失败')
        })
        .catch(failResponse=>{
          this.$message.error('系统错误')
        })
      this.form.sid = ''
      this.form.tid = ''
      this.form.sname = ''
      this.form.arrive_date = ''
      this.form.price = ''
      this.form.mileage = ''
    }
  },
  mounted() {
    basePost.post('/train/queryAll')
      .then(successResponse => {
        if (successResponse.status === 200 && successResponse.data.type === 0 && successResponse.data.description !== '搜索列车为空') {
          let array = JSON.parse(successResponse.data.description)
          for (let i = 0; i < array.length; i++) {
            this.tids.push(array[i].tid)
          }
        } else
          this.$message.warning('暂无列车')
      })
  }
}
</script>

<style scoped>
.outside{
  width: calc(100% - 100px);
  height: 550px;
}
</style>

