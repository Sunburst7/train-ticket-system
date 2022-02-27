<template>
  <div id="outer">
    <div id="header">
      <div id="icon">
        <img src="../assets/train.svg">
        <div id="icon-textSpace">
          <span class="icon-text">中国铁路——管理员端</span>
          <span class="icon-text">China Railway</span>
        </div>
      </div>
      <div style="display: flex;flex-direction: row;justify-content: flex-end;align-items: center;width: 75%;padding-right: 20px">
        <el-button type="primary" size="medium">退出</el-button>
      </div>
    </div>
    <div id="main">
      <el-row>
        <el-col :span="4">
          <el-menu
            default-active="2"
            class="el-menu-vertical-demo"
            :unique-opened="true"
            style="min-height: 649px"
            >
            <el-menu-item index="1" @click="jump('1-1')">
              <i class="el-icon-user"></i>
              <span slot="title">用户审核</span>
            </el-menu-item>

            <el-submenu index="2">
              <template slot="title">
                <i class="el-icon-menu"></i>
                <span>车次管理</span>
              </template>
              <el-menu-item index="2-1" @click="jump('2-1')"><i class="el-icon-search"></i><span slot="title">查询车次</span></el-menu-item>
              <el-menu-item index="2-2" @click="jump('2-2')"><i class="el-icon-plus"></i><span slot="title">添加车次</span></el-menu-item>
            </el-submenu>

            <el-submenu index="3">
              <template slot="title">
                <i class="el-icon-menu"></i>
                <span>停靠站管理</span>
              </template>
              <el-menu-item index="3-1" @click="jump('3-1')"><i class="el-icon-search"></i><span slot="title">查询停靠站</span></el-menu-item>
              <el-menu-item index="3-2" @click="jump('3-2')"><i class="el-icon-plus"></i><span slot="title">添加停靠站</span></el-menu-item>
            </el-submenu>

            <el-submenu index="4">
              <template slot="title">
                <i class="el-icon-menu"></i>
                <span>车厢管理</span>
              </template>
              <el-menu-item index="4-1" @click="jump('4-1')"><i class="el-icon-search"></i><span slot="title">查询车厢</span></el-menu-item>
              <el-menu-item index="4-2" @click="jump('4-2')"><i class="el-icon-plus"></i><span slot="title">添加车厢</span></el-menu-item>
              <el-menu-item index="4-3" @click="jump('4-3')"><i class="el-icon-plus"></i><span slot="title">组装车厢</span></el-menu-item>
            </el-submenu>


            <el-submenu index="5">
              <template slot="title">
                <i class="el-icon-menu"></i>
                <span>车票管理</span>
              </template>
              <el-menu-item index="5-1" @click="jump('5-1')"><i class="el-icon-search"></i><span slot="title">查询车票</span></el-menu-item>
            </el-submenu>
          </el-menu>
        </el-col>

        <el-col :span="20">
          <router-view></router-view>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: "Info",
  data(){
    return{


    }
  },
  methods: {
    jump(index){
      if(index === '1-1')
        this.$router.push({name : 'UserVerify'})
      if(index === '2-1')
        this.$router.push({name : 'TrainQuery'})
      if(index === '2-2')
        this.$router.push({name : 'TrainAdd'})
      if(index === '2-3')
        this.$router.push({name : 'TrainModify'})
      if(index === '3-1')
        this.$router.push({name : 'StationQuery'})
      if(index === '3-2')
        this.$router.push({name : 'StationAdd'})
      if(index === '3-3')
        this.$router.push({name : 'StationModify'})
      if(index === '4-1')
        this.$router.push({name : 'CarriageQuery'})
      if(index === '4-2')
        this.$router.push({name : 'CarriageAdd'})
      if(index === '4-3')
        this.$router.push({name : 'CarriageAttach'})
      if(index === '5-1')
        this.$router.push({name : 'TicketQuery'})

    }
  }
}
</script>

<style scoped>
#outer{
  width: 100vw;
  height: 100vh;
  display: flex;
  flex-direction: column;
}
#header{
  width: 100%;
  height: 10%;
  display: flex;
  flex-direction: row;
  border-bottom: 1px lightgray solid;
}
#icon{
  margin: 10px;
  width: 25%;
  display: flex;
  align-items: center;
  flex-direction: row;
  justify-content: flex-start;
}
#icon-textSpace{
  display: flex;
  flex-direction: column;
  margin-left: 5px;

}
.icon-text{
  font-family: "Comic Sans MS", cursive, sans-serif;
  font-weight: bold;
  color: deepskyblue;
}
#main{
  width: 100%;
  height: 650px
}
</style>
