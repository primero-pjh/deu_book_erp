package com.example.bap.dto;

public class RecordDto {
    private int recordId;
    private int bookId;
    private int accountId;
    private String rentalDate;
    private String returnDate;
    private String returnDueDate;
    private int overDue;

    public int getRecordId() { return recordId; }
    public void setRecordId(int recordId) { this.recordId = recordId; }
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }
    public String getRentalDate() { return rentalDate; }
    public void setRentalDate(String rentalDate) { this.rentalDate = rentalDate; }
    public String getReturnDate() { return returnDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }
    public String getReturnDueDate() { return returnDueDate; }
    public void setReturnDueDate(String returnDueDate) { this.returnDueDate = returnDueDate; }
    public int getOverDue() { return overDue; }
    public void setOverDue(int overDue) { this.overDue = overDue; }
}