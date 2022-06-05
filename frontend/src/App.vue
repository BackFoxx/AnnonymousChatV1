<template>
  <div id="app">
    <div class="container">
      <div class="header">
        <ul class="nav nav-pills pull-right">
          <li><a @click="$router.push({name: 'IndexPage'})">ToyProject</a></li>
        </ul>
        <a @click="$router.push({name: 'IndexPage'})"><h3 class="text-muted">RandomChat</h3></a>
      </div>
      <router-view/>
    <div class="footer">
      <p>&copy; Hello Shop V2</p>
    </div>
  </div>
  </div>
</template>

<style>

@import "../src/assets/css/bootstrap.min.css";
@import "../src/assets/css/jumbotron-narrow.css";

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>
