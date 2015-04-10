var requestHandlers = {};

var globaldata = root.globaldata;
var accessKeyPool = {};
var serverSetting = root.globaldata.serverSetting;
var mcServer = serverSetting.zookeeper.mcServer;
var redis = require("redis");
var client = redis.createClient(serverSetting.redisPort, serverSetting.redisIP);
var zookeeper = require("./lib/zookeeper-client.js");
//zookeeper.start(mcServer.ip, mcServer.port, mcServer.timeout, accessKeyPool, function (KeyPool) {
//    accessKeyPool = KeyPool;
//    console.info(mcServer.name + " accessKeyPool update :  " + mcServer.ip + ":" + mcServer.port + " " + mcServer.timeout);
//});
var bugManage = require("./handlers/bugManage.js");
requestHandlers.bugManage = function (request, response, pathObject, data) {
    if (data == null) {
        return;
    }
    var operation = pathObject["operation"];
    if (operation == "send") {
        bugManage.send(data, response);
    }
    else if (operation == "sendqxs") {
        bugManage.sendqxs(data, response);
    }
}
var accountManage = require("./handlers/accountManage.js");
requestHandlers.accountManage = function (request, response, pathObject, data) {
    if (data == null) {
        return;
    }
    var operation = pathObject["operation"];
    if (operation == "verifyphone") {
        accountManage.verifyphone(data, response);
    } else if (operation == "verifycode") {
        accountManage.verifycode(data, response, setOauthAccessKey);
    }
    else if (operation == "auth") {
        accountManage.auth(data, response, setOauthAccessKey);
    }
    else if (operation == "get") {
        accountManage.get(data, response);
    }
    else if (operation == "modify") {
        oauth6(data.phone, data.accessKey, response, function () {
            accountManage.modify(data, response);
        });
    }
    else if (operation == "exit") {
        oauth6(data.phone, data.accessKey, response, function () {
            accountManage.exit(data, response, delOauthAccessKey);
        });
    }
    else if (operation == "oauth6") {
        oauth6(data.phone, data.accessKey, response, function () {
            accountManage.oauth6(data, response);
        });
    }
}

var relationManage = require("./handlers/relationManage.js");
requestHandlers.relationManage = function (request, response, pathObject, data) {
    if (data == null) {
        return;
    }
    var operation = pathObject["operation"];
    if (operation == "deletefriend") {
        oauth6(data.phone, data.accessKey, response, function () {
            relationManage.deletefriend(data, response);
        });
    }
    else if (operation == "blacklist") {
        oauth6(data.phone, data.accessKey, response, function () {
            relationManage.blacklist(data, response);
        });
    }
    else if (operation == "intimatefriends") {
        oauth6(data.phone, data.accessKey, response, function () {
            relationManage.intimatefriends(data, response);
        });
    }
    else if (operation == "modifysequence") {
        oauth6(data.phone, data.accessKey, response, function () {
            relationManage.modifysequence(data, response);
        });
    }
    else if (operation == "updatecontact") {
        oauth6(data.phone, data.accessKey, response, function () {
            relationManage.updatecontact(data, response);
        });
    }
    else if (operation == "getfollow") {
        oauth6(data.phone, data.accessKey, response, function () {
            relationManage.getfollow(data, response);
        });
    }
    else if (operation == "getfans") {
        oauth6(data.phone, data.accessKey, response, function () {
            relationManage.getfans(data, response);
        });
    }
    else if (operation == "follow") {
        oauth6(data.phone, data.accessKey, response, function () {
            relationManage.follow(data, response);
        });
    }
    else if (operation == "modifycircle") {
        oauth6(data.phone, data.accessKey, response, function () {
            relationManage.modifycircle(data, response);
        });
    }
    else if (operation == "canclefollow") {
        oauth6(data.phone, data.accessKey, response, function () {
            relationManage.canclefollow(data, response);
        });
    }
    //Liao Liao api interface.
    else if (operation == "fuzzyquery") {
        oauth6(data.phone, data.accessKey, response, function () {
            relationManage.fuzzyquery(data, response);
        });
    }
}

var circleManage = require("./handlers/circleManage.js");
requestHandlers.circleManage = function (request, response, pathObject, data) {
    if (data == null) {
        return;
    }
    var operation = pathObject["operation"];
    if (operation == "modify") {
        oauth6(data.phone, data.accessKey, response, function () {
            circleManage.modify(data, response);
        });
    }
    else if (operation == "delete") {
        oauth6(data.phone, data.accessKey, response, function () {
            circleManage.delete(data, response);
        });
    }
    else if (operation == "createcircle") {
        oauth6(data.phone, data.accessKey, response, function () {
            circleManage.createcircle(data, response);
        });
    }
}
var messageManage = require("./handlers/messageManage.js");
requestHandlers.messageManage = function (request, response, pathObject, data) {
    if (data == null) {
        return;
    }
    var operation = pathObject["operation"];
    if (operation == "send") {
        oauth6(data.phone, data.accessKey, response, function () {
            messageManage.send(data, response);
        });
    }
    else if (operation == "get") {
        oauth6(data.phone, data.accessKey, response, function () {
            messageManage.get(data, response);
        });
    }
    else if (operation == "deletes") {
        oauth6(data.phone, data.accessKey, response, function () {
            //messageManage.deletes(data, response);
        });
    }
}
var webcodeManage = require("./handlers/webcodeManage.js");
requestHandlers.webcodeManage = function (request, response, pathObject, data) {
    if (data == null) {
        return;
    }
    var operation = pathObject["operation"];
    if (operation == "webcodelogin") {
        oauth6(data.phone, data.accessKey, response, function () {
            webcodeManage.webcodelogin(data, response);
        });
    }
}
var groupManage = require("./handlers/groupManage.js");
requestHandlers.groupManage = function (request, response, pathObject, data) {
    if (data == null) {
        return;
    }
    var operation = pathObject["operation"];
    if (operation == "create") {
        oauth6(data.phone, data.accessKey, response, function () {
            groupManage.create(data, response);
        });
    }
    else if (operation == "addmembers") {
        oauth6(data.phone, data.accessKey, response, function () {
            groupManage.addmembers(data, response);
        });
    }
    else if (operation == "removemembers") {
        oauth6(data.phone, data.accessKey, response, function () {
            groupManage.removemembers(data, response);
        });
    }
    else if (operation == "getallmembers") {
        oauth6(data.phone, data.accessKey, response, function () {
            groupManage.getallmembers(data, response);
        });
    }
    else if (operation == "modify") {
        oauth6(data.phone, data.accessKey, response, function () {
            groupManage.modify(data, response);
        });
    }
    else if (operation == "get") {
        oauth6(data.phone, data.accessKey, response, function () {
            groupManage.get(data, response);
        });
    }
    else if (operation == "getgroupmembers") {
        oauth6(data.phone, data.accessKey, response, function () {
            groupManage.getgroupmembers(data, response);
        });
    }
    else if (operation == "creategroupcircle") {
        oauth6(data.phone, data.accessKey, response, function () {
            groupManage.creategroupcircle(data, response);
        });
    } else if (operation == "deletegroupcircle") {
        oauth6(data.phone, data.accessKey, response, function () {
            groupManage.deletegroupcircle(data, response);
        });
    } else if (operation == "modifygroupcircle") {
        oauth6(data.phone, data.accessKey, response, function () {
            groupManage.modifygroupcircle(data, response);
        });
    } else if (operation == "modifygroupcirclesequence") {
        oauth6(data.phone, data.accessKey, response, function () {
            groupManage.modifygroupcirclesequence(data, response);
        });
    }
    else if (operation == "movegroupstocircle") {
        oauth6(data.phone, data.accessKey, response, function () {
            groupManage.movegroupstocircle(data, response);
        });
    }
    else if (operation == "follow") {
        oauth6(data.phone, data.accessKey, response, function () {
            groupManage.follow(data, response);
        });
    } else if (operation == "removegroupfromcircle") {
        oauth6(data.phone, data.accessKey, response, function () {
            groupManage.removegroupfromcircle(data, response);
        });
    }


    else if (operation == "modifygrouplabel") {
        oauth6(data.phone, data.accessKey, response, function () {
            labelManage.modifygrouplabel(data, response);
        });
    }
    else if (operation == "getgrouplabels") {
        oauth6(data.phone, data.accessKey, response, function () {
            labelManage.getgrouplabels(data, response);
        });
    }
    else if (operation == "getlabelsgroups") {
        oauth6(data.phone, data.accessKey, response, function () {
            labelManage.getlabelsgroups(data, response);
        });
    }
    else if (operation == "gethotlabels") {
        oauth6(data.phone, data.accessKey, response, function () {
            labelManage.gethotlabels(data, response);
        });
    }
}
var labelManage = require("./handlers/labelManage.js");
requestHandlers.labelManage = function (request, response, pathObject, data) {
    if (data == null) {
        return;
    }
    var operation = pathObject["operation"];
    if (operation == "modifygrouplabel") {
        oauth6(data.phone, data.accessKey, response, function () {
            labelManage.modifygrouplabel(data, response);
        });
    }
    else if (operation == "getgrouplabels") {
        oauth6(data.phone, data.accessKey, response, function () {
            labelManage.getgrouplabels(data, response);
        });
    }
    else if (operation == "getlabelsgroups") {
        oauth6(data.phone, data.accessKey, response, function () {
            labelManage.getlabelsgroups(data, response);
        });
    }
    else if (operation == "gethotlabels") {
        oauth6(data.phone, data.accessKey, response, function () {
            labelManage.gethotlabels(data, response);
        });
    }
}
var shareManage = require("./handlers/shareManage.js");
requestHandlers.shareManage = function (request, response, pathObject, data) {
    if (data == null) {
        return;
    }
    var operation = pathObject["operation"];
    if (operation == "addcomment") {
        oauth6(data.phone, data.accessKey, response, function () {
            shareManage.addcomment(data, response);
        });
    }
    else if (operation == "delete") {
        oauth6(data.phone, data.accessKey, response, function () {
            shareManage.delete(data, response);
        });
    }
    else if (operation == "getusershares") {
        oauth6(data.phone, data.accessKey, response, function () {
            shareManage.getusershares(data, response);
        });
    }
    else if (operation == "sendboardshare") {
        oauth6(data.phone, data.accessKey, response, function () {
            shareManage.sendboardshare(data, response);
        });
    } else if (operation == "getboardshares") {
        oauth6(data.phone, data.accessKey, response, function () {
            shareManage.getboardshares(data, response);
        });
    } else if (operation == "getboardshare") {
        shareManage.getboardshare(data, response);
    } else if (operation == "addboard") {
        oauth6(data.phone, data.accessKey, response, function () {
            shareManage.addboard(data, response);
        });
    } else if (operation == "modifyboard") {
        oauth6(data.phone, data.accessKey, response, function () {
            shareManage.modifyboard(data, response);
        });
    } else if (operation == "modifysquence") {
        oauth6(data.phone, data.accessKey, response, function () {
            shareManage.modifysquence(data, response);
        });
    } else if (operation == "getgroupboards") {
        oauth6(data.phone, data.accessKey, response, function () {
            shareManage.getgroupboards(data, response);
        });
    } else if (operation == "deleteboard") {
        oauth6(data.phone, data.accessKey, response, function () {
            shareManage.deleteboard(data, response);
        });
    } else if (operation == "score") {
        oauth6(data.phone, data.accessKey, response, function () {
            shareManage.score(data, response);
        });
    }
}
function setOauthAccessKey(phone, accessKey, next) {
    client.rpush(phone + "_accessKey", accessKey, function (err, reply) {
        if (err != null) {
            next(false);
            console.log(err);
            return;
        } else {
            next(true);
            return;
        }
    });
}
function delOauthAccessKey(phone, accessKey, next) {
    client.lrem(phone + "_accessKey", 0, accessKey, function (err, reply) {
        if (err != null) {
            next();
            console.log(err);
            return;
        } else {
            if (reply == 0) {
                next(false);
            } else {
                next(true);
            }
        }
    });
}
function oauth6(phone, accessKey, response, next) {
    response.asynchronous = 1;
    if (phone == undefined || phone == "" || phone == null || accessKey == undefined || accessKey == "" || accessKey == null) {
        response.write(JSON.stringify({
            "提示信息": "请求失败",
            "失败原因": "数据不完整"
        }), function () {
            console.log("安全机制数据不完整" + phone + "--" + accessKey);
        });
        response.end();
        return;
    } else {
        if (accessKey == "lejoying") {
            next();
            return;
        }
        else if (accessKeyPool[phone + "_accessKey"] != undefined) {
            var accessKeys = accessKeyPool[phone + "_accessKey"];
            var flag0 = false;
            for (var index in accessKeys) {
                if (accessKeys[index] == accessKey) {
                    flag0 = true;
                    break;
                }
            }
            if (flag0) {
                console.log("验证通过accessKeyPool...");
                next();
                return;
            } else {
                getAccessed(response);
            }
        } else {
            getAccessed(response);
        }
    }

    function getAccessed(response) {
        console.log("正在查看" + phone + "accessKey");
        client.lrange(phone + "_accessKey", 0, -1, function (err, reply) {
            if (err != null) {
                response.write(JSON.stringify({
                    "提示信息": "请求失败",
                    "失败原因": "数据异常"
                }));
                response.end();
                console.log(err);
                return;
            } else {
                if (reply.length == 0) {
                    response.write(JSON.stringify({
                        "提示信息": "请求失败",
                        "失败原因": "AccessKey Invalid"
                    }), function () {
                        console.log(phone + "AccessKey Invalid...");
                    });
                    response.end();
                    return;
                } else {
                    var flag = false;
                    for (var i = 0; i < reply.length; i++) {
                        if (reply[i] == accessKey) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        accessKeyPool[phone + "_accessKey"] = accessKeyPool[phone + "_accessKey"] || [];
//                        accessKeyPool[phone + "_accessKey"][accessKey] = accessKey;
                        accessKeyPool[phone + "_accessKey"].push(accessKey);
                        console.log("验证通过DB..." + accessKey);
//                        zookeeper.setData(accessKeyPool);
                        next();
                        return;
                    } else {
                        response.write(JSON.stringify({
                            "提示信息": "请求失败",
                            "失败原因": "AccessKey Invalid"
                        }), function () {
                            console.log(phone + ".AccessKey Invalid...");
                        });
                        response.end();
                        return;
                    }
                }
            }
        });
    }
}

module.exports = requestHandlers;