<template>
    <div class="carWrapper">
        <div class="bookingList" v-if="bookingList.length > 0">
            <h3>Comparte coche. Trayectos disponibles:</h3>

            <ul ref="bookingList">
                <li class="bookingItem" v-for="booking in bookingList" :data-id="booking.id" :data-paxes="booking.paxes">
                    <span class="origen" v-html="booking.origen"></span>

                    <svg class="arrowIcon" width="1792" height="1792" viewBox="0 0 1792 1792"
                         xmlns="http://www.w3.org/2000/svg">
                        <path d="M1728 893q0 14-10 24l-384 354q-16 14-35 6-19-9-19-29v-224h-1248q-14 0-23-9t-9-23v-192q0-14 9-23t23-9h1248v-224q0-21 19-29t35 5l384 350q10 10 10 23z"/>
                    </svg>

                    <span class="destino" v-html="booking.destino"></span>

                    <span class="details">
                        a las <span><span v-html="booking.hour"></span>:00</span>

                        (<span><span v-html="(4-booking.paxes)"></span> plazas libres</span>)
                    </span>

                    <div class="row">
                        <div class="requestedPaxes">
                            <label>Plazas solicitadas</label>

                            <select name="selectedPaxes">
                                <option v-for="i in (4-booking.paxes)">{{ i }}</option>
                            </select>
                        </div>

                        <button class="proposalButton" @click="sharingProposal($event)">Proponer</button>
                    </div>
                </li>
            </ul>
        </div>

        <form @submit.prevent="confirmBooking">
            <h3 class="heading-3">Reserva coche para compartir:</h3>

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

        <modal v-if="showConfirmModal" @close="showConfirmModal = false">
            <h3 slot="header">Reserva confirmada</h3>
            <div slot="body">Se ha confirmado tu reserva en Cabify.</div>
        </modal>

        <modal v-if="showModal" @close="showModal = false">
            <h3 slot="header">Propuesta para compartir coche</h3>
            <div slot="body">Se ha enviado una propuesta al usuario que confirmó la reserva
                <strong><span v-html="selectedBooking"></span></strong> para ocupar
                <strong><span v-html="selectedPaxes"></span></strong> de sus plazas libres.
            </div>
        </modal>
    </div>
</template>

<style scoped lang="scss">
    label {
        padding: 15px 0;
    }

    input {
        margin-bottom: 40px;
        width: 45%;
        margin-right: 10%;
        float: left;

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
        .bookingItem {
            transition: opacity 0.5s;
        }

        .details {
            font-size: 17px;
            margin-left: 8px;
        }

        .origen,
        .destino {
            font-size: 22px;
        }

        ul {
            padding: 0;
            margin: 0;

            li {
                &:after {
                    clear: both;
                }

                clear: both;

                list-style: none;
                margin-bottom: 1.5em;
                padding-bottom: 1em;
                border-bottom: 3px solid #ddd;

            }
        }
    }

    .bookingList,
    form {

        clear: both;
        width: 80%;
        margin: 2.5em auto 1.5em;

        h3 {
            font-weight: 800;
            font-size: 1.8em;
        }

    }

    button, .proposalButton {
        background: #F44336;
        padding: 15px;
        border: 0;
        color: #fff;
        display: block;
        margin: 30px 0 30px 0;
        float: right;
        font-size: 18px;
        min-width: 150px;
    }

    .arrowIcon {
        width: 20px;
        height: 20px;
        top: 3px;
        position: relative;
        margin: 0 5px;
    }

    .row {
        display: table;
        position: relative;
        width: 100%;

        .requestedPaxes {
            width: 80%;
            float: left;
            display: table-cell;

            select {
                width: 70%;
            }
        }

        .proposalButton {
            float: right;
            margin: 0;
            position: absolute;
            bottom: 0;
            right: 0;
            display: table-cell;
            vertical-align: bottom;
        }
    }

</style>

<script>
    import qs from "qs"
    import axios from "axios"

    export default{
        data(){
            return{
                bookingList: [],
                showModal: false,
                showConfirmModal: false,
                selectedBooking: '',
                selectedPaxes: 0,
                paxes: 4
            }
        },
        mounted() {
            const endPoint = 'http://127.0.0.1:8080/cabify/bookingList'

            axios.get(endPoint).then((response) => {
                this.setBookingList(response.data)
            })
        },
        methods: {
            sharingProposal(event) {
                const self = this
                const parent = event.target.parentNode
                const els = parent.childNodes

                els.forEach( (el) => {
                    el.childNodes.forEach( (el) => {
                        if (el.name == 'selectedPaxes') {
                            self.$data.selectedPaxes = el.value
                        }
                    })
                })

                self.$data.selectedBooking = parent.parentNode.getAttribute('data-id')
                self.$data.paxes = parent.parentNode.getAttribute('data-paxes')

                self.$data.showModal = true

                const endPoint = 'http://127.0.0.1:8080/cabify/update'

                const queryString = qs.stringify({
                    'id': self.$data.selectedBooking,
                    'paxes': parseInt(self.$data.paxes) + parseInt(self.$data.selectedPaxes)
                })

                axios.get(endPoint + '?' + queryString).then((response) => {
                    self.setBookingList(response.data)
                })
            },
            confirmBooking() {
                const self = this
                const endPoint = "http://127.0.0.1:8080/cabify/booking"

                const carFrom = self.$refs['carFrom']
                const carTo = self.$refs['carTo']
                const carPaxes = self.$refs['carPaxes']

                const queryString = qs.stringify({
                    'latitudeOrigen': carFrom.getAttribute('data-latitude'),
                    'longitudeOrigen': carFrom.getAttribute('data-longitude'),
                    'latitudeDestino': carTo.getAttribute('data-latitude'),
                    'longitudeDestino': carTo.getAttribute('data-longitude'),
                    'nameOrigen': carFrom.value,
                    'nameDestino': carTo.value,
                    'paxes': carPaxes.value,
                    'hour': self.$parent.$refs['slider'].currentValue
                })

                axios.get(endPoint + '?' + queryString).then((response) => {
                    this.setBookingList(response.data)
                    self.$data.showConfirmModal = true
                })
            },
            setBookingList(bookingList) {
                var bookings = []

                bookingList.forEach( (item) => {
                    if ((4-item.paxes) > 0) {
                        bookings.push(item)
                    }
                })

                console.log('bookings after filter', bookings)

                this.$data.bookingList = bookings
            }
        }
    }

</script>
