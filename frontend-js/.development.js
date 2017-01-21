var config = {
	"environmentName": "development",
	"server": {
		"port": 3000,
		"host": "127.0.0.1",
		"assetPath": "http://127.0.0.1:3000/assets/"
	},
	"services": {
	    "backend": {
	        "base": 'http://127.0.0.1:8080'
        },
		"microsoft": {
			'Ocp-Apim-Subscription-Key': 'b2a497a6fc874be684e977e16417b8e8',
			'autoSuggestEndpoint': 'https://api.cognitive.microsoft.com/bing/v5.0/suggestions/'

		},
        "demo": {
			"key": "demo",
			"secret": "s3cr3t"
		}
	}
}

module.exports = config;
