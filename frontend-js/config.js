var fs = require("fs");
var path = require("path");

var minimist = require("minimist");
var objectAssign = require("object-assign");

function config(procArgV, defaults) {
	var argv = minimist(procArgV.slice(2));

	var defaults = {
		"environmentName": "default",
		"development": false,
		"server": {
			"port": 3000,
			"host": "127.0.0.1",
			"assetPath": "http://127.0.0.1:3000/assets/"
		},
		"services": {
			"pinterest": {
				"appId": "putYourAppIdHere",
				"appSecret": "putYourSecretsHere"
			}
		}
	};

	if (argv.env) {
		var configFileName = "." + argv.env + ".js";
		var configFilePath = path.join(__dirname, configFileName);

		try {
			var config = require(configFilePath);

			console.log("Loading config from %s", configFileName);
			defaults = objectAssign(defaults, config);
		} catch (e) {
			console.log("[ERROR] Cannot load file %s", configFileName);
		}
	} else {
		console.log("Loading default config");
	}

	return defaults;
}

module.exports = config;
