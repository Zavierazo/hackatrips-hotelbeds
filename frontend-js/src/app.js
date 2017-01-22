import Vue from 'vue'
import VueCookie from 'vue-cookie'
import Cookie from 'tiny-cookie'

// Application structure
import auth from './auth'
import store from './store'
import router from './router'

// Vue.js components
import App from './components/App.vue'
import Layout from "./components/Layout.vue"
import InputPassword from "./components/InputPassword.vue"
import Mic from "./components/Mic.vue"
import Mapi from "./components/Mapi.vue"
import Car from "./components/Car.vue"

// Register Vue.js plugins
Vue.use(VueCookie)

// Register components
Vue.component('app-layout', Layout)
Vue.component('input-password', InputPassword)
Vue.component('mic', Mic)
Vue.component('mapi', Mapi)
Vue.component('car-booking', Car)

Vue.config.debug    = true
Vue.config.devtools = true

new Vue({
    router,
    store,
    el: '#app',
    beforeMount: () => {
    },
    render: h => h(App),
    mounted() {
    }
})
