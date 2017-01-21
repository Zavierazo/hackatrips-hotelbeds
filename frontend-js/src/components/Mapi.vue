<template>
    <div class="map">
        <div class="map__header">
            <a href="/" replace class="map__header-back">
                <svg version="1.1" id="Capa_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px" viewBox="0 0 477.175 477.175" style="enable-background:new 0 0 477.175 477.175;" xml:space="preserve">
                    <g>
                        <path d="M145.188,238.575l215.5-215.5c5.3-5.3,5.3-13.8,0-19.1s-13.8-5.3-19.1,0l-225.1,225.1c-5.3,5.3-5.3,13.8,0,19.1l225.1,225 c2.6,2.6,6.1,4,9.5,4s6.9-1.3,9.5-4c5.3-5.3,5.3-13.8,0-19.1L145.188,238.575z"></path>
                    </g>
                </svg>
            </a>
            <h3 ref="locationCaption" class="map__header-title" v-html="locationCaption"></h3>
        </div>
        <div id="map"></div>
    </div>
</template>

<style scoped lang="scss">
    html, body, #map {
        height: 100%;
        padding: 0;
        margin: 0;
    }

    #map {
        height: 400px;
    }

    .map {
        &__header {
            display: flex;
            align-items: center;
            height: 100px;
            position: relative;
            background: #F44336;
        }
        &__header-back {
            width: 35px;
            height: 100px;
            display: inline-block;
            fill: #fff;
            padding: 0 30px;
            border-right: 2px solid #fff;
            svg {
                width: 35px;
                position: absolute;
                top: 50%;
                transform: translate(0, -50%)
            }
        }
        &__header-title {
            margin-left: 30px;
            font-size: 32px;
            color: #fff;
            font-weight: 300;
            display: inline-block;
        }
    }
</style>

<script>
    export default {
        data: () => {
            return {
                locationCaption: ''
            }
        },
        beforeMount: function() {
            if (typeof this.$route.params.text !== 'undefined') {
                this.$data.locationCaption = this.$route.params.text
            }
        },
        mounted: function () {
            this.setCaption(this.$data.locationCaption)

            cartodb.createVis('map', 'http://documentation.cartodb.com/api/v2/viz/2b13c956-e7c1-11e2-806b-5404a6a683d5/viz.json', {
                shareable: true,
                title: true,
                description: true,
                search: true,
                tiles_loader: true,
                center_lat: 0,
                center_lon: 0,
                zoom: 2
            })
                .done(function (vis, layers) {
                    // layer 0 is the base layer, layer 1 is cartodb layer
                    // setInteraction is disabled by default
                    layers[1].setInteraction(true);
                    layers[1].on('featureOver', function (e, latlng, pos, data) {
                        cartodb.log.log(e, latlng, pos, data);
                    });
                    // you can get the native map to work with it
                    var map = vis.getNativeMap();
                    // now, perform any operations you need
                    // map.setZoom(3);
                    // map.panTo([50.5, 30.5]);
                })
                .error(function (err) {
                    console.log(err);
                });
        },
        methods: {
            record: function () {

            },
            setCaption(text) {
                this.$refs['locationCaption'].innerHTML = text
            }
        }
    }
</script>
