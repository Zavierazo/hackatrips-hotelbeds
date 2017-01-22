<template>
    <div class="app-container">
        <template v-if="$route.matched.length">
            <router-view></router-view>
        </template>
    </div>
</template>

<script>
    export default {
        name: 'app-container',
        data: {},
        created () {
        }
    }
</script>

<style lang="scss">
    @import '../styles/normalize.scss';
    @import '../styles/source_sans_pro.scss';
    /*@import '../styles/cartodb.css';*/
    html {
        box-sizing: border-box;
    }
    *, *:before, *:after {
        box-sizing: inherit;
    }

    label{
        display : block;
        font-size : 18px;
        margin-top :10px;
    }

    .heading-3{
        font-weight: 200;
        font-size: 1.8em;
        display: block;
        padding: 20px 0 0 0;
    }
    input{
        padding: 15px;
        font-size: 16px;
        width : 100%;

        border: 0;
        background: transparent;
        border-bottom: 2px solid #F44336;
        &:focus{
             outline :0;
         }
    }

    body {
        font-family             : 'Source Sans Pro', Helvetica, Arial, sans-serif;
        font-weight             : 400;
        -webkit-font-smoothing  : antialiased;
        -moz-osx-font-smoothing : grayscale;
        color                   : #333;
        font-size               : 80%;
        width                   : 100%;
        margin                  : 0 auto;
        height                  : 100%;
        background: linear-gradient(#ebece7, white);
        background-repeat       : no-repeat;
        background-attachment   : fixed;

    }
</style>
