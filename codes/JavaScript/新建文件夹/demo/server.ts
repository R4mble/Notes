import {buildSchema} from "graphql";
import express from "express";
// @ts-ignore
import graphqlHTTP from "express-graphql";

const schema2 = buildSchema(`
  type Query {
    quoteOfTheDay: String
    random: Float!
    rollDice(numDice: Int!, numSides: Int): [Int]
  }
`);

//curl -X POST -H "Content-Type: application/json" -d '{"query": "{ rollDice(numDice: 4, numSides: 6) }"}' http://localhost:4000/graphql | jq .
const root2 = {
  quoteOfTheDay: () => Math.random() < 0.5 ? 'Take it easy' : 'Salvation lies within',
  random: () => Math.random(),
  rollDice: ({numDice, numSides}) => {
    var output = []
    for (let i = 0; i < numDice; i++) {
      output.push(1 + Math.floor(Math.random() * (numSides || 6)))
    }
    return output
  }
};

var app = express();
app.use('/graphql', graphqlHTTP({
  schema: schema2,
  rootValue: root2,
  graphiql: true,
}));
app.listen(4000);
console.log('Running a GraphQL API server at localhost:4000/graphql');