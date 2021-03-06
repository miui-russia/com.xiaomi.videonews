package com.xiaomi.videonews.bombapi.other;


import com.xiaomi.videonews.bombapi.BombConst;

/**
 * 指向用户表的Pointer
 *
 * 作者：yuanchao on 2016/8/18 0018 14:47
 * 邮箱：yuanchao@feicuiedu.com
 */
public class UserPointer extends Pointer {
    private String username;

    public UserPointer(String objectId) {
        super(BombConst.TABLE_USER, objectId);
    }
//
//    "createdAt": "2016-07-11 12:20:07",
//    "updatedAt": "2016-07-11 12:20:09",
//    "username": "飞翔的猪头"

    public String getUsername() {
        return username;
    }
}
