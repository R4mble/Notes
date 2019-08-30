var cmd = require('node-cmd');
 
var i = 0;
setInterval(() => {
    let res = cmd.get('tsc Site.ts');
    console.log(res)
    console.log('ts => js...' + ++i)
}, 3000)