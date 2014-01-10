var session = {};

smsSession = {};

session.event = function (data, response) {
    response.asynchronous = 1;
    smsSession["sms"] = response;
}

session.notify = function (data, response) {

    var sessionResponse = smsSession["sms"];
    sessionResponse.write(JSON.stringify(data));
    sessionResponse.end();

    response.write(JSON.stringify({
        "information": "send sms success"
    }));
    response.end();
}

module.exports = session;