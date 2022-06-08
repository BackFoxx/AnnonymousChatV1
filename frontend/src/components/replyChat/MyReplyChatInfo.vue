<template>
    <b-list-group-item>
        <p>작성일: {{replyInfo.createDate}}</p>
        <p>Chat 내용: {{replyInfo.chatContent}}</p>
        <p>Chat 작성일: {{replyInfo.chatCreateDate}}</p>
    </b-list-group-item>
</template>

<script>
import axios from 'axios'

export default {
    name: 'MyReplyChatInfo',
    props: {
        reply_id: Number
    },
    data() {
        return {
            replyInfo: Object
        }
    },
    created() {
        axios.get(`reply/my-reply/info/${this.reply_id}`)
        .then(response => {
            this.replyInfo = response.data;
        })
        .catch(error => {
            console.log(error);
        })
    }
}
</script>