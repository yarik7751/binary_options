package com.elatesoftware.grandcapital.models;

public class User {

    private String login;
    private String token;
    private String serverName;
    private String userName = "";
    private double balance = 0;

    private static User ourInstance = null;

    public static User getInstance() {
        return ourInstance;
    }

    public static void setInstance(User user) {
        ourInstance = user;
    }

    private User() {
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public User(String login, String serverName, String token) {
        this.login = login;
        this.serverName = serverName;
        this.token = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", token='" + token + '\'' +
                ", serverName='" + serverName + '\'' +
                '}';
    }
}
