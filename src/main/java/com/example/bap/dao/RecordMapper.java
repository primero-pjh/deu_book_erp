package com.example.bap.dao;

import com.example.bap.dto.RecordDto;
import com.example.bap.dto.SettingDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecordMapper {
    public void RentalInsert(RecordDto recordDto);
    public List<RecordDto> MyRentalList(int accountId);
    public RecordDto MyRentalDTO(int accountId, int bookId);
    public List<RecordDto> MyRecordList(int accountId);
    public int MyRecordWithBookIdCount(int accountId, int bookId);
    public int MyOverDueListCount(int accountId);
    public List<RecordDto> MyOverDueList(int accountId);

    public void Return(RecordDto recordDto);
}
