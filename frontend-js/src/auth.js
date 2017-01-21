import axios from 'axios'
import CryptoJS from 'crypto-js'

//const endpoint = 'http://localhost:8080/auth'
const endpoint = 'http://demodb1-alterpalma.rhcloud.com/auth'

export default {
    login(email, pass, cb) {
        cb = arguments[arguments.length - 1]
        if (localStorage.token) {
            if (cb) cb(true)
            this.onChange(true)
            return
        }
        sendRequest(email, pass, (res) => {
            if (res.authenticated) {
                localStorage.token = res.token
                if (cb) cb(true)
                this.onChange(true)
            } else {
                if (cb) cb(false)
                this.onChange(false)
            }
        })
    },

    getToken() {
        return localStorage.token
    },

    logout(cb) {
        delete localStorage.token
        if (cb) cb()
        this.onChange(false)
    },

    loggedIn() {
        return !!localStorage.token
    },

    onChange() {
    }
}

function sendRequest(email, pass, cb) {
    cb({
        authenticated: true,
        token: Math.random().toString(36).substring(7)
    })
}

/*
 function sendRequest(email, pass, cb) {
 axios.post(endpoint, {
 //password: CryptoJS.SHA1(pass),
 password: pass,
 email: email
 }).then(function(response) {
 console.log(response)

 if (typeof response.data === 'object' && response.data.email == email) {
 cb({
 authenticated: true,
 token: Math.random().toString(36).substring(7)
 })
 } else {
 cb({
 authenticated: false
 })
 }
 }).catch(function(error) {
 console.log(error)
 })
 }
 */