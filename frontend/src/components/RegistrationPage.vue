<template>
    <div class="jumbotron">
      <h1>RandomChat</h1>
      <p class="lead">회원가입</p>
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
        <button @click="registration" type="button" class="w-25 btn btn-primary btn-lg">회원가입</button>
      </div>
    </div>
</template>

<script>
import axios from "axios";

export default {
  name: 'RegistrationPage',
  data() {
    return {
      userEmail: '',
      password: ''
    }
  },
  methods: {
    registration() {
      axios.post('/login/registration', {
        userEmail: this.userEmail,
        password: this.password
      })
          .then(() => {
            alert('회원가입 성공했다');
            this.$router.push({
              name: 'LoginPage',
              params: {
                registEmail: this.userEmail
              },
            })
          })
          .catch(error => {
            alert(error.response.data.message);
            console.log(error.response.data);
          })
    },
  }
}
</script>