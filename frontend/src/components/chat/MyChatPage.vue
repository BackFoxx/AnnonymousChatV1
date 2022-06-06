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
                        img-width="1024"
                        img-height="580"
                        @sliding-start="onSlideStart"
                        @sliding-end="onSlideEnd"
                        >

                        <template v-for="(item, index) of myChats">
                            <b-carousel-slide img-blank img-alt="Blank image" :key="index">
                                <p style="background-color:#ffffff">{{item.content}}</p>
                                <p>{{item.createDate}}</p>
                            </b-carousel-slide>
                        </template>

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
export default {
    name: 'MyChatPage',
    data() {
        return {
            myChats: [],

            slide: 0,
            sliding: null
        }
    },
    created() {
        this.getMyChats();
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
        }
    }
}
</script>