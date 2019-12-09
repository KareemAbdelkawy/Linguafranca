const express = require('express')
const app = express()
const port = 3000


var o = {} // empty Object
var key = 'translation saved';
o[key] = []; // empty Array, which you can push() values into


var hello = {
    es: 'hola',
    fr: 'Bonjour',
    de: 'hallo'

};
var bye = {
    es: 'adiós',
    fr: 'au revoir',
    de: 'Tschüss'
};
o[key].push(hello);
o[key].push(bye);
app.get('/', (req, res) => {
    
    
    res.send(o)
})

app.listen(port, () => console.log(`Example app listening on port ${port}!`))