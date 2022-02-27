<template>
  <div>
    <span>{{ticketInfo}}</span>
  </div>
</template>

<script>
export default {
  name: "ticket",
  props:['Info'],
  data(){
    return{
      ticketInfo: this.Info.trainID
    }
  },
  mounted() {
    console.log(typeof this.ticketInfo)
  }
}
</script>

<style scoped>

</style>
