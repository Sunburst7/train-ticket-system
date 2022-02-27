<template>
  <div class="outside">
    <div class="search">
      <el-form :inline="true" :model="form" class="form-inline">
        <el-form-item label="车次">
          <el-input v-model="form.tid" placeholder="请输入车次"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="table">
      <el-table
      :data="tableDate"
      height="540"
      >
      <el-table-column
        prop="tid"
        label="车次"
        align="center"
      >
      </el-table-column>
        <el-table-column
          prop="type"
          label="类型"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="start_station"
          label="起始站"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="end_station"
          label="终点站"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="start_date"
          label="发时"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="end_date"
          label="到时"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="state"
          label="状态"
          align="center"
        >
        <template slot-scope="scope">
          <span style="margin-left: 10px">{{ scope.row.state === 0?'正常':'停运'}}</span>
        </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="handleEdit(scope.$index, scope.row)">修改</el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose">
      <el-form ref="form" :model="verifyForm" label-width="80px">
        <el-form-item label="列车车次">
          <el-input v-model="verifyForm.tid"></el-input>
        </el-form-item>
        <el-form-item label="列车类型">
          <el-select v-model="verifyForm.type" placeholder="请选择列车类型">
            <el-option label="高铁" value="G"></el-option>
            <el-option label="动车" value="D"></el-option>
            <el-option label="特快" value="T"></el-option>
            <el-option label="快速" value="K"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="始发站">
          <el-input v-model="verifyForm.start_station"></el-input>
        </el-form-item>
        <el-form-item label="终点站">
          <el-input v-model="verifyForm.end_station"></el-input>
        </el-form-item>
        <el-form-item label="发时">
          <el-date-picker type="date" placeholder="选择日期" v-model="verifyForm.start_date" style="width: 100%;"></el-date-picker>
        </el-form-item>
        <el-form-item label="到时">
          <el-date-picker type="date" placeholder="选择日期" v-model="verifyForm.end_date" style="width: 100%;"></el-date-picker>
        </el-form-item>
        <el-form-item label="列车状态">
          <el-select v-model="verifyForm.state" placeholder="请选择列车类型">
            <el-option label="正常" value="0"></el-option>
            <el-option label="停运" value="1"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="Edit">确 定</el-button>
      </span>
    </el-dialog>
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
  name: "TrainQuery",
  data(){
    return{
      form:{
        tid:''
      },
      tableDate:[],
      dialogVisible:false,
      verifyForm:{
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
  methods: {
    onSubmit() {
      this.tableDate = []
      basePost.post('/train/query','tid='+this.form.tid)
        .then(successResponse=>{
          if(successResponse.status === 200 && successResponse.data.type===0){
            this.$message.success(this.form.tid+'次列车查询成功')
            let data = JSON.parse(successResponse.data.description)
            let sDate = new Date(parseInt(data.start_date))
            let eDate = new Date(parseInt(data.end_date))
            data.start_date = sDate.getFullYear()+'-'+((sDate.getMonth()+1)<10?('0'+(sDate.getMonth()+1)):(sDate.getMonth()+1))+'-'+sDate.getDate()
            data.end_date = eDate.getFullYear()+'-'+((eDate.getMonth()+1)<10?('0'+(eDate.getMonth()+1)):(eDate.getMonth()+1))+'-'+eDate.getDate()
            this.tableDate.push(data)
          }
          else
            this.$message.warning(this.form.tid+'次列车不存在')
        })
        .catch(failResponse=>{
          this.$message.error('系统错误')
        })
    },

    handleEdit(index, row) {
      this.dialogVisible = true
      this.verifyForm = row
    },

    Edit(){
      basePost.post('/train/modify',{
        tid:this.verifyForm.tid,
        type:this.verifyForm.type,
        start_station:this.verifyForm.start_station,
        end_station:this.verifyForm.end_station,
        start_date:this.verifyForm.start_date,
        end_date:this.verifyForm.end_date,
        state:this.verifyForm.state
      })
        .then(successResponse=>{
          if(successResponse.status === 200 && successResponse.data.type===0){
            this.$message.success(this.verifyForm.tid+'次列车修改成功')
            location.reload()
          }
          else
            this.$message.warning('修改失败')
        })
        .catch(failResponse=>{
          this.$message.error('系统错误')
        })
      this.dialogVisible = false
      this.verifyForm = {}
    },

    handleDelete(index, row) {
      this.tableDate.splice(index,1)
      basePost.post('/train/delete','tid='+row.tid)
        .then(successResponse=>{
          if(successResponse.status === 200 && successResponse.data.type===0){
            this.$message.success(row.tid+'次列车删除成功')
            location.reload()
          }
          else
            this.$message.warning('修改失败')
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
    }
  },
  mounted() {
    basePost.post('/train/queryAll')
      .then(successResponse=>{
        if(successResponse.status === 200 && successResponse.data.type === 0 && successResponse.data.description !== '搜索列车为空'){
          this.$message.success('默认查询成功')
          let array = JSON.parse(successResponse.data.description)
          for(let i=0;i<array.length;i++){
            let data = array[i]
            let sDate = new Date(parseInt(data.start_date))
            let eDate = new Date(parseInt(data.end_date))
            data.start_date = sDate.getFullYear()+'-'+((sDate.getMonth()+1)<10?('0'+(sDate.getMonth()+1)):(sDate.getMonth()+1))+'-'+sDate.getDate()
            data.end_date = eDate.getFullYear()+'-'+((eDate.getMonth()+1)<10?('0'+(eDate.getMonth()+1)):(eDate.getMonth()+1))+'-'+eDate.getDate()
            this.tableDate.push(data)
          }
        }else
          this.$message.warning('暂无列车')
      })

  }
}
</script>

<style scoped>
.outside{
  display: flex;
  flex-direction: column;
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
  height: 530px;
}
</style>
