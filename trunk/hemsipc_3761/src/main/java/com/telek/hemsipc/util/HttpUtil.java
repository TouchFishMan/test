package com.telek.hemsipc.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * @Auther: wll
 * @Date: 2018/9/18 12:05
 * @Description:
 */
public class HttpUtil {
    /**
     * 从网络Url中下载文件
     *
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public static void downLoadFromUrl(String url, String savePath, String fileName, String downloadKey) throws IOException {
        // 生成一个httpclient对象
        //CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        StringEntity stringEntity = new StringEntity("downloadKey=" + downloadKey);
        stringEntity.setContentType("application/x-www-form-urlencoded");
        httpPost.setEntity(stringEntity);
        HttpResponse response = httpclient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        InputStream in = entity.getContent();
        //文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        File file = new File(saveDir + File.separator + fileName);
        try {
            FileOutputStream fout = new FileOutputStream(file);
            byte[] tmp = new byte[1024];
            int ch = 0;
            while ((ch = in.read(tmp)) != -1) {
                fout.write(tmp, 0, ch);
            }
            fout.flush();
            fout.close();
        } finally {
            // 关闭低层流。
            in.close();
        }
        //httpclient.close();
    }


    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
