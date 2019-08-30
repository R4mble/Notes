var express = require('express');
var app = express();
 
app.get('/', function (req, res) {
   res.send('Hello ');
})

 
var server = app.listen(80, function () {
  var host = server.address().address
  var port = server.address().port
})