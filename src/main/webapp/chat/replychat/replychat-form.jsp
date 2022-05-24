<%@ page import="toyproject.annonymouschat.chat.model.Chat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>답장 보내기</title>

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
        <p class="lead">답장 보내기</p>
        <p class="lead">To.</p>
        <div class="input-group mb-3">
            <textarea type="text" class="form-control" rows="5" aria-label="Large" aria-describedby="inputGroup-sizing-sm" disabled><%=((Chat)request.getAttribute("chat")).getContent()%></textarea>
        </div>
        <p class="lead">답장</p>
        <div class="input-group mb-3">
            <textarea name="content" type="text" class="form-control" rows="5" aria-label="Large" aria-describedby="inputGroup-sizing-sm"></textarea>
        </div>
    </div>
    <div class="row">
        <div class="col text-end">
            <button id="submitButton" type="button" class="w-25 btn btn-primary btn-lg">보내기</button>
            <a href="/">
                <button class="w-25 btn btn-outline-primary btn-lg">안 해!</button>
            </a>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
<script>
    $('#submitButton').click(function () {

        let replyChat = {
            'content': $('textarea[name="content"]').val(),
            'chatId' : <%=((Chat)request.getAttribute("chat")).getId()%>
        };

        $.ajax({
            type: 'post',
            url: '/v/reply/save',
            headers: {'Content-Type': 'application/json'},
            data: JSON.stringify(replyChat),
            success: function (result) {
                const parsed = JSON.parse(result);
                if (parsed.ok) {
                    alert(parsed.message);
                    window.location.href = '/v/chat/myreply';
                } else {
                    alert(parsed.message);
                }
            }
        })

    })
</script>
</html>