package com.lishe.gateway;

import com.lishe.gateway.core.SocketServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.stream.Stream;

public class GatewayApp {


    public static void main(String[] args) throws IOException {
       SocketServer.startHttpServer();
    }


}
