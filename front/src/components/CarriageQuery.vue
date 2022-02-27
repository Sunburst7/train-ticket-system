<template>
  <div class="outside">
    <div class="search">
      <el-form :inline="true" :model="form" class="form-inline">
        <el-form-item label="车厢id">
          <el-input v-model="form.cid" placeholder="请输入车厢id"></el-input>
        </el-form-item>
        <el-form-item label="车厢类型">
          <el-select v-model="form.ctype" placeholder="请选择车厢类型" clearable>
            <el-option label="商务座" value="0"></el-option>
            <el-option label="一等座" value="1"></el-option>
            <el-option label="二等座" value="2"></el-option>
            <el-option label="卧铺" value="3"></el-option>
          </el-select>
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
          prop="cid"
          label="车厢id"
          align="center"
        >
        </el-table-column>
        <el-table-column
          prop="ctype"
          label="车厢类型"
          align="center"
        >
          <template slot-scope="scope">
            <span style="margin-left: 10px">{{scope.row.ctype}}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="seat_number"
          label="座位数"
          align="center"
        >
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
      title="修改"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose">
      <el-form ref="form" :model="verifyForm" label-width="80px">
        <el-form-item label="车厢id">
          <el-input v-model="verifyForm.cid"></el-input>
        </el-form-item>
        <el-form-item label="车厢类型">
          <el-select v-model="verifyForm.ctype" placeholder="请选择车厢类型" clearable>
            <el-option label="商务座" value="0"></el-option>
            <el-option label="一等座" value="1"></el-option>
            <el-option label="二等座" value="2"></el-option>
            <el-option label="卧铺" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="座位数">
          <el-input v-model="verifyForm.seat_number"></el-input>
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
  name: "CarriageQuery",
  data(){
    return{
      form:{
        cid:'',
        ctype:''
      },
      tableDate:[],
      dialogVisible:false,
      verifyForm:{
        cid:'',
        ctype:'',
        seat_number:''
      }
    }
  },
  methods: {
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

    handleEdit(index, row) {
      this.dialogVisible = true
      this.verifyForm = row
    },

    Edit(){
      basePost.post('/carriage/modify',{
        cid:this.verifyForm.cid,
        ctype:this.verifyForm.ctype,
        seat_number:this.verifyForm.seat_number
      })
        .then(successResponse=>{
          if(successResponse.status === 200 && successResponse.data.type===0)
            this.$message.success('修改成功')
          else
            this.$message.warning('修改失败')
        })
        .catch(failResponse=>{
          this.$message.error('系统错误')
        })
      this.dialogVisible = false
      this.verifyForm = {}
      location.reload()
    },

    handleDelete(index, row) {
      this.tableDate.splice(index,1)
      basePost.post('/carriage/delete','cid='+row.cid)
        .then(successResponse=>{
          if(successResponse.status === 200 && successResponse.data.type===0){
            this.$message.success(row.cid+'车厢删除成功')
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
    },

    typeLabel: function (ticketType){
      if(ticketType === '0')
        return '商务座'
      else if (ticketType === '1')
        return '一等座'
      else if (ticketType === '2')
        return '二等座'
      else if (ticketType === '3')
        return '卧铺'
      else if (ticketType === '')
        return ''
    }
  },
  mounted() {
    basePost.post('/carriage/query','cid='+''+'&ctype='+'')
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

  },
}
</script>

<style scoped>
.outside{
  display: flex;
  flex-direction: column;
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
  height: 530px;
}
</style>
