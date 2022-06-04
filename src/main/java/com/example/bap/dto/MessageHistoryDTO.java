package com.example.bap.dto;

public class MessageHistoryDTO {
    private int messageHistoryId;
    private int recordId;
    private String sendDate;
    private String sendTime;
    private String content;

    public int getMessageHistoryId() { return messageHistoryId; }
    public void setMessageHistoryId(int messageHistoryId) { this.messageHistoryId = messageHistoryId; }
    public int getRecordId() { return recordId; }
    public void setRecordId(int recordId) { this.recordId = recordId; }
    public String getSendDate() { return sendDate; }
    public void setSendDate(String sendDate) { this.sendDate = sendDate; }
    public String getSendTime() { return sendTime; }
    public void setSendTime(String sendTime) { this.sendTime = sendTime; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}