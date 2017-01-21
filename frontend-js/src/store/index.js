import Vue from 'vue'
import Vuex from 'vuex'

import Cookie from 'tiny-cookie'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
    },
    actions: {
        goHome(context) {
            console.log('goHome', context)
        }
    }
})
