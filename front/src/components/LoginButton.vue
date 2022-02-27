<template>
  <button id="button-outer" @click="click">
    <img style="margin-right: 20px" src="../assets/user-small.svg">
    <span id="button-text">{{content}}</span>
  </button>
</template>

<script>
export default {
  name: "LoginButton",
  props:['buttonUid','notLogin'],
  emits:['openGrandfatherDrawer'],
  data (){
    return{


    }
  },
  computed:{
    content:function (){
      return this.notLogin ? '未登录' : '进入售票大厅'
    },
  },
  methods:{
    click(){
      if(!this.notLogin){
        this.$router.push({
          name:'Main',
          path:'/main',
        })
      }
      else
        this.$emit('openGrandfatherDrawer')
    }
  },
}
</script>

<style scoped>
#button-outer{
  display: flex;
  flex-direction: row;
  justify-content: center;
  border-radius:16px;
  width: 50%;
  height: 100%;
  background: #00c6ff;  /* fallback for old browsers */
  background: -webkit-linear-gradient(to right, #0072ff, #00c6ff);  /* Chrome 10-25, Safari 5.1-6 */
  background: linear-gradient(to right, #0072ff, #00c6ff); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  border: 0;
  align-items: center;
  box-shadow: 0 16px 20px -10px deepskyblue;
}
#button-text{
  color: white;
  font-size: 16px;
  font-family: "Comic Sans MS", cursive, sans-serif;
}
#button-outer:hover{
  transform: scale(1.2);
  transition: 0.5s ;
}
</style>
