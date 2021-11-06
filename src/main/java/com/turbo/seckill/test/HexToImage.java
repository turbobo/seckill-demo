package com.turbo.seckill.test;
 
import java.io.File;
import java.io.FileOutputStream;
 
public class HexToImage {
    public static void toImage(String image, String path) throws Exception {  
    	
       HexToImage to=new HexToImage(); 
       /*InputStream is=new FileInputStream("f://cc.txt");  
       InputStreamReader isr = new InputStreamReader(is);  
       BufferedReader br = new BufferedReader(isr);  
       String str = null;  
       StringBuilder sb = new StringBuilder();  
       while ((str = br.readLine()) != null) {  
           //System.out.println(str);  
           sb.append(str);  
       }
       //str.replaceAll(" ", "");
       //String aaa = sb.toString();
       //aaa.replaceAll(" ", "");
       System.out.println(sb.toString().replaceAll(" ", ""));  
       //System.out.println(str);  
       to.saveToImgFile(sb.toString().replaceAll(" ", "").replace("<", "").replace(">", "").toUpperCase(),path);*/
       if (image != null) {  
    	   to.saveToImgFile(image.replaceAll(" ", "").replace("<", "").replace(">", "").toUpperCase(), path);  
        }
    }
    
    public void saveToImgFile(String src,String output){  
           if(src==null||src.length()==0){  
               return;  
           }  
           try{  
               FileOutputStream out = new FileOutputStream(new File(output));  
               byte[] bytes = src.getBytes();  
               for(int i=0;i<bytes.length;i+=2){  
                   out.write(charToInt(bytes[i])*16+charToInt(bytes[i+1]));  
               }  
               out.close();  
           }catch(Exception e){  
               e.printStackTrace();  
           }  
       }  
       private int charToInt(byte ch){  
           int val = 0;  
           if(ch>=0x30&&ch<=0x39){  
               val=ch-0x30;  
           }else if(ch>=0x41&&ch<=0x46){  
               val=ch-0x41+10;  
           }  
           return val;  
       }  
}