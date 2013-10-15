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
            success: {"提示信息": "验证成功","phone":"XXX"},
            failed: {"提示信息": "验证失败","失败原因": ["验证码不正确" || "验证码超时"]}
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
            success: {"提示信息": "注册成功",account:{}},
            failed: {"提示信息": "注册失败", "失败原因": ["保存数据遇到错误"]}
        }
    },
    /***************************************
     *     URL：/api2/account/auth
     ***************************************/
        "account_auth": {
        request: {
            typical: {"phone": "XXX", "password": "XXX"}
        },
        response: {
            success: {"提示信息": "账号登录成功",account:{}},
            failed: {"提示信息": "账号登录失败", "失败原因": ["手机号不存在" || "密码不正确"]}
        }
    },
    /***************************************
     *     URL：/api2/account/exit
     ***************************************/
    "account_exit": {
        request: {
            typical: {}
        },
        response: {
            success: {"提示信息": "消息发送成功"},
            failed: {"提示信息": "消息发送失败", "失败原因": []}
        }
    }
}

/*************************************** ***************************************
 * *    Class：relation
 *************************************** ***************************************/

api = {
    /***************************************
     *     URL：/api2/relation/join
     ***************************************/
    "relation_join": {
        request: {
            typical: {"cid": "XXX","phone":"XXX"}
        },
        response: {
            success: {"提示信息": "加入成功"},
            failed: {"提示信息": "加入失败", "失败原因": ["数据异常"]}
        }
    },
    /***************************************
     *     URL：/api2/relation/addfriend
     ***************************************/
    "relation_addfriend": {
        request: {
            typical: {"phoneform": "XXX","phoneto":"XXX"}
        },
        response: {
            success: {"提示信息": "添加成功"},
            failed: {"提示信息": "添加失败", "失败原因": ["数据异常"]}
        }
    },
    /***************************************
     *     URL：/api2/relation/getfriends
     ***************************************/
    "relation_getfriends": {
        request: {
            typical: {"phone": "XXX"}
        },
        response: {
            success: {"提示信息": "获取好友成功",friends:[{},{},{}]},
            failed: {"提示信息": "获取好友失败", "失败原因": ["数据异常"]}
        }
    },
    /***************************************
     *     URL：/api2/relation/getcommunities
     ***************************************/
    "relation_getcommunities": {
        request: {
            typical: {"phone": "XXX"}
        },
        response: {
            success: {"提示信息": "获取社区成功",communities:[{},{},{}]},
            failed: {"提示信息": "获取社区失败", "失败原因": ["数据异常"]}
        }
    }
}
/*************************************** ***************************************
 * *    Class：commounity
 *************************************** ***************************************/

api = {
    /***************************************
     *     URL：/api2/community/find
     ***************************************/
    "community_find": {
        request: {
            typical: {longitude:"XXX",latitude:"XXX"}
        },
        response: {
            success: {"提示信息": "获取成功",community:{}},
            failed: {"提示信息": "获取失败", "失败原因": ["社区不存在"]}
        }
    },
    /***************************************
     *     URL：/api2/community/finddefault
     ***************************************/
    "community_finddefault": {
        request: {
            typical: {}
        },
        response: {
            success: {"提示信息": "获取成功",community:{}},
            failed: {"提示信息": "获取失败", "失败原因": ["社区不存在"]}
        }
    }
}

