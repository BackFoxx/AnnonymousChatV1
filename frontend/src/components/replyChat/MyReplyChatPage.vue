<template>
    <b-list-group>
        <template v-for="(reply, index) in myReplies">
            <b-list-group-item class="d-flex justify-content-between align-items-center" :key="index">
                {{reply.replyContent}}
                <div>
                    <b-button v-b-toggle="'info_'+reply.replyId" variant="primary">상세</b-button>
                    <b-button @click="deleteReply(reply.replyId)" variant="danger">삭제</b-button>
                </div>
            </b-list-group-item>
            <b-collapse v-bind:id="'info_'+reply.replyId" class="mt-2" :key="reply.replyId">
                <my-reply-chat-info :reply_id="reply.replyId" />
            </b-collapse>
        </template>
    </b-list-group>
</template>

<script>
import axios from 'axios';
import MyReplyChatInfo from '@/components/replyChat/MyReplyChatInfo.vue';

export default {
  components: { MyReplyChatInfo },
    name: 'MyReplyChatPage',
    data() {
        return {
            myReplies: [],
        }
    },
    created() {
        this.getMyReplies();
    },
    methods: {
        getMyReplies() {
            axios.get('/reply/my-reply')
            .then(response => {
                this.myReplies = response.data;
            })
        },
        deleteReply(reply_id) {
            axios.post('/reply/delete', {
                replyId: reply_id
            })
            .then(() => {
                alert('Reply 삭제 완료되었습니다.')
                this.getMyReplies();
            })
            .catch((error) => {
                alert('모종의 이유로 삭제에 실패하였습니다.');
                console.log(error);
            })
        },
    }
}
</script>