//package com.turbo.seckill.test;
//
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//
///**
// * @Author Jusven
// * @Date 2021/10/13 20:57
// */
//public class test1 {
//    public static void main(String[] args) {
//        testImg();
//    }
//    /**
//     *
//     */
////    @RequestMapping(value = LIST + "/testImg",method= RequestMethod.POST)
////    @ResponseBody
//    public static String testImg(String imgByte) throws FileNotFoundException {
//        String path = "F://imgSave";
//        File dir = new File(path);
//
//        imgByte = "图片16进制代码";
//
//        //检查目录
//        if(!dir.isDirectory()){
//            throw new FileNotFoundException();
//        }
//        //检查目录写权限
//        if(!dir.canWrite()){
////            throw new FIle("上传目录没有写权限");
//            System.out.println("上传目录没有写权限");
//        }
//
//        path = "F://imgSave//test.jpg"; //path到下面方法调用的时候需要有带记录下来的文件名
//
//        try {
//            HexToImage.toImage(imgByte, path);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            System.out.println("十六进制转换图片报错");
//            e.printStackTrace();
//        }
//
//        return "1";
//    }
//}
