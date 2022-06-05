import Vue from 'vue'
import VueRouter from 'vue-router'
import IndexPage from "@/components/IndexPage";
import LoginPage from "@/components/LoginPage";
import RegistrationPage from "@/components/RegistrationPage";

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'IndexPage',
    component: IndexPage
  },
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
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
