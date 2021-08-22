package com.turbo.seckill.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turbo.seckill.pojo.User;
import com.turbo.seckill.vo.ResponseBean;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Jusven
 * @Date 2021/7/16 20:45
 */
public class UserUtil {

    public static void main(String[] args) throws Exception {
        createUser(5000);
    }

    public static void createUser(int count) throws Exception {
        List<User> userList = new ArrayList<User>(count);
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setId(1300000000L + i);    //修改手机号验证正则表达式 ValidatorUtil
            user.setNickname("user" + i);
            user.setSalt("1a2b3c4d");
            user.setPassword(MD5Util.inputPassToDBPass("123456", user.getSalt()));
            user.setLoginCount(1);
            user.setRegisterDate(new Date());
            userList.add(user);
        }
        System.out.println("create user");
        Connection conn = getConn();
        String sql = "insert into t_user(id,nickname,password,salt,register_date,login_count) values(?,?,?,?,?,?)";
        PreparedStatement preSta = conn.prepareStatement(sql);
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            preSta.setLong(1, user.getId());
            preSta.setString(2, user.getNickname());
            preSta.setString(3, user.getPassword());
            preSta.setString(4, user.getSalt());
            preSta.setTimestamp(5, new Timestamp(user.getRegisterDate().getTime()));
            preSta.setInt(6, user.getLoginCount());
            preSta.addBatch();   //添加执行批
        }
        preSta.executeBatch();  //批量执行
        preSta.clearParameters();
        conn.close();
        System.out.println("insert into db");
        //登录，生成userTicket
        String urlString = "http://localhost:8080/login/doLogin";
        File file = new File("E:\\IdeaSpace\\seckill-demo\\config.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(0);
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection) url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream out = co.getOutputStream();
            String params = "mobile=" + user.getId() + "&password=" + MD5Util.inputPassToFormPass("123456");
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0, len);
            }
            inputStream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            ObjectMapper mapper = new ObjectMapper();
            ResponseBean responseBean = mapper.readValue(response, ResponseBean.class);
            String userTicket = (String) responseBean.getObj();
            System.out.println("create userTicket : " + user.getId());
            String row = user.getId() + "," +userTicket;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file" + user.getId());
        }
        raf.close();
        System.out.println("over");
    }

    public static Connection getConn() throws Exception {
//        String url = "jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String url = "jdbc:mysql://localhost:3306/seckill";
        String userName = "root";
        String password = "1234";
        String driver = "com.mysql.jdbc.Driver";
//        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        return DriverManager.getConnection(url,userName,password);
    }
}
