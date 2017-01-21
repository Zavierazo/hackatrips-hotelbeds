import axios from "axios"
import qs from "qs"
import Recorder from "recorderjs"
import Guid from "guid"
import base64 from "base-64"

export default{
    constructor() {
        this.recorder;
        this.input;
        this.blob;
    },
    init() {
        const self = this;

        function startUserMedia(stream) {
            const audioContext = new AudioContext;
            self.input = audioContext.createMediaStreamSource(stream);
            console.log('Media stream created.', self.input, stream);
            self.recorder = new Recorder(self.input, {
                workerPath: 'demo'
            });
            console.log('Recorder initialised.', self.recorder);
        }

        navigator.getUserMedia({audio: true}, startUserMedia, function (e) {
            console.log('No live audio input: ' + e);
        });
    },
    start() {
        const self = this;

        self.recorder && self.recorder.record();
        console.log('Recording...', self.recorder);
    },
    stop() {
        const self = this;

        self.recorder && self.recorder.stop();
        console.log('Stopped recording.', self.recorder);

        self.recorder.exportWAV(function (blob) {
            console.log(blob, self.input)

            self.blob = blob
            self.recorder.clear()
            self.recorder = undefined
        })
    },
    send() {
        const self = this;

        console.log(self.blob, typeof self.blob);

        axios.post('http://127.0.0.1:3000/api/ms-cognitive-token').then((response) => {
            const accessToken = response.data.token;
            const appId = 'D4D52672-91D7-4C74-8AD8-42B1D98141A5';

            const endPoint = 'https://speech.platform.bing.com/recognize'

            axios.post(endPoint, qs.stringify({
                'version': '3.0',
                'scenarios': 'ulm',
                'appid': appId,
                'locale': 'en-US',
                'device.os': 'wp7',
                'format': 'json',
                'requestid': Guid.raw(),
                'instanceid': '41265e54-8bbe-406a-b1c0-82f7147e2330'
            }), {
                headers: {
                    'Authorization': 'Bearer ' + accessToken,
                    'Content-Type': 'audio/wav; samplerate=16000'
                    //'Content-Length': self.blob.size
                },
                data: self.blob
            }).then((response) => {
                console.log(response)
            }).catch((error) => {
                console.log(error)
            })
        }).catch((error) => {
            console.error(error)
        })
    }
}
