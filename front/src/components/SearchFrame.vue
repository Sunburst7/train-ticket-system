<template>
  <div id="search-outer">
    <div id="search-prefix"><span id="search-text">quick-search</span></div>
    <div id="drop">
      <input id="drop-input" type="text" v-model="searchValue"></input>
      <div id="dropdown-content">
        <ul>
          <li class="dropdown-content-li" v-for="(item,index) in searchResult" :key="index" @click="clickText(item)">{{item}}</li>
        </ul>
      </div>
    </div>
    <button id="search-suffix" @click="search"><img src="../assets/search.svg"></button>
  </div>
</template>

<script>
export default {
  name: "SearchFrame",
  data(){
    return{
      searchValue:'',
      searchResult:['登录','注册','注册须知','购票须知','管理员登录'],
    }
  },
  methods:{
    search(){
      if(this.searchValue === '登录' || this.searchValue === '注册')
        this.$emit('openGrandfatherDrawer')
      if(this.searchValue === '注册须知')
        this.$emit('open-message-window','<strong>注册需要提供以下信息</strong><p>1：身份证（18位）</p><p>2：昵称(不超过10位)</p><p>3：真实姓名</p><p>4：密码(可由数字字母标点组成，不超过15位)</p><p>5：邮箱</p><p>6：电话</p><p>7：出生日期</p>')
      if(this.searchValue === '购票须知')
        this.$emit('open-message-window','<strong>登录后进入购票大厅进行购票，注意：</strong><p>1：一次最多额外添加两名购票人</p><p>2：换乘最多支持两班列车之间</p>')
      if(this.searchValue === '管理员登录')
        this.$emit('admin-register1')
    },
    clickText(value){
      this.searchValue = value;
    },
  },
  watch:{
    searchValue(newValue){
      this.searchResult = [];
      if(newValue.indexOf('登')>=0 || newValue.indexOf('录')>=0 || newValue.indexOf('登录')>=0 || newValue.indexOf('login')>=0){
        this.searchResult.push('登录')
      }
      if(newValue.indexOf('注')>=0 || newValue.indexOf('册')>=0 || newValue.indexOf('注册')>=0 || newValue.indexOf('register')>=0){
        this.searchResult.push('注册')
        this.searchResult.push('注册须知')
      }
      if(newValue.indexOf('购')>=0 || newValue.indexOf('票')>=0 || newValue.indexOf('购票')>=0){
        this.searchResult.push('购票须知')
      }
      if(newValue.indexOf('换')>=0 || newValue.indexOf('乘')>=0 || newValue.indexOf('换乘')>=0) {
        this.searchResult.push('换乘须知')
      }
      if(newValue.indexOf('管')>=0 || newValue.indexOf('管理')>=0 || newValue.indexOf('管理员')>=0 || newValue.indexOf('登录')>=0 ||newValue.indexOf('登')>=0 || newValue.indexOf('录')>=0) {
        this.searchResult.push('管理员登录')
      }
    }
  }
}
</script>

<style scoped>
#search-outer{
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: row;
}
#search-prefix{
  width: 14%;
  height: 100%;
  background: #00c6ff;  /* fallback for old browsers */
  background: -webkit-linear-gradient(to right, #0072ff, #00c6ff);  /* Chrome 10-25, Safari 5.1-6 */
  background: linear-gradient(to right, #0072ff, #00c6ff); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  border-radius: 10px 0 0 10px;
  display: flex;
  justify-content: center;
  align-items: center;
}
#search-text{
  font-family: "Comic Sans MS", cursive, sans-serif;
  color: white;

}
#drop{
  width: 70%;
  height: 100%;
  display: inline-block;
}
#drop:hover #dropdown-content{
   display: block;
 }
#drop-input:focus+#dropdown-content{
  display: block;
}
#drop-input{
  width: 100%;
  height: 95%;
  margin: 1px 0 0 0;
  padding: 0 10px;
  border: none;
  outline: 1px deepskyblue solid;
}
#dropdown-content {
  display: none;
  position: absolute;
  background-color: white;
  width: 558px;
  box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2);
  padding: 0;
  border-left: 1px deepskyblue solid;
  border-right: 1px deepskyblue solid;
  border-bottom: 1px deepskyblue solid;
}
#dropdown-content ul{
  list-style-type: none;/*y移除列表前小标志。*/
  padding: 0;
  margin: 0;
}
.dropdown-content-li{
  margin: 0;
  padding: 5px 10px;
}
.dropdown-content-li:hover{
  background: lightgray;
}
#search-suffix{
  width: 8%;
  height: 100%;
  background: #00c6ff;  /* fallback for old browsers */
  background: -webkit-linear-gradient(to right, #0072ff, #00c6ff);  /* Chrome 10-25, Safari 5.1-6 */
  background: linear-gradient(to right, #0072ff, #00c6ff); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  border-radius: 0 10px 10px 0;
  border: none;

}
</style>
