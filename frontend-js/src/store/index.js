import Vue from 'vue'
import Vuex from 'vuex'

import Cookie from 'tiny-cookie'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
    },
    mutations: {
        setLanguage(state, key) {
        	Vue.set(state, 'language', key)
            Cookie.set('lang', key)
            Vue.config.lang = key
        },
        setAvailableLanguages(state, languages) {
            Vue.set(state, 'available_languages', languages)
        }
    },
    getters: {
    	currentLanguage: state => {
            return state['language']
    	},
        availableLanguages: state => {
            return state['available_languages']
        }
    }
})
