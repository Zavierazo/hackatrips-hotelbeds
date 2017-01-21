import fp from "path"
import fs from "fs"
import express from "express"
import hbs from "express-hbs"
import favicon from "serve-favicon"
import Handlebars from "handlebars"
import {createStore, combineReducers, applyMiddleware} from "redux"

//import routes from "./src/routes"
//import fetchComponentData from "./src/lib/fetchComponentData"
//import appReducers from "./src/reducers"
//import promiseMiddleware from "./src/lib/promiseMiddleware"

const appServer = function (config) {
    const app = express()

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
    });

    app.use(favicon(fp.join(__dirname, "public", "favicon.png")))

    app.use(express.static("public"))

    app.use("/*", (req, res) => {
        res.render("app")
    })

    return app
}

export default appServer
