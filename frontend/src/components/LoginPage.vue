<template>
    <form class="jumbotron">
      <p class="lead">로그인</p>
      <div class="mb-3 row">
        <label for="userEmail" class="col-sm-2 col-form-label">Email</label>
        <div class="col-sm-10">
          <input v-model="userEmail" type="email" class="form-control" id="userEmail">
        </div>
      </div>
      <div class="mb-3 row">
        <label for="password" class="col-sm-2 col-form-label">Password</label>
        <div class="col-sm-10">
          <input v-model="password" type="password" class="form-control" id="password">
        </div>
      </div>
      <div class="col mt-3 text-end">
        <button @click="login" type="button" class="w-25 btn btn-primary btn-sm">로그인</button>
        <a @click="$router.push('registrationPage')"><button type="button" class="w-auto btn btn-outline-success btn-sm">회원가입</button></a>
      </div>
    </form>
</template>

<script>
import axios from 'axios';

export default {
  name: 'LoginPage',
  data() {
    return {
      userEmail: '',
      password: ''
    }
  },
  created() {
    this.userEmail = this.registEmail
  },
  props: ['registEmail'],
  methods: {
    login() {
      axios.post('/login', {
        userEmail: this.userEmail,
        password: this.password
      })
          .then(() => {
            alert('로그인 성공했다');
            this.$router.push({
              name: 'IndexPage'
            })
          })
          .catch(error => {
            alert(error.response.data.message);
          })
    }
  }
}
</script>