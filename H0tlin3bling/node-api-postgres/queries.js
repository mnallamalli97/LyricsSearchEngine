const Pool = require('pg').Pool
const pool = new Pool({
  user: 'mnallamalli97',
  host: 'localhost',
  database: 'api',
  password: 'password',
  port: 5432,
})


const andQuery = (request, response) => {
  const id = parseInt(request.params.id)
  //const keywords = String(request.params.keywords)

  var test = String(request.params.keywords)
  var decodedsearch = decodeURI(test)

  var keywords= decodedsearch.split(" ")
  var keywords_count = keywords.length

  console.log(keywords)
  console.log(keywords.length)
  var input_statement = "";

  if(keywords_count == 1){
    input_statement += "token = '" + keywords[0] + "' "
  }else{
    for(var i = 0; i < keywords_count - 1; i++)
      input_statement += "token = '" + keywords[i] + "' or "
    input_statement += "token = '" + keywords[i]


  }

  console.log(input_statement)

 // input_statement += "token = '" + keywords[strlen(keywords) - 1] + "'"

  pool.query('select sname, A.aname, url from project1.tfidf L inner join project1.song R on L.sid = R.sid inner join project1.artist A on R.aid = A.aid where token = ANY($1) group by  L.sid, sname, A.aid, url having count( distinct token) = $2 order by sum(tfidf) desc;', [keywords, keywords_count], (error, results) => {
    if (error) {
      throw error
    }
    console.log()
    response.status(200).json(results.rows)
  })
}

const orQuery = (request, response) => {
  const id = parseInt(request.params.id)
  //const keywords = String(request.params.keywords)

  var test = String(request.params.keywords)
  var decodedsearch = decodeURI(test)

  var keywords= decodedsearch.split(" ")
  var keywords_count = keywords.length

  console.log(keywords)
  console.log(keywords.length)
  var input_statement = "";

  if(keywords_count == 1){
    input_statement += "token = '" + keywords[0] + "' "
  }else{
    for(var i = 0; i < keywords_count - 1; i++)
      input_statement += "token = '" + keywords[i] + "' or "
    input_statement += "token = '" + keywords[i]


  }

  console.log(input_statement)

 // input_statement += "token = '" + keywords[strlen(keywords) - 1] + "'"

  pool.query('select sname, A.aname, url from project1.tfidf L inner join project1.song R on L.sid = R.sid inner join project1.artist A on R.aid = A.aid where token = ANY($1) group by  L.sid, sname, A.aid, url order by sum(tfidf) desc;', [keywords], (error, results) => {
    if (error) {
      throw error
    }
    console.log()
    response.status(200).json(results.rows)
  })
}

module.exports = {
  andQuery,
  orQuery,
}