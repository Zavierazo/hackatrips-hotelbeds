<template>
    <div>
        <mapi></mapi>
        <vue-slider class="slider" v-bind="sliderOpts" ref="slider" v-model="sliderOpts.value"></vue-slider>
    </div>
</template>

<style lang="scss" scoped>
    .slider {
        margin-top: 2.5em;
    }
</style>

<script>
    import store from "../store"
    import vueSlider from 'vue-slider-component/src/vue2-slider.vue';

    export default {
        data() {
            return {
                sliderOpts: {
                    height: 10,
                    value: 0,
                    dotSize: 20,
                    min: 0,
                    max: 24,
                    interval: 1,
                    disabled: false,
                    show: true,
                    speed: 0.3,
                    reverse: false,
                    lazy: true,
                    tooltip: 'always',
                    piecewise: true,
                    formatter: "{value}:00"
                }
            }
        },
        beforeRouteEnter (to, from, next) {
            if (typeof to.params.text !== 'undefined') {
                setTimeout(function() {
                    next(data => {
                        console.log(data)
                    })
                }, 800)
            } else {
                next({
                    name: 'home'
                })
            }
        },
        components: {
            vueSlider
        }
    }
</script>
