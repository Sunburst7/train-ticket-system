<template>
  <div id="myHeader-outer">
    <div id="icon">
      <img src="../assets/train.svg">
      <div id="icon-textSpace">
        <span class="icon-text">中国铁路</span>
        <span class="icon-text">China Railway</span>
      </div>
    </div>
    <div id="search">
      <search-frame @openGrandfatherDrawer="$emit('openFatherDrawer')" @open-message-window="openMessage" @admin-register1="$emit('admin-register')"></search-frame>
    </div>
    <div id="login">
      <login-button @openGrandfatherDrawer="$emit('openFatherDrawer')" :buttonUid="buttonUid" :notLogin="notLogin"></login-button>
    </div>

  </div>
</template>

<script>
import SearchFrame from "./SearchFrame";
import LoginButton from "./LoginButton";
export default {
  name: "MyHeader",
  props:['buttonUid','notLogin'],
  components: {LoginButton, SearchFrame},
  data(){
    return{

    }
  },
  methods:{
    openMessage(message){
      this.$emit('open-message',message);
    }
  }
}
</script>

<style scoped>
#myHeader-outer{
  height: 100%;
  width: 100%;
  display: flex;

}
#icon{
  width: 25%;
  display: flex;
  align-items: center;
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
#search{
  width: 55%;
  padding: 8px 0;
}
#login{
  width: 20%;
  padding: 7px 0;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
