var express = require("express");
var winston = require("winston");
var md5 = require("md5");
var path = require("path");
var fs = require("fs");
var bodyParser = require("body-parser");
var objectAssign = require("object-assign");
var Immutable = require("immutable");
var Jimp = require("jimp");

function slugify(s) {
  var _slugify_strip_re = /[^\w\s-]/g;
  var _slugify_hyphenate_re = /[-\s]+/g;
  s = s.replace(_slugify_strip_re, '').trim().toLowerCase();
  s = s.replace(_slugify_hyphenate_re, '-');
  return s;
}

var db = new Immutable.List([{
    id: '0a76f075154f9d560aef31df283f84ab',
    name: 'Item#1',
    value: 998.3
}, {
    id: '614b5c0d89246bd50aacc4a36fca0a37',
    name: 'Item#2',
    value: 7626.3
}, {
    id: '9f7b879262fdc579960bee57ec6d92c5',
    name: 'Item#3',
    value: 9082.2
}, {
    id: '8d76e139dd34ab1e796f702d1a39c734',
    name: 'Item#4',
    value: 1298.00
}, {
    id: '0cbab2bc3f0daaca34db4250d9c7144e',
    name: 'Item#5',
    value: 7827.93
}]);

var cache = {};

function apiService(config) {
    var app = express();

    var logger = new winston.Logger();

    var SERVER_LATENCY = 200;

    app.use(bodyParser.json());

    // Allow CORS
    app.use(function(req, res, next) {
        res.header("Access-Control-Allow-Origin", "*");
        res.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE");
        res.header("Access-Control-Allow-Headers", "Content-Type");

        next();
    });

    app.get("/hotels", function(req, res) {
        console.log(`[POST] /hotels`);

        var apitude = require("./src/lib/apitudeClient").default;

        var params = {
            "destination": "PMI",
            "checkInDate": "2016-09-08",
            "checkOutDate": "2016-09-10",
            "shiftDays": "2"
        }

        var key = params.destination + params.checkInDate + params.checkOutDate + params.shiftDays;

        if (typeof cache[key] !== 'undefined') {
            console.log("Already cached.", key);
            res.send(cache[key]);
        } else {
            console.log("Not cached. Caching...", key);
            apitude.hotelAvail(config.services.apitude, params).then(function(r) {
                cache[key] = r.data;
                res.send(r.data);
            });
        }
    });

    // Get all items
    app.get("/item", function(req, res) {
        console.log(`[GET] /item`);

        const items = db.sortBy(function(item) {
            return item.name
        });

        setTimeout(function() {
            res.end(JSON.stringify(items));
        }, SERVER_LATENCY);
    });

    // Get one item
    app.get("/item/:id", function(req, res) {
        console.log(`[GET] /item/:id`);

        var id = req.params.id;

        var item = db.filter(function(item) { return (item.id === id) }).get(0);

        setTimeout(function() {
            res.end(JSON.stringify(item));
        }, SERVER_LATENCY);
    });

    // Create an item
    app.post("/item", function(req, res) {
        console.log(`[POST] /item`);

        var item = {
            "id": md5(JSON.stringify(req.body) + new Date().getMilliseconds()),
            "name": req.body.name,
            "value": parseFloat(req.body.value)
        };

        db = db.push(item);

        setTimeout(function() {
            res.end(JSON.stringify(item));
        }, SERVER_LATENCY);
    });

    // Update an item
    app.put("/item/:id", function(req, res) {
        console.log(`[PUT] /item/${req.params.id}`);

        var id = req.params.id;
        var newItem = null;

        var pos = db.findIndex(function(item) {
            return (item.id === id);
        });

        newItem = {
            "id": req.params.id,
            "name": req.body.name,
            "value": parseFloat(req.body.value)
        };

        db = db.update(db.findIndex(function(item) {
            return item.id === id;
        }), function(item) {
            return objectAssign(item, newItem);
        });
        
        setTimeout(function() {
            res.end(JSON.stringify(newItem));
        }, SERVER_LATENCY);
    });

    // Delete one item
    app.delete("/item/:id", function(req, res) {
        console.log(`[DELETE] /item/${req.params.id}`);

        var output = []
        var id = req.params.id;
        var element = null;

        db = db.delete(
            db.findIndex(function(item) {
                if (item.id === id) {
                    element = item;
                    return true;
                } else {
                    return false;
                }
            })
        );

        setTimeout(function() {
            res.end(JSON.stringify(element));
        }, SERVER_LATENCY);
    });

    // Sample: http://localhost:3131/pinterest/thumb?url=https://s-media-cache-ak0.pinimg.com/originals/ab/3f/bf/ab3fbf4dc8b24af205784d9275a24c13.jpg
    app.get("/pinterest/thumb", function(req, res) {
        console.log(`[GET] /pinterest/thumb ${req.query.url}`);

        const filePath = path.join(__dirname, "temp", slugify(req.query.url) + ".jpg");

        try {
            const stats = fs.statSync(filePath);

            const img = fs.readFileSync(filePath);
            res.contentType = 'image/png';
            res.contentLength = stats.size;
            res.end(img, 'binary');
        } catch(e) {
            console.log(e);

            Jimp.read(req.query.url, function (err, image) {
                image
                    .resize(236, Jimp.AUTO, Jimp.RESIZE_BICUBIC)
                    .write(filePath, function() {
                        const stats = fs.statSync(filePath);

                        const img = fs.readFileSync(filePath);
                        res.contentType = 'image/png';
                        res.contentLength = stats.size;
                        res.end(img, 'binary');
                    });
            });
        }
    });

    return app;
}

export default apiService;
