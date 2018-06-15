package com.briup.environment.server;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

public class ServerImpl extends Thread implements Server,ConfigurationAware {
    private ServerSocket serverSocket;
    private Socket socket;
    private InputStream is = null;
    private BufferedInputStream bis = null;
    private ObjectInputStream ois = null;
    private static Collection<Environment> collection;
    private LogImpl log;
    private BackUpImpl back;
    private static Configuration configuration;
    private int port;
    private DBStore dbstore;

    public ServerImpl(Socket socket) {
            this.socket = socket;
    }

    public ServerImpl() {

    }
    public ServerImpl(int port){
        this.port = port;
        try{
            dbstore=configuration.getDBStore();
            log=(LogImpl)configuration.getLogger();
            back=(BackUpImpl)configuration.getBackUp();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            log.info("Got information");
            //System.out.println("Got information!");
            is = socket.getInputStream();
            ois = new ObjectInputStream(is);
            collection = (Collection<Environment>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            shutdown();
        }
    }

    @Override
    public Collection<Environment> reciver() throws Exception {
        log.info("Waiting...");
        //System.out.println("Waiting...");
        serverSocket = new ServerSocket(port);
        socket = serverSocket.accept();
        this.start();
        Thread.sleep(5000);
        return collection;

    }

    @Override
    public void shutdown() {

        try {
            if (socket != null) socket.close();
            if (is != null) is.close();
            if (ois != null) ois.close();
            if (bis != null) bis.close();
            log.error("Exception happened!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration=configuration;
        try{
            dbstore=configuration.getDBStore();
            log=(LogImpl)configuration.getLogger();
            back=(BackUpImpl)configuration.getBackUp();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void init(Properties properties) throws Exception {
        port=Integer.parseInt(properties.getProperty("port"));
    }

    public static void main(String[] args) throws Exception {
        ConfigurationImpl configuration = new ConfigurationImpl();
        Collection<Environment> reciver = configuration.getServer().reciver();
        configuration.getDBStore().saveDB(reciver);
    }
}
