import axios from "axios"

export default{
    constructor() {
    },
    send(queryText) {
        const endPoint = 'http://127.0.0.1:3000/api/suggest'
        return axios.get(endPoint + '?q=' + queryText)
    }
}
