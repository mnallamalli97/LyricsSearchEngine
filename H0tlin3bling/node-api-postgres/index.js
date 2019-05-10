const db = require('./queries')


const express = require('express')
const bodyParser = require('body-parser')
const app = express()
const port = process.env.PORT


app.use(bodyParser.json())
app.use(
  bodyParser.urlencoded({
    extended: true,
  })
)

app.get('/', (request, response) => {
  response.json({ info: 'Node.js, Express, and Postgres API' })
})

app.get('/search/and/:keywords', db.andQuery)

app.get('/search/or/:keywords', db.orQuery)

app.listen(port, () => {
  console.log(`App running on port ${port}.`)
})

