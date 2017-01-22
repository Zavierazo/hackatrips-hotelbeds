<template>
    <div class="map">
        <div class="map__header">
            <router-link to="/"  class="map__header-back">
                <svg version="1.1" id="Capa_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px" viewBox="0 0 477.175 477.175" style="enable-background:new 0 0 477.175 477.175;" xml:space="preserve">
                    <g>
                        <path d="M145.188,238.575l215.5-215.5c5.3-5.3,5.3-13.8,0-19.1s-13.8-5.3-19.1,0l-225.1,225.1c-5.3,5.3-5.3,13.8,0,19.1l225.1,225 c2.6,2.6,6.1,4,9.5,4s6.9-1.3,9.5-4c5.3-5.3,5.3-13.8,0-19.1L145.188,238.575z"></path>
                    </g>
                </svg>
          </router-link>
            <h3 class="map__header-title">París, France</h3>
        </div>
        <div id="map"></div>
    </div>
</template>

<style scoped lang="scss">
    html, body, #map {
        height  : 100%;
        padding : 0;
        margin  : 0;
    }

    #map {
        /*width :500px;*/
        height : 400px;
    }
    .map{
        &__header{
             display: flex;
             align-items: center;
             height :100px;
             position : relative;
             background : #F44336;
         }
        &__header-back{
             width: 100px;
             height :100px;
             display : inline-block;
             fill: #fff;
             position : relative;
             border-right: 2px solid #fff;
            svg{
                width: 35px;
                position: absolute;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
            }
         }
         &__header-title{
              margin-left :30px;
              font-size : 32px;
              color : #fff;
              font-weight :300;
              display : inline-block;
          }
    }
</style>

<script>
    //import HeaderComponent from './components/header.vue'
    //import OtherComponent from './components/other.vue'
    //    import cartodb from 'cartodb.js'
    //this.$el
    export default {
        mounted: function () {
            var map=L.map('map', {center: [40.418709,-3.703277],zoom:2});
            L.tileLayer('http://{s}.tile.stamen.com/toner-lite/{z}/{x}/{y}.png', {
                attribution:'Stamen and CartoDB attribution'
            }).addTo(map);

            var clusterSQL = "SELECT * FROM data_group06_mass;";


            cartodb.createLayer(map, {
                user_name:'hackatrips11',
                type:'cartodb',
                sublayers:[{
                    sql: clusterSQL,
                    cartocss:"#layer{  marker-width: 12;  marker-fill: #FD8D3C;  marker-line-width: 1.5;  marker-fill-opacity: 1;  marker-line-opacity: 1;  marker-line-color: #fff; marker-allow-overlap: true;} #layer::labels { text-size: 6; text-fill: #fff; text-opacity: 0.8;text-name: [points_count]; text-face-name: 'DejaVu Sans Book'; text-halo-fill: #FFF; text-halo-radius: 0;text-allow-overlap: true;[zoom>11]{ text-size: 16; }[points_count = 1]{ text-size: 0; }}",
                }]
            },{https:true})
                .on('done', function(layer){
                    console.log(layer)
                    console.log("s");
//                    cdb.vis.Vis.addInfowindow(map,layer.getSubLayer(0), ['points_count', 'contenido'],{infowindowTemplate: $('#infowindow_template').html()});
                })
                .addTo(map);

//            cartodb.createVis('map', 'http://documentation.cartodb.com/api/v2/viz/2b13c956-e7c1-11e2-806b-5404a6a683d5/viz.json', {
//                shareable: true,
//                title: true,
//                description: true,
//                search: true,
//                tiles_loader: true,
//                center_lat: 40.418709,
//                center_lon: -3.703277,
//                zoom: 10
//            })
//                .done(function (vis, layers) {
//                    // layer 0 is the base layer, layer 1 is cartodb layer
//                    // setInteraction is disabled by default
//                    layers[1].setInteraction(true);
//                    layers[1].on('featureOver', function (e, latlng, pos, data) {
//                        cartodb.log.log(e, latlng, pos, data);
//                    });
//                    // you can get the native map to work with it
//                    var map = vis.getNativeMap();
//                    // now, perform any operations you need
//                    // map.setZoom(3);
//                    // map.panTo([50.5, 30.5]);
//                })
//                .error(function (err) {
//                    console.log(err);
//                });
        },
        methods: {
            record: function () {

            }
        }
    }
</script>
