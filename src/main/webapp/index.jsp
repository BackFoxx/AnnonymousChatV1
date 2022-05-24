<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Index</title>

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
    <div class="jumbotron">
        <h1>RandomChat</h1>
        <c:set var="user" value='<%=request.getAttribute("user")%>' />
        <c:if test='${empty user}'>
            <p class="lead">회원 기능</p>
            <p>
                <a class="btn btn-lg btn-outline-primary" href="/v/login/registration-form">회원 가입</a>
                <a class="btn btn-lg btn-primary" href="/v/login/login-form">로그인</a>
            </p>
        </c:if>
        <c:if test='${not empty user}'>
            <p class="lead">${user.userEmail}님 반갑습니다!</p>
            <p>
                <a class="btn btn-lg btn-outline-success" href="/v/chat/postbox">편지함 보기</a>
                <a class="btn btn-lg btn-success" href="/v/chat/post">편지 보내기</a>
                <a class="btn btn-lg btn-outline-dark" href="/v/logout">로그 아웃</a>
            </p>
        </c:if>
    </div>
    <div class="footer">
        <p>&copy; Hello Shop V2</p>
    </div>
</div>
</body>
<script type="text/javascript" src="/js/bootstrap.bundle.min.js"></script>
</html>