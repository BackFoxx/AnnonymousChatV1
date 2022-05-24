<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>

    <link rel="stylesheet" type="text/css" href="/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="/css/jumbotron-narrow.css">
</head>
<body>
<div class="container">
    <div class="header">
        <ul class="nav nav-pills pull-right">
            <li><a href="/">ToyProject</a></li>
        </ul>
        <a href="/"><h3 class="text-muted">RandomChat</h3></a>
    </div>
    <form method="post" action="/v/login" class="jumbotron">
        <h1>RandomChat</h1>
        <p class="lead">로그인</p>
        <div class="mb-3 row">
            <label for="userEmail" class="col-sm-2 col-form-label">Email</label>
            <div class="col-sm-10">
                <input value="${cookie.registerEmail.value}" name="userEmail" type="email" class="form-control" id="userEmail">
            </div>
        </div>
        <div class="mb-3 row">
            <label for="password" class="col-sm-2 col-form-label">Password</label>
            <div class="col-sm-10">
                <input name="password" type="password" class="form-control" id="password">
            </div>
        </div>
        <div class="col mt-3 text-end">
            <button type="submit" class="w-25 btn btn-primary btn-sm">로그인</button>
            <a href="/v/login/registration-form"><button type="button" class="w-auto btn btn-outline-success btn-sm">회원가입</button></a>
        </div>
    </form>
</div>
</body>
<script type="text/javascript" src="/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
</html>