package com.ds.ccludemusic.controller;

import com.ds.ccludemusic.jiami.Jiami;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/userCtrl")
public class UserCtrl {


    @RequestMapping("/getid")
    public void getid(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String musicid=req.getParameter("musicID");
        String param=Jiami.aesEncrypt("{\"ids\":\"["+musicid+"]\",\"br\":128000,\"csrf_token\":\"\"}", "0CoJUm6Qyw8W8jud", "0102030405060708");
        param = Jiami.aesEncrypt(param, "a8LWv2uAtXjzSfkQ","0102030405060708");
        String urlencode=Jiami.getURLEncoderString(param);
        String url=Jiami.httpPost(urlencode);
        Map map=new HashMap();
        map.put("code",1);
        map.put("Uurl",url);
        String json= JSONArray.fromObject(map).toString();
        PrintWriter out=resp.getWriter();
        out.write(json);
    }

    @RequestMapping("/downmp3")
    public void downMp3(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String musicid=req.getParameter("musicID");
        musicid = musicid.replace(" +","");
        String param=Jiami.aesEncrypt("{\"ids\":\"["+musicid+"]\",\"br\":128000,\"csrf_token\":\"\"}", "0CoJUm6Qyw8W8jud", "0102030405060708");
        param = Jiami.aesEncrypt(param, "a8LWv2uAtXjzSfkQ","0102030405060708");
        String urlencode=Jiami.getURLEncoderString(param);
        String urll=Jiami.httpPost(urlencode);

        URL url = new URL(urll);
        HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
        urlCon.setConnectTimeout(6000);
        urlCon.setReadTimeout(6000);
        int code = urlCon.getResponseCode();
        if (code != HttpURLConnection.HTTP_OK) {
            throw new Exception("文件读取失败");
        }

        resp.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(musicid+".mp3", "utf-8"));
        resp.setContentType("application/octet-stream");
//        resp.setHeader("Accept-Ranges", "bytes");
//        resp.setCharacterEncoding("UTF-8");
//        resp.setHeader("Access-Control-Allow-Origin", "*");

        DataInputStream in = new DataInputStream(urlCon.getInputStream());
        ServletOutputStream out = resp.getOutputStream();
        byte[] buffer = new byte[2048];
        int count = 0;
        while ((count = in.read(buffer)) > 0) {
            out.write(buffer, 0, count);
        }
        out.close();
        in.close();
    }
}
