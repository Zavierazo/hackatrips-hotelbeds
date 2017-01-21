"use strict";

require("babel-register")({});

process.on("SIGINT", function() {
  console.log("\nGracefully shutting down from SIGINT (Ctrl-C)");
  process.exit();
});

var config = require("./config")(process.argv);
var server = require("./server").default; // TODO: ES6 conversion results in a default attribute
var api = require("./api").default;

api(config).listen(3131, function() {
  console.log("API listening on port", 3131);
});

server(config).listen(config.server.port, config.server.host, function() {
  console.log("Server listening on %s:%s", config.server.host, config.server.port);
});
