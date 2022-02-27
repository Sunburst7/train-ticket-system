<template>
  <div class="outside">
    <div class="leftAdd">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="列车车次">
          <el-select v-model="form.tid" placeholder="请选择列车车次" clearable>
            <el-option v-for="(id,index) in tids" :key="index" :label="id" :value="id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="车厢id">
          <el-select v-model="form.cid" placeholder="请选择车厢" clearable>
            <el-option v-for="(obj,index) in clabels" :key="index" :label="obj.cid+'-'+obj.label" :value="obj.cid"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="车厢次序">
          <el-input v-model="form.sequence"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">立即创建</el-button>
          <el-button>取消</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="rightSearch">
      <el-table
        :data="tableData"
        height="649"
      >
        <el-table-column
          prop="tid"
          label="列车车次"
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
          prop="sequence"
          label="车厢次序"
          align="center"
        >
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)">删除</el-button>
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
  name: "CarriageAttach",
  data(){
    return{
      tableData:[],
      tids:[],
      clabels:[],
      form:{
        cid:'',
        tid:'',
        sequence:''
      },
      modifyForm:{
        cid:'',
        tid:'',
        sequence:''
      }
    }
  },
  methods:{
    handleDelete(index,row){
      basePost.post('/train_carriage/delete','tid='+row.tid+'&cid='+row.cid)
        .then(successResponse=>{
          if(successResponse.status === 200 && successResponse.data.type===0){
            this.$message.success('删除成功')
            location.reload()
          }

          else
            this.$message.warning('删除失败')
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
    onSubmit(){
      basePost.post('/train_carriage/add',{
        cid:this.form.cid,
        tid:this.form.tid,
        sequence:this.form.sequence
      })
    .then(successResponse=>{
        if(successResponse.status === 200 && successResponse.data.type===0)
          this.$message.success('添加成功')
        else
          this.$message.warning('添加失败')
      })
        .catch(failResponse=>{
          this.$message.error('系统错误')
        })
      this.form.cid = ''
      this.form.tid = ''
      this.form.sequence = ''
      location.reload()
    }
  },
  async mounted() {
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

    let successResponse1 = await basePost.post('/carriage/query', 'cid=' + '' + '&ctype=' + '')
        if (successResponse1.status === 200 && successResponse1.data.type === 0 && successResponse1.data.description !== '搜索列车为空') {
          let array = JSON.parse(successResponse1.data.description)
          for (let i = 0; i < array.length; i++) {
            let label = ''
            if(array[i].ctype === 0)
              label =  '商务座'
            else if (array[i].ctype === 1)
              label =  '一等座'
            else if (array[i].ctype === 2)
              label =  '二等座'
            else if (array[i].ctype === 3)
              label = '卧铺'
            else if (array[i].ctype === '')
              label = ''
              this.clabels.push({
                  cid:array[i].cid,
                  ctype:array[i].ctype,
                  label:label
              })
          }
        } else
          this.$message.warning('暂无列车')

    let successResponse = await basePost.post('/train_carriage/queryAll')
    if (successResponse.status === 200 && successResponse.data.type === 0 && successResponse.data.description !== '搜索列车为空') {
      let array = JSON.parse(successResponse.data.description)
      for (let i = 0; i < array.length; i++) {
        for(let j = 0;j<this.clabels.length;j++){
          if(this.clabels[j].cid === array[i].cid)
            this.clabels.splice(j,1)
        }
      }
    } else
      this.$message.warning('暂无列车')

    basePost.post('/train_carriage/queryAll')
      .then(successResponse=>{
        if(successResponse.status === 200 && successResponse.data.type === 0 && successResponse.data.description !== '搜索列车-车厢联系集为空'){
          let array = JSON.parse(successResponse.data.description)
          for(let i=0;i<array.length;i++)
            this.tableData.push(array[i])
          this.$message.success('默认查询成功')
        }else
          this.$message.warning('默认查询失败')
      })
      .catch(failResponse=>{
        this.$message.error('系统错误')
      })
  },
}
</script>

<style scoped>
.outside{
  width: calc(100% - 20px);
  height: calc(100% - 20px);
  display: flex;
  flex-direction: row;
}
.leftAdd{
  width: 50%;
  height: 100%;
  padding: 30px;
}
.rightSearch{
  border-left: 1px lightgray solid;
  width: 50%;
  height: 100%;
}
</style>
