package com.xiaomi.videonews.bombapi.result;

/**
 * 用户登陆和注册时的数据结果
 *
 * 作者：yuanchao on 2016/8/18 0018 17:18
 * 邮箱：yuanchao@feicuiedu.com
 */
public class UserResult {

    private String objectId;

    private String sessionToken;

    public String getObjectId() {
        return objectId;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    //    {
//              "username": username,               // 登录用户名称
//            "createdAt": YYYY-mm-dd HH:ii:ss,   // 用户创建时间
//            "updatedAt": YYYY-mm-dd HH:ii:ss,   // 用户更新时间
//            "objectId": objectId,               // 用户唯一Id
//            "sessionToken": sessionToekn        // 用来认证更新或删除用户的请求
//    }

//    {
//        "createdAt": YYYY-mm-dd HH:ii:ss,    // 用户注册时间
//            "objectId": objectId,                // 用户唯一Id
//            "sessionToken": sessionToken         // 用来认证更新或删除用户的请求
//    }
}
