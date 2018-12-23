package com.ds.ccludemusic.jiami;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import org.jsoup.Jsoup;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



public class Jiami {

    public static String aesEncrypt(String content, String key1,String iv1) throws Exception {
        byte[] keyBytes=key1.getBytes();
        byte[] iv=iv1.getBytes();
        try{
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] result=cipher.doFinal(content.getBytes());
            return new String(Base64.encodeBase64(result),"UTF-8");
        }catch (Exception e) {
            System.out.println("exception:"+e.toString());
        }
        return null;

    }

    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String httpPost(String param) throws MalformedURLException, IOException{
        String fullurl = "https://music.163.com/weapi/song/enhance/player/url?csrf_token";
        //打开连接
        URLConnection conn =new URL(fullurl).openConnection();
        //设置通用的请求属性
        conn.setRequestProperty("Referer", "https://music.163.com/");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3554.0 Safari/537.36");
        //发送POST请求必须设置下面两行
        conn.setDoInput(true);
        conn.setDoOutput(true);
        param="params="+param+"&encSecKey=2d48fd9fb8e58bc9c1f14a7bda1b8e49a3520a67a2300a1f73766caee"
                + "29f2411c5350bceb15ed196ca963d6a6d0b61f3734f0a0f4a172ad853f16dd06018bc5ca8fb640eaa8decd1cd41f66e166cea7a3023bd63960e656ec97751cfc7ce08d943928e9db9b35400ff3d138bda1ab511a06fbee75585191cabe0e6e63f7350d6";

        OutputStream os = conn.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        dos.write(param.getBytes("utf-8"));
        dos.flush();
        os.close();

        StringBuffer sb = new StringBuffer();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn
                .getInputStream(), "utf-8"));
        String readLine = "";
        while ((readLine = in.readLine()) != null) {
            sb.append(readLine);
        }
        in.close();
        //解析json对象
        JSONObject jsStr = JSONObject.fromObject(sb.toString());
        JSONArray data = jsStr.getJSONArray("data");
        String s = data.getString(0);
        JSONObject data2 = JSONObject.fromObject(s);
        String url=data2.getString("url");
        url=url.replace("http", "https");
        return url;





    }




}
