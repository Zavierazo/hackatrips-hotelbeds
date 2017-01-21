import Vue from 'vue'
import VueRouter from 'vue-router'

import auth from '../auth'

import HotelSearch from '../components/accommodation/Search.vue'
import TicketSearch from '../components/tickets/Search.vue'
import SignInForm from '../components/auth/SignInForm.vue'

Vue.use(VueRouter)

function requireAuth(to, from, next) {
    if (!auth.loggedIn()) {
        next({
            name: 'sign-in',
            query: {redirect: to.fullPath}
        })
    } else {
        next()
    }
}

const router = new VueRouter({
    mode: 'history',
    root: '/',
    routes: [
        {
            name: 'home',
            path: '/',
            component: HotelSearch,
            beforeEnter: requireAuth
        },
		{
            name: 'tickets',
            path: '/tickets',
            component: TicketSearch,
            beforeEnter: requireAuth
        },
        {
            name: 'sign-in',
            path: '/sign-in',
            component: SignInForm
        },
        {
            name: 'sign-out',
            path: '/sign-out',
            beforeEnter (to, from, next) {
                auth.logout(() => {
                    router.push({ name: 'sign-in', query: { from: 'session' } })
                })
            }
        }
    ]
})

export default router
