<template>
    <div>
        <div class="jumbotron">
            <p class="lead">편지함</p>
            <div class="card">
                <div v-if="isChatPresent" id="chat_card_body" class="card-body">
                    <h5 class="card-title">{{content}}</h5>
                    <p id="chat-content" class="card-text">{{createDate}}</p>
                    <a @click="activeReplyForm" type="button" class="d-block">답장하기</a>
                    <a @click="getRandomChat" type="button" class="d-block">다른 편지 보기</a>
                </div>
                <div v-else id="chat_card_body" class="card-body">
                    <p class="lead mt-3">게시글이 없습니다.</p>
                </div>
            </div>
            <reply-chat-form v-on:deActiveReplyForm="activeReplyForm" :chat_id="chat_id" v-if="replyFromToggle" />
        </div>
        <div class="row">
            <div class="col mt-3 text-end">
                <button @click="$router.push({ name: 'MyChatPage' })" type="button" class="w-auto btn btn-primary btn-lg">내가 보낸 편지</button>
                <a @click="$router.push({name: 'MyReplyChatPage'})">
                    <button type="button" class="w-auto btn btn-primary btn-lg">내가 쓴 답장</button>
                </a>
                <a @click="$router.push({name: 'IndexPage'})">
                    <button class="w-25 btn btn-outline-primary btn-lg">홈</button>
                </a>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import ReplyChatForm from '@/components/replyChat/ReplyChatForm.vue';

export default {
  components: { ReplyChatForm },
    name: 'ChatPostBoxPage',
    created() {
        this.getRandomChat();
    },
    data() {
        return {
            chat_id: Number,
            content: String,
            createDate: String,
            isChatPresent: false,

            //reply
            replyFromToggle: false
        }
    },
    methods: {
        getRandomChat() {
            axios.get('/chat/postbox')
            .then(response => {
                const chat = response.data;
                if (chat) {
                    this.chat_id = chat.id;
                    this.content = chat.content;
                    this.createDate = chat.createDate;
                    this.isChatPresent = true
                    this.replyFromToggle = false
                }
                else { this.isChatPresent = false }
            })
        },
        activeReplyForm() {
            this.replyFromToggle = !this.replyFromToggle;
        }
    }
}
</script>
