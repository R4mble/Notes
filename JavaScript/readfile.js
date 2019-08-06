var fs = require('fs')
var Task = require('data.task')
var _ = require('ramda')

var readFile = filename => 
                new Task((rej, res) => 
                    fs.readFile(filename, 'utf-8', (err, data) =>
                        rr ? rej(err) : res(data)))

var f = readFile('metamorphosis').map(_.split('\n')).map(_.head)


