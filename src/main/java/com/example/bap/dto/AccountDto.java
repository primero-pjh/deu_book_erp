package com.example.bap.dto;

public class AccountDto {
    private int accountId;
    private String userId;
    private String password;
    private String phoneNumber;
    private String telegramId;
    private int isAdmin;
    private int isReceiveMessage;

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public String getUserId() { return userId;
    }
    public void setUserId(String userId) { this.userId = userId;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getTelegramId() { return telegramId; }
    public void setTelegramId(String telegramId) { this.telegramId = telegramId; }

    public int getIsAdmin() { return isAdmin; }
    public void setIsAdmin(int isAdmin) { this.isAdmin = isAdmin; }

    public int getIsReceiveMessage() { return isReceiveMessage; }
    public void setIsReceiveMessage(int isReceiveMessage) { this.isReceiveMessage = isReceiveMessage; }
}