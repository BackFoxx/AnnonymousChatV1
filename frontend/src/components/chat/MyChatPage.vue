<template>
    <div class="jumbotron">
        <p class="lead">편지함</p>
        <div id="carouselExampleControls" class="carousel carousel-dark slide carousel-fade">
            <div class="carousel-inner">

                <template v-if="myChats.length === 0">
                    <div class="card">
                        <div class="card-body">
                            <p class="lead mt-3">게시글이 없습니다.</p>
                        </div>
                    </div>
                </template>

                <template v-else>
                    <div>
                        <b-carousel
                        id="carousel-1"
                        v-model="slide"
                        :interval="0"
                        controls
                        indicators
                        background="#FFE9F5"
                        img-width="auto"
                        img-height="auto"
                        @sliding-start="onSlideStart"
                        @sliding-end="onSlideEnd"
                        >
                            <div v-for="(item, index) of myChats" :key="index">
                                <b-carousel-slide img-blank img-alt="Blank image">
                                    <p style="background-color:#ffffff">{{item.content}}</p>
                                    <p>{{item.createDate}}</p>
                                    <b-button @click="switchReplyChatToggle" size="sm">댓글 보기</b-button>
                                    <b-button @click="deleteChat(item.id)" size="sm" variant="danger">삭제</b-button>
                                    <reply-chat-info :chat_id="item.id" v-if="replyChatToggle" />
                                </b-carousel-slide>
                            </div>
                        </b-carousel>

                        <p class="mt-4">
                        {{ slide + 1 }} / {{ myChats.length }}
                        </p>
                    </div>
                </template>

            </div>
        </div>
    </div>
</template>

<style>
    .sr-only {
        display: none;
    }
</style>
<script>
import axios from 'axios'
import ReplyChatInfo from '../replyChat/ReplyChatInfo.vue';

export default {
  components: { ReplyChatInfo },
    name: 'MyChatPage',
    data() {

        return {
            myChats: [],

            slide: 0,
            sliding: null,

            replyChatToggle: false
        }
    },
    created() {
        this.getMyChats();
    },
    watch: {
        slide: function() {
            this.replyChatToggle = false;
        }
    },
    methods: {
        getMyChats() {
            axios.get('/chat/myChat')
            .then(response => {
                this.myChats = response.data
            })
        },
        onSlideStart() {
            this.sliding = true;
        },
        onSlideEnd() {
            this.sliding = false
        },
        deleteChat(chat_id) {
            axios.post('/chat/delete', {
                id: chat_id
            })
            .then(() => {
                alert('삭제 완료되었습니다!');
                this.$router.go();
            })
        },
        switchReplyChatToggle() {
            this.replyChatToggle = !this.replyChatToggle;
        }
    }
}
</script>