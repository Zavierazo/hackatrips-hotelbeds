import Vue from 'vue'
import i18n from 'vue-i18n'
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
import SelectLanguage from "./components/SelectLanguage.vue"

// Register Vue.js plugins
Vue.use(i18n)
Vue.use(VueCookie)

// Register components
Vue.component('app-layout', Layout)
Vue.component('input-password', InputPassword)
Vue.component('select-language', SelectLanguage)

const locales = {
    'en': {
        name: 'English (UK)',
        auth: {
            forgot_password: 'Forgot your password?',
            username: 'User name',
            password: 'Password',
            sign_in: 'Sign in',
            login_first: 'You need to login first',
            signed_out: 'You\'ve been signed out',
            bad_login: 'Incorrect username or password',
        },
        footer: {
            copyright: 'Bedsonline © All rights reserved'
        }
    },
    'es': {
        name: 'Español',
        auth: {
            forgot_password: '¿Has olvidado tu contraseña?',
            username: 'Usuario o e-mail',
            password: 'Contraseña',
            sign_in: 'Entrar',
            login_first: 'Necesitas autenticarte antes',
            signed_out: 'Has cerrado sesión',
            bad_login: 'Nombre de usuario o contraseña incorrectos',
        },
        footer: {
            copyright: 'Bedsonline © Todos los derechos reservados'
        }
    }
}

Object.keys(locales).forEach(function (lang) {
    Vue.locale(lang, locales[lang])
})

Vue.config.debug = true
Vue.config.devtools = true

new Vue({
    router,
    store,
    el: '#app',
    beforeMount: () => {
    },
    render: h => h(App),
    mounted() {
        const lang = Cookie.get('lang')

        this.$store.commit('setAvailableLanguages', locales)

        if (lang !== null && typeof lang === 'string') {
            this.$store.commit('setLanguage', lang)
        } else {
            this.$store.commit('setLanguage', 'en') // TODO: set default language
        }
    }
})
