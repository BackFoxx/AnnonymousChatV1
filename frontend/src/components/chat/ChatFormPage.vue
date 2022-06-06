<template>
    <div>
        <div class="jumbotron">
            <p class="lead">편지 보내기</p>
            <div class="input-group mb-3">
                <textarea v-model="content" placeholder="140 이내로 자유롭게 작성해주세요" type="text" class="form-control" rows="5" aria-label="Large" aria-describedby="inputGroup-sizing-sm"></textarea>
            </div>
        </div>
        <div class="row">
            <div class="col text-end">
                <button @click="postChat" type="button" class="w-25 btn btn-primary btn-lg">보내기</button>
                <button @click="$router.push({ name: 'IndexPage' })" class="w-25 btn btn-outline-primary btn-lg">안 해!</button>
            </div>
        </div>
    </div>
</template>

<script>
import axios from "axios";

export default {
    name: 'ChatFormPage',
    data() {
        return {
            content: ''
        }
    },
    methods: {
        postChat() {
            axios.post('/chat/save', {
                content: this.content
            })
            .then(() => {
                alert('Chat이 등록되었습니다.');
                this.$router.push({
                    name: 'MyChatPage'
                });
            })
            .catch(error => {
                console.log(error.response.data)
                alert(error.response.data.message)
            })
        }
    }
}
</script>