<template>
    <div>
        <mapi></mapi>
        <div class="timeSelect">
            <span class="sliderCaption">Hora</span>
            <div class="sliderWrapper">
                <vue-slider class="slider" v-bind="sliderOpts" ref="slider" v-model="sliderOpts.value"></vue-slider>
            </div>
        </div>

        <car-booking></car-booking>
    </div>
</template>

<style lang="scss" scoped>
    .timeSelect {
        width: 80%;
        margin: 3em auto 1.5em;

        &:after {
            visibility: hidden;
            display: block;
            font-size: 0;
            content: " ";
            clear: both;
            height: 0;
        }
    }
    .sliderCaption {
        float: left;
        width: 10%;
        display: inline-block;
        font-size: 1.8em;
        font-weight: 200;
        font-color: #333;
    }
    .sliderWrapper {
        width: 90%;
        float: right;
        display: inline-block;
    }
</style>

<script>
    import store from "../store"
    import vueSlider from '../../node_modules/vue-slider-component/src/vue2-slider.vue'
    import moment from "moment"

    export default {
        data() {
            return {
                sliderOpts: {
                    height: 10,
                    value: 1,
                    dotSize: 20,
                    min: 1,
                    max: 24,
                    interval: 1,
                    disabled: false,
                    show: true,
                    speed: 0.3,
                    reverse: false,
                    lazy: true,
                    tooltip: 'always',
                    piecewise: true,
                    formatter: "{value}:00",
                    tooltipStyle: {
                        "backgroundColor": "#666",
                        "borderColor": "#666"
                    },
                    bgStyle: {
                      "backgroundColor": "#efefef",
                      //"boxShadow": "inset 0.5px 0.5px 3px 1px rgba(0,0,0,.36)"
                    },
                    processStyle: {
                      "backgroundColor": "#999"
                    }
                }
            }
        },
        created() {
            const currentHour = moment().hours() + 1
            this.sliderOpts.value = currentHour
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
        },
        methods: {
            sliderCallback(value) {
                const currentHour = moment().hours() + 1

                if (value <= currentHour) {
                    this.sliderOpts.value = currentHour
                }
            }
        }
    }
</script>
