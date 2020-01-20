package com.lishe.gateway.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpTask implements Runnable {
    private Socket socket;

    public HttpTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            throw new IllegalArgumentException("socket can't be null.");
        }

        try {
            InputStream inputStream = socket.getInputStream();
            Request httpRequest = HttpMessageParser.parse2request(inputStream);
            System.out.println(httpRequest);

            OutputStream os = socket.getOutputStream();
            PrintWriter out = new PrintWriter(os);

            // 根据请求结果进行响应，省略返回
            String result = "{\"a\":\"1\"}";
            Response response = new Response();
            response.setCode(200);
            response.setContent(result);
            response.setStatus("ok");
            Map<String, String> header = new HashMap<>(8);
            header.put("Content-Type", "application/json");
            response.setHeaders(header);
            String httpRes = HttpMessageParser.buildResponse(response);
            out.print(httpRes);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}