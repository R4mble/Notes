var mammoth = require("mammoth");

mammoth.extractRawText({path: "./aaa.docx"})
    .then(function(result){
    var text = result.value; // The raw text 
    console.log(text);
}).done();