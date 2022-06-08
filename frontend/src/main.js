import Vue from 'vue'
import App from './App.vue'
import router from './router'
import axios from "axios";
import VueCookies from 'vue-cookies';
import store from './store'

import BootstrapVue from "bootstrap-vue";
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

import PortalVue from 'portal-vue'

axios.defaults.baseURL = "http://localhost:8081";
axios.defaults.withCredentials = true
axios.defaults.headers.post['Content-Type'] = 'application/json;charset=utf-8';
axios.defaults.headers.post['Access-Control-Allow-Origin'] = '*';

Vue.config.productionTip = false
Vue.use(BootstrapVue)
Vue.use(PortalVue)
Vue.use(VueCookies)
Vue.use(axios)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
