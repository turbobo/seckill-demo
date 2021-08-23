package com.turbo.seckill.config;

import com.turbo.seckill.pojo.User;

/**
 * @Author Jusven
 * @Date 2021/8/22 21:38
 * 获取用户线程 --  线程绑定用户，当前登录用户存在自己的线程中，用户值只有当前线程才能看得到
 * ThreadLocal --  专门存放当前线程私有数据的盒子
 */
public class UserContext {
    private static ThreadLocal<User> userHolder = new ThreadLocal<User>();



    public static User getUser() {
        return userHolder.get();
    }

    public static void setUser(User user) {
        userHolder.set(user);
    }
}
