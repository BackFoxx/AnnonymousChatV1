<template>
    <div>
        <div class="jumbotron">
            <p class="mt-3 lead">답장 보내기</p>
            <div class="input-group mb-3">
                <textarea v-model="content" type="text" class="form-control" rows="5" aria-label="Large" aria-describedby="inputGroup-sizing-sm"></textarea>
            </div>
            <div class="col text-end">
                <button @click="postChat" type="button" class="w-25 btn btn-primary">보내기</button>
                <button @click="$emit('deActiveReplyForm')" class="w-25 btn btn-outline-primary">안 해!</button>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios'

export default {
    name: 'ReplyChatForm',
    props: ['chat_id'],
    data() {
        return {
            content: ''
        }
    },
    methods: {
        postChat() {
            axios.post('/reply/save', {
                content: this.content,
                chatId: this.chat_id
            })
            .then(() => {
                alert('ReplyChat이 등록되었습니다.');
                this.$emit('deActiveReplyForm');
            })
            .catch(error => {
                console.log(error.response.data)
                alert(error.response.data.message)
            })
        }
    }
}
</script>