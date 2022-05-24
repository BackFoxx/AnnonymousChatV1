<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>편지함</title>

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
        <p class="lead">편지함</p>
        <p class="lead"><%=request.getRequestURI()%></p>
        <div id="chat_card" class="card">
            <div id="chat_card_body" class="card-body">
                <h5 id="chat-title" class="card-title"></h5>
                <p id="chat-content" class="card-text"></p>
                <a onclick="ReplyChatForm()" type="button" class="d-block">답장하기</a>
                <a onclick="getRandomChat()" type="button" class="d-block">다른 편지 보기</a>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col mt-3 text-end">
            <a href="/v/chat/mypostbox">
                <button type="button" class="w-auto btn btn-primary btn-lg">내가 보낸 편지</button>
            </a>
            <a href="/v/chat/myreply">
                <button type="button" class="w-auto btn btn-primary btn-lg">내가 쓴 답장</button>
            </a>
            <a href="/">
                <button class="w-25 btn btn-outline-primary btn-lg">홈</button>
            </a>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
<script>
    window.addEventListener('DOMContentLoaded', function () {
        getRandomChat();
    });

    let chat = {
        'id' : null
    }

    function getRandomChat() {
        $.ajax({
            type: 'get',
            url: '/v/chat/postbox/random',
            success: function (result) {
                const parsed = JSON.parse(result);

                if (parsed !== null) {
                    document.getElementById('chat-title').innerText = parsed.createDate;
                    document.getElementById('chat-content').innerText = parsed.content;
                    chat.id = parsed.id;
                } else {
                    document.getElementById('chat_card_body').className += 'visually-hidden';
                    document.getElementById('chat_card').innerHTML = '<p class="lead mt-3">게시글이 없습니다.</p>'
                }
            }
        })
    }

    function ReplyChatForm() {
        window.location.href = '/v/replyForm?id=' + chat.id;
    }
</script>
</html>