/*************************************** ***************************************
 * *    Class：session
 *************************************** ***************************************/

api = {
    /***************************************
     *     URL：/api2/session/sendmessage
     ***************************************/
    "message_send": {
        request: {
            typical: {"uid": "XXX", "userlist": [1,2,3], "message": {"content":"xxx","time":"123123123"}}
        },
        response: {
            success: {"提示信息": "消息发送成功"},
            failed: {"提示信息": "消息发送失败", "失败原因": []}
        }
    },
    /***************************************
     *     URL：/api2/session/getmessages
     ***************************************/
    "message_get": {
        request: {
            typical: {"uid": "XXX","sessionID":"XXX"}
        },
        response: {
            success: {"提示信息": "消息获取成功", "messages":[{"content":"xxx","time":"123123123"},{"content":"xxx","time":"123123123"}]},
            failed: {"提示信息": "消息获取失败", "失败原因": []}}
        }
    }

/*************************************** ***************************************
 * *    Class：account
 *************************************** ***************************************/

api = {
    /***************************************
     *     URL：/api2/account/verifyphone
     ***************************************/
    "account_verifyphone": {
        request: {
            typical: {"phone": "XXX"}
        },
        response: {
            success: {"提示信息": "手机号验证成功" , "phone":"XXX"},
            failed: {"提示信息": "手机号验证失败", "失败原因": ["手机号不正确","手机号已被注册"]}
        }
    },
    /***************************************
     *     URL：/api2/account/verifycode
     ***************************************/
    "account_verifycode": {
        request: {
            typical: {"phone":"XXX","code":"XXX"}
        },
        response: {
            success: {"提示信息": "验证码正确","phone":"XXX"},
            failed: {"提示信息": "验证码错误","失败原因": []}
        }
    },
    /***************************************
     *     URL：/api2/account/verifypass
     ***************************************/
    "account_verifypass": {
        request: {
            typical: {"phone": "XXX", "password":"XXX"}
        },
        response: {
            success: {"提示信息": "注册成功"},
            failed: {"提示信息": "注册失败", "失败原因": []}
        }
    },
    /***************************************
     *     URL：/api2/account/auth
     ***************************************/
    "account_auth": {
        request: {
            typical: {"phone": "XXX", "password": "XXX", "message": {"content":"xxx","time":"123123123"}}
        },
        response: {
            success: {"提示信息": "消息发送成功"},
            failed: {"提示信息": "消息发送失败", "失败原因": []}
        }
    },
    /***************************************
     *     URL：/api2/account/trash
     ***************************************/
    "account_trash": {
        request: {
            typical: {"uid": "XXX", "userlist": [1,2,3], "message": {"content":"xxx","time":"123123123"}}
        },
        response: {
            success: {"提示信息": "消息发送成功"},
            failed: {"提示信息": "消息发送失败", "失败原因": []}
        }
    }
}

/*************************************** ***************************************
 * *    Class：paccount
 *************************************** ***************************************/

api = {

}
