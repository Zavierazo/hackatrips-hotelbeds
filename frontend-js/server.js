import fp from "path"
import axios from "axios"
import express from "express"
import hbs from "express-hbs"
import favicon from "serve-favicon"
import bodyParser from "body-parser"

const microsoftSubscriptionKey = "b2a497a6fc874be684e977e16417b8e8"
const microsoftEndPoint = 'https://api.cognitive.microsoft.com/sts/v1.0/issueToken'

const appServer = function (config) {
    const app = express()

    const parser = bodyParser.json()

    app.set("view engine", "hbs")
    app.set("views", fp.join(__dirname, "templates"))

    app.engine("hbs", hbs.express4({
        //partialsDir: fp.join(__dirname, "templates", "partials")
    }))

    // Allow CORS
    app.use(function (req, res, next) {
        res.header("Access-Control-Allow-Origin", "*")
        res.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE")
        res.header("Access-Control-Allow-Headers", "Content-Type")

        next()
    })

    app.post('/api/ms-cognitive-token', (req, res) => {
        axios.post(microsoftEndPoint, {}, {
            headers: {
                'Ocp-Apim-Subscription-Key': microsoftSubscriptionKey
            }
        }).then(
            (response) => {
                res.send({token: response.data})
            }
        ).catch(
            (error) => {
                console.log(error)
                res.send({error: 'error'})
            }
        )
    })

    app.use(favicon(fp.join(__dirname, "public", "favicon.png")))

    app.use(express.static("public"))

    app.use("/*", (req, res) => {
        res.render("app")
    })

    return app
}

export default appServer
