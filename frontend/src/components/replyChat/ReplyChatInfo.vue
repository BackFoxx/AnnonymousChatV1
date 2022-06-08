<template>
    <b-list-group>
        <b-list-group-item v-if="repliesInfo.length === 0">
            <p>댓글이 없어요</p>
        </b-list-group-item>
        <b-list-group-item v-else v-for="(reply, index) in repliesInfo" :key="index" class="mt-3">
            <p>REPLY : {{reply.content}}</p>
            <p>작성일 : {{reply.createDate}}</p>
        </b-list-group-item>
    </b-list-group>
</template>

<script>
import axios from 'axios'

export default({
    name: 'ReplyChatInfo',
    props: ['chat_id'],
    data() {
        return {
            repliesInfo: Object
        }
    },
    created() {
        this.getReplies();
    },
    methods: {
        getReplies() {
            axios.get(`/reply/replies/${this.chat_id}`)
            .then(response => {
                this.repliesInfo = response.data
            })
            .catch(error => {
                console.log(error);
            })
        }
    }

})
</script>
