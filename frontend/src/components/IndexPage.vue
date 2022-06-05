<template>
    <div class="jumbotron">
      <h1>RandomChat</h1>
      <template v-if="!isLogined">
        <p class="lead">회원 기능</p>
        <p>
          <a class="btn btn-lg btn-outline-primary" @click="$router.push({name: 'RegistrationPage'})">회원가입</a>
          <a class="btn btn-lg btn-primary" @click="$router.push({name: 'LoginPage'})">로그인</a>
        </p>
      </template>
      <template v-else>
        <p class="lead">{{user_email}}님 반갑습니다!</p>
        <p>
          <a class="btn btn-lg btn-outline-success" href="">편지함 보기</a>
          <a class="btn btn-lg btn-success" href="">편지 보내기</a>
          <button class="btn btn-lg btn-outline-dark" @click="logout">로그 아웃</button>
        </p>
      </template>
    </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'IndexPage',
  created() {
    this.getUser();
  },
  data() {
    return {
      user_email: '',
      isLogined: false
    }
  },
  methods: {
    getUser() {
      axios.get('/')
      .then(response => {
        if(response.data) {
          this.isLogined = true;
          this.user_email = response.data.userEmail;
        }
        else { 
          this.isLogined = false;
          console.log('값이 없음'); 
        }
      })
      .catch(() => {
        console.log('유효하지 않은 로그인 상태값')
      })
    },
    logout() {
      this.$cookies.remove('SessionId');
      this.$router.go();
    }
  }
}
</script>