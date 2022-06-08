import Vue from 'vue'
import VueRouter from 'vue-router'
import IndexPage from "@/components/IndexPage";
import LoginPage from "@/components/LoginPage";
import RegistrationPage from "@/components/RegistrationPage";

import ChatPostBoxPage from "@/components/chat/PostBoxPage";
import ChatFormPage from '@/components/chat/ChatFormPage'
import MyChatPage from '@/components/chat/MyChatPage'

import MyReplyChatPage from '@/components/replyChat/MyReplyChatPage'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'IndexPage',
    component: IndexPage
  },
  // USER
  {
    path: '/loginPage',
    name: 'LoginPage',
    component: LoginPage,
    props: true
  },
  {
    path: '/registrationPage',
    name: 'RegistrationPage',
    component: RegistrationPage
  },

  // CHAT
  {
    path: '/chat',
    name: 'ChatPostBoxPage',
    component: ChatPostBoxPage
  },
  {
    path: '/chat-form',
    name: 'ChatFormPage',
    component: ChatFormPage
  },
  {
    path: '/my-chat',
    name: 'MyChatPage',
    component: MyChatPage
  },

  //REPLYCHAT
  {
    path: '/my-reply',
    name: 'MyReplyChatPage',
    component: MyReplyChatPage
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
