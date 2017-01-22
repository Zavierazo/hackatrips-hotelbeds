<template>
    <div class="carWrapper">
        <form @submit.prevent="confirmBooking">
            <h3 class="heading-3">Reserva transporte para compartir</h3>

            <label for="carFrom">Elige tu trayecto</label>

            <input ref="carFrom" id="carFrom" data-latitude="40.4655112" data-longitude="-3.6165726" value="IFEMA"
                   placeholder="Selecciona un origen" disabled="disabled">
            <input ref="carTo" id="carTo" data-latitude="40.4655112" data-longitude="-3.6165726" value="Demo"
                   placeholder="Selecciona un destino" disabled="disabled">

            <label for="paxes">Plazas</label>
            <select ref="carPaxes" id="paxes">
                <option>1</option>
                <option>2</option>
                <option>3</option>
                <option>4</option>
            </select>

            <button>Reservar</button>
        </form>

        <div class="bookingList" v-if="bookingList.length > 0">
            <h3>Comparte con alguien</h3>

            <ul ref="bookingList">
                <li v-for="booking in bookingList">
                    <span v-html="booking.origen"></span>
                    <svg class="arrowIcon" width="1792" height="1792" viewBox="0 0 1792 1792" xmlns="http://www.w3.org/2000/svg"><path d="M1728 893q0 14-10 24l-384 354q-16 14-35 6-19-9-19-29v-224h-1248q-14 0-23-9t-9-23v-192q0-14 9-23t23-9h1248v-224q0-21 19-29t35 5l384 350q10 10 10 23z"/></svg>
                    <span v-html="booking.destino"></span>
                    {{ booking }}
                </li>
            </ul>
        </div>
    </div>
</template>

<style scoped lang="scss">
    label {
        padding: 15px 0;
    }

    input {
        margin-bottom : 40px;width: 45%;
        margin-right: 10%;
        float : left;

        box-sizing: border-box;
        &:last-of-type {
            margin-right: 0;
        }

    }

    select {
        padding: 15px;
        font-size: 16px;
        width: 100%;

        border: 0;
        background: transparent;
        border: 2px solid #F44336;
    }

    .bookingList {
        clear: both;
    }

    form {

        clear: both;
        width: 80%;
        margin: 2.5em auto 1.5em;

        h3 {
            font-weight: 200;
            font-size: 1.8em;
        }

    }

    button {
        background: #F44336;
        padding: 15px;
        border: 0;
        color: #fff;
        display: block;
        margin: 30px 0 30px 0;
        float: right;
        font-size: 18px;
    }

    .arrowIcon {
        width: 20px;
        height: 20px;
    }
</style>

<script>
    import qs from "qs"
    import axios from "axios"

    export default{
        data(){
            return{
                bookingList: []
            }
        },
        components:{
        },
        mounted() {
            const endPoint = 'http://127.0.0.1:8080/cabify/bookingList'

            axios.get(endPoint).then((response) => {
                this.$data.bookingList = response.data
            })
        },
        methods: {
            confirmBooking() {
                const endPoint = "http://127.0.0.1:8080/cabify/booking"

                const carFrom = this.$refs['carFrom']
                const carTo = this.$refs['carTo']
                const carPaxes = this.$refs['carPaxes']

                const queryString = qs.stringify({
                    'latitudeOrigen': carFrom.getAttribute('data-latitude'),
                    'longitudeOrigen': carFrom.getAttribute('data-longitude'),
                    'latitudeDestino': carTo.getAttribute('data-latitude'),
                    'longitudeDestino': carTo.getAttribute('data-longitude'),
                    'nameOrigen': carFrom.value,
                    'nameDestino': carTo.value,
                    'paxes': carPaxes.value,
                    'hour': this.$parent.$refs['slider'].currentValue
                })

                axios.get(endPoint + '?' + queryString).then((response) => {
                    console.log(response)
                })
            }
        }
    }

</script>
