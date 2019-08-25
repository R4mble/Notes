var fs = require("fs")

var mds = []
var path = '/Users/Ramble/Desktop/Notes/_posts/'
var files = []

fs.readdirSync(path).forEach(file => {
    if (file.endsWith("md")) {
        var contArr = fs.readFileSync(path + file).toString().split('\n');
        console.log(contArr[0] === '---')
        if (contArr[0] === '---') {
            var md = {}
            for (let i=1; i<contArr.length; i++) {
                if (contArr[i] === '---') {
                    break
                }
                var kv = contArr[i].split(": ")
                var k = kv[0]
                var v = kv[1]
                if (k === 'tags' || k === 'categories') {
                    v = v.slice(1, -1).split(", ")
                }
                md[k] =  v
                md['file'] = file
            }
        mds.push(md)
        }
    }
}
)

console.log(files.length)
console.log(mds.length)




