import axios from "axios"

export default{
    constructor() {
    },
    send(queryText) {
        const endPoint = 'http://127.0.0.1:3000/api/ms-autosuggest'

        axios.post(endPoint, {
            query: queryText
        }).then((response) => {
            console.log(response)
        }).catch((error) => {
            console.log(error)
        })
    }
}
