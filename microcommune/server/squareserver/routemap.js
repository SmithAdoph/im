/**
 * session:
 *  http://127.0.0.1:8061/api2/session/event?account=user1&sessionID=user1231325456546
 *  http://127.0.0.1:8061/api2/session/notify?account=user1&sessionID=user1231325456546&eventID=update
 *     post     JSON.stringify(event={event:"update", data,....})
 */
var requestHandlers = require("./requestHandlers");

var routemap = {
    "get": {
        "/api2/square/:operation": requestHandlers.squareManage
    },
    "post": {
        "/api2/square/:operation": requestHandlers.squareManage
    }
};

module.exports = routemap;