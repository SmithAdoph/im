/**
 * The simplest framework for RESTful api server.
 * Run "$ sudo node main.js" to start the server.
 */

var http = require("http");
var route = require("./lib/route");
var routemap = require("./routemap");

var i = 1;

http.createServer(
    function (request, response) {

        response.writeHead(200, {
            "Content-Type":"application/json; charset=UTF-8"
        });
        route(routemap, request.url, request, response);

        i++;
        console.log("The LBS server has been accessed " + i);
        if (response.asynchronous == null) {
            response.end();
        }

    }).listen(8076);

console.log("The LBS server is running.8076");
