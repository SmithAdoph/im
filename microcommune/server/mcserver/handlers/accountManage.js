var accountManage = {};
var neo4j = require('neo4j');
var db = new neo4j.GraphDatabase(serverSetting.neo4jUrl);

accountManage.verifyphone = function(data, response){
    var phone = data.phone;
    var time = new Date().getTime().toString();
    console.log(phone+"--"+time.substr(time.length-4));
    var account = {
        phone: phone,
        code: time.substr(time.length-4),
        status: "init",
        time: new Date().getTime()
    };
    checkPhone();
    function checkPhone(){
        var query = [
            'MATCH account:Account',
            'WHERE account.phone! ={phone}',
            'RETURN account'
        ].join('\n');
        var params = {
            phone: phone
        };
        db.query(query, params, function (error, results){
            if(error){
                console.error(error);
                return;
            }else if(results.length == 0){
                createAccountNode();
            }else{
                var accountNode = results.pop().account;
                if(accountNode.data.status == "success"){
                    response.write(JSON.stringify({
                        "提示信息":"手机号验证失败",
                        "失败原因":"手机号已被注册"
                    }));
                    response.end();
                }else{
                    var time = new Date().getTime().toString();
                    accountNode.data.code = time.substr(time.length-4);
                    accountNode.save();
                    console.log(phone+"--"+time.substr(time.length-4));
                    response.write(JSON.stringify({
                        "提示信息":"手机号验证成功",
                        "phone": account.phone
                    }));
                    response.end();
                }
            }
        });
    }
    function createAccountNode() {
        var query = [
            'CREATE account:Account{account}',
            'SET account.uid=ID(account)',
            'RETURN  account'
        ].join('\n');

        var params = {
            account: account
        };

        db.query(query, params, function (error, results) {
            if (error) {
                console.error(error);
                return;
            } else {
                var accountNode = results.pop().account;
                response.write(JSON.stringify({
                    "提示信息":"手机号验证成功",
                    "phone": accountNode.data.phone
                }));
                response.end();
            }
        });
    }
}
accountManage.verifycode = function(data, response){
    console.log(data);
    var phone = data.phone;
    var code = data.code;

    checkPhoneCode();
    function checkPhoneCode(){
        var query = [
            'MATCH account:Account',
            'WHERE account.phone! ={phone} AND account! ={status}',
            'RETURN account'
        ].join('\n');
        var params = {
            phone: phone,
            status: "init"
        };
        db.query(query, params, function (error, results){
            if(error){
                console.error(error);
                return;
            }else{
                var accountNode = results.pop().account;
                if(account.data.code == code){
                    response.write(JSON.stringify({
                        "提示信息":"验证成功",
                        "phone":accountNode.data.phone
                    }));
                    response.end();
                }else{
                    response.write(JSON.stringify({
                        "提示信息":"验证失败",
                        "失败原因":"验证码不正确"
                    }));
                    response.end();
                }
            }
        });
    }
}
accountManage.verifypass = function(data, response){
    response.asynchronous = 1;
    var phone = data.phone;
    var password = data.password;
    checkPhone();
    function checkPhone(){
        var query = [
            'MATCH account:Account',
            'WHERE account.phone! ={phone}',
            'RETURN account'
        ].join('\n');
        var params = {
            phone: phone
        };
        db.query(query, params, function (error, results){
            if(error){
                response.write(JSON.stringify({
                    "提示信息":"注册失败",
                    "失败原因":"保存数据遇到错误"
                }));
                response.end();
            }else{
                var accountNode = results.pop().account;
                var accountData = accountNode.data;
                accountData.password = password;
                accountData.status = "success";
                accountNode.save();
                response.write(JSON.stringify({
                    "提示信息":"注册成功",
                    "account":accountData
                }));
                response.end();
            }
        });
    }

}
/***************************************
 *     URL：/api2/account/auth
 ***************************************/
accountManage.auth = function(data, response){
    response.asynchronous = 1;
    var phone = data.phone;
    var password = data.password;

    checkAccountNode();

    function checkAccountNode(){
        var query = [
            'MATCH account:Account',
            'WHERE account.phone! ={phone}',
            'RETURN  account'
        ].join('\n');

        var params = {
            phone: phone
        };
        db.query(query, params, function (error, results) {
            if (error) {
                console.error(error);
                return;
            } else if (results.length == 0) {
                response.write(JSON.stringify({
                    "提示信息": "账号登录失败",
                    "失败原因": "手机号不存在"
                }));
                response.end();
            } else {
                var accountNode = results.pop().account;
                if (accountNode.data.password == password) {
                    response.write(JSON.stringify({
                        "提示信息": "账号登录成功",
                        "account":accountNode.data
                    }));
                    response.end();
                } else {
                    response.write(JSON.stringify({
                        "提示信息": "账号登录失败",
                        "失败原因": "密码不正确"
                    }));
                    response.end();
                }
            }
        });
    }
}
accountManage.trash = function(data, response){

}

module.exports = accountManage;