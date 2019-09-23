"use strict";
exports.__esModule = true;
var graphql_1 = require("graphql");
var express_1 = require("express");
// @ts-ignore
var express_graphql_1 = require("express-graphql");
var schema2 = graphql_1.buildSchema("\n  type Query {\n    quoteOfTheDay: String\n    random: Float!\n    rollDice(numDice: Int!, numSides: Int): [Int]\n  }\n");
//curl -X POST -H "Content-Type: application/json" -d '{"query": "{ rollDice(numDice: 4, numSides: 6) }"}' http://localhost:4000/graphql | jq .
var root2 = {
    quoteOfTheDay: function () { return Math.random() < 0.5 ? 'Take it easy' : 'Salvation lies within'; },
    random: function () { return Math.random(); },
    rollDice: function (_a) {
        var numDice = _a.numDice, numSides = _a.numSides;
        console.log(numSides);
        var output = [];
        for (var i = 0; i < numDice; i++) {
            output.push(1 + Math.floor(Math.random() * (numSides || 6)));
        }
        return output;
    }
};
var app = express_1["default"]();
app.use('/graphql', express_graphql_1["default"]({
    schema: schema2,
    rootValue: root2,
    graphiql: true
}));
app.listen(4000);
console.log('Running a GraphQL API server at localhost:4000/graphql');
