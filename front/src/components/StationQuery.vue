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
          prop="sid"
          label="站次"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="sname"
          label="站名"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="arrive_date"
          label="到站时间"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="price"
          label="价格"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="mileage"
          label="到首发站的里程"
          align="center"
        >
        </el-table-column>
<!--        <el-table-column-->
<!--          prop="state"-->
<!--          label="状态"-->
<!--        >-->
<!--          <template slot-scope="scope">-->
<!--            <span style="margin-left: 10px">{{ scope.row.state === 0?'正常':'停运'}}</span>-->
<!--          </template>-->
<!--        </el-table-column>-->
        <el-table-column label="操作">
          <template slot-scope="scope" align="center">
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
        <el-form-item label="列车站次">
          <el-input v-model="verifyForm.sid"></el-input>
        </el-form-item>
        <el-form-item label="站名">
          <el-input v-model="verifyForm.sname"></el-input>
        </el-form-item>
        <el-form-item label="到站时间">
          <el-date-picker
            v-model="verifyForm.arrive_date"
            type="datetime"
            placeholder="选择日期时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="价格">
          <el-input v-model="verifyForm.price"></el-input>
        </el-form-item>
        <el-form-item label="里程">
          <el-input v-model="verifyForm.mileage"></el-input>
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
  name: "StationQuery",
  data(){
    return{
      form:{
        tid:''
      },
      tableDate:[],
      dialogVisible:false,
      verifyForm:{
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
  methods: {
    onSubmit() {
      this.tableDate = []
      basePost.post('/station/query','tid='+this.form.tid+'&sid='+' ')
        .then(successResponse=>{
          if(successResponse.status === 200 && successResponse.data.type===0){
            this.$message.success(this.form.tid+'次列车经停站查询成功')
            let dataArray = JSON.parse(successResponse.data.description)
            for(let i = 0;i<dataArray.length;i++){
              this.tableDate.push(dataArray)
            }
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
      basePost.post('/station/modify',{
        tid:this.verifyForm.tid,
        sid:this.verifyForm.sid,
        sname:this.verifyForm.sname,
        arrive_date:this.verifyForm.arrive_date,
        price:this.verifyForm.price,
        mileage:this.verifyForm.mileage,
      })
        .then(successResponse=>{
          if(successResponse.status === 200 && successResponse.data.type===0){
            this.$message.success('修改成功')
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
      basePost.post('/station/delete','tid='+row.tid+'&sid='+row.sid)
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
    basePost.post('/station/queryAll')
      .then(successResponse=>{
        if(successResponse.status === 200 && successResponse.data.type === 0 && successResponse.data.description !== '搜索列车为空'){
          this.$message.success('默认查询成功')
          let array = JSON.parse(successResponse.data.description)
          for(let i=0;i<array.length;i++){
            let data = array[i]
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
