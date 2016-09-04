package edu.asu.msse.smurthy3.assign9test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Copyright (c) 2016 Shweta Murthy,
 * You may not use this file except for self-evaluation and practice
 * This file is allowed to be used for grading puroposes
 * through the spring semester 2016, ASU, by  the grader, TA and the instructor
 * Unless agreed to in writing, this material can is to be
 * distributed on an "AS IS" BASIS
 *
 * @author Shweta Murthy mailTo: smurthy3@asu.edu
 * @version 2/29/16
 */

public class JsonRPCRequestViaHttp {

    private final Map<String, String> headers;
    private URL url;
    private String requestData;
    private getFromServer parent;

    public JsonRPCRequestViaHttp(URL url, getFromServer parent) {
        this.url = url;
        this.parent = parent;
        this.headers = new HashMap<String, String>();
    }

    public void setHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public String call(String requestData) throws Exception {
        android.util.Log.d(this.getClass().getSimpleName(),"in call, url: "+url.toString()+" requestData: "+requestData);
        android.util.Log.d(this.getClass().getSimpleName(),"in call headers:"+headers.keySet().toString());
        String respData = post(url, headers, requestData);
        return respData;
    }

    private String post(URL url, Map<String, String> headers, String data) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        this.requestData = data;
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        connection.addRequestProperty("Accept-Encoding", "gzip");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        android.util.Log.d(this.getClass().getSimpleName(),"Connecting...");
        connection.connect();
        OutputStream out = null;
        try {
            out = connection.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            out.close();
            int statusCode = connection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                throw new Exception(
                        "Unexpected status from post: " + statusCode);
            }
        } finally {
            if (out != null) {
                out.close();
            }
        }
        String responseEncoding = connection.getHeaderField("Content-Encoding");
        responseEncoding = (responseEncoding == null ? "" : responseEncoding.trim());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream in = connection.getInputStream();
        try {
            in = connection.getInputStream();
            if ("gzip".equalsIgnoreCase(responseEncoding)) {
                in = new GZIPInputStream(in);
            }
            in = new BufferedInputStream(in);
            byte[] buff = new byte[1024];
            int n;
            while ((n = in.read(buff)) > 0) {
                bos.write(buff, 0, n);
            }
            bos.flush();
            bos.close();
        } finally {
            if (in != null) {
                in.close();
            }
        }
        android.util.Log.d(this.getClass().getSimpleName(),"json rpc request via http returned string "+bos.toString());
        return bos.toString();
    }
}
