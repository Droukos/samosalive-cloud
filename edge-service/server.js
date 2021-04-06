const express = require('express');
//const spdy = require('spdy');
//const fs = require('fs');
const app = express();
const {
    createProxyMiddleware
} = require('http-proxy-middleware');
const PORT = 8081;
const authApiLoc = 'http://localhost:8089'
const authApiName = 'http://auth-service:8089'
const authApi = authApiLoc;
//const HOST = '0.0.0.0';
app.use(express.static('public'));

//const options = {
//    key: fs.readFileSync(__dirname + '/server.key'),
//    cert:  fs.readFileSync(__dirname + '/server.crt')
//}

//app.use('/api', createProxyMiddleware({ target: authApi, changeOrigin: true }));

app.get('/**', (req, res) => {
    res.sendFile('./public/index.html', {
        root: __dirname
    });
});

//spdy.createServer(options, app)
//    .listen(PORT, (error) => {
//        if (error) {
//            console.error(error)
//            return process.exit(1)
//        } else {
//            console.log('Listening on port ${PORT}.');
//        }
//    })

app.listen(PORT, () => console.log(`listening on port ${PORT}!`));