<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>내가 쓴 편지</title>

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
        <p class="lead">내가 쓴 편지</p>
        <p class="lead"><%=request.getRequestURI()%></p>
        <div id="carouselExampleControls" class="carousel carousel-dark slide carousel-fade">
            <div class="carousel-inner">
                <c:if test='${empty replies}'>
                    <div id="chat_card" class="card">
                        <div id="chat_card_body" class="card-body">
                            <p class="lead mt-3">게시글이 없습니다.</p>
                        </div>
                    </div>
                </c:if>
                <c:if test="${not empty replies}">
                    <c:forEach var="reply" items="${replies}">
                        <table class="table caption-top">
                            <caption>List of users</caption>
                            <thead>
                            <tr>
                                <th scope="col">${reply.chatCreateDate}</th>
                                <th scope="col">${reply.chatContent}</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <th scope="row">${reply.replyCreateDate}</th>
                                <td>${reply.replyContent}</td>
                            </tr>
                            </tbody>
                        </table>
                        <button onclick="deleteReply(this.id)" id="deleteButton_${reply.replyId}" type="button" class="w-25 btn-sm btn btn-outline-warning">삭제</button>
                    </c:forEach>
                </c:if>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
<script type="text/javascript">
    function placeReplies(result) {
        const parsed = JSON.parse(result);
        let table = '';
        parsed.forEach(function (reply) {
            table += ('<tr><th scope="row">' + reply.createDate + '</th><td>' + reply.content + '</td></tr>')
        })
        document.getElementById('reply_table').innerHTML = table;
    }

    function showReplies(btnId) {
        const chatId = btnId.split('_')[1];

        $.ajax({
            type: 'get',
            url: '/v/reply/find?chatId=' + chatId,
            success: function (result) {
                placeReplies(result);
            }
        })
    }

    function deleteReply(btnId) {
        const deleteDto = {
            'replyId' : btnId.split('_')[1]
        }

        $.ajax({
            type: 'post',
            url: '/v/reply/delete',
            headers: {'Content-Type': 'application/json'},
            data: JSON.stringify(deleteDto),
            success: function (result) {
                const parsed = JSON.parse(result);
                if (parsed.ok) {
                    alert(parsed.message);
                    window.location.href = '/v/chat/myreply';
                }
            }
        })
    }
</script>
</html>