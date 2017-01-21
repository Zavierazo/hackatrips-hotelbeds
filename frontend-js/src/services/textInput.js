import axios from "axios"

export default{
    constructor() {
    },
    send(queryText) {
        const endPoint = 'http://localhost:8080/minube/textSearch'

        return axios.get(endPoint + '?text=' + queryText)
    }
}
