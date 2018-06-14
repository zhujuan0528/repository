package com.briup.environment.gui;


public interface User {
    boolean login(String username,String pwd);
    boolean searchByName(String username);
    boolean register(String username,String pwd);
}