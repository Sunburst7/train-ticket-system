import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '../page/HelloWorld';
import Main from "../page/Main";
import BuyTicket from "../components/BuyTicket";
import Orders from "../components/Orders";
import Admin from "../components/Admin";
import Info from "../page/Info";
import TrainAdd from "../components/TrainAdd";

import TrainQuery from "../components/TrainQuery";
import StationAdd from "../components/StationAdd";
import StationQuery from "../components/StationQuery";

import CarriageAdd from "../components/CarriageAdd";
import CarriageQuery from "../components/CarriageQuery";

import TicketQuery from "../components/TicketQuery";
import CarriageAttach from "../components/CarriageAttach";
import UserVerify from "../components/UserVerify";

Vue.use(Router)
const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}


export default new Router({
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      component: HelloWorld
    },
    {
      path:'/info',
      name:'Info',
      component: Info,
      children:[
            {
              name:'UserVerify',
              path: '/userVerify',
              component: UserVerify
            },
            {
              name:'TrainAdd',
              path:'/trainAdd',
              component:TrainAdd
            },
            {
              name:'TrainQuery',
              path:'/trainQuery',
              component:TrainQuery
            },

            {
              name:'StationAdd',
              path:'/stationAdd',
              component:StationAdd
            },
            {
              name:'StationQuery',
              path:'/stationQuery',
              component:StationQuery
            },

            {
              name:'CarriageAdd',
              path:'/carriageAdd',
              component:CarriageAdd
            },
            {
              name:'CarriageQuery',
              path:'/carriageQuery',
              component:CarriageQuery
            },
            {
              name:'CarriageAttach',
              path:'/carriageAttach',
              component:CarriageAttach
            },
            {
              name:'TicketQuery',
              path:'/ticketQuery',
              component:TicketQuery
            },
      ]
    },
    {
      path: '/main',
      name: 'Main',
      component: Main,
      props:true,
      meta: {
        requireAuth: true,  // 添加该字段，表示进入这个路由是需要登录的
      },
      children:[
        {
          name:'Buy',
          path:'/buy',
          component:BuyTicket
        },
        {
          name:'Orders',
          path:'/orders',
          component:Orders
        },
      ]
    },
    {
      path: '/admin',
      name: 'Admin',
      component: Info
    }
  ]
})

