package com.example.bap.dao;

import com.example.bap.dto.RecordDto;
import com.example.bap.dto.SettingDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface RecordMapper {
    public void Return(RecordDto recordDto);
    public void AddRentalRecord(RecordDto recordDto);

    public List<RecordDto> MyRentalList(int accountId);
    public List<RecordDto> MyOverDueList(int accountId);

    public RecordDto MyRentalDTO(int accountId, int bookId);
    public RecordDto getRecentRecord(int accountId, int bookId);

    public List<Map<String, Object>> MyRecordList(int accountId);
    public List<Map<String, Object>> MyFilterRecordList(@Param("accountId") int accountId,
                                                        @Param("startDate") String startDate,
                                                        @Param("endDate") String endDate,
                                                        @Param("type") String type);

    public int MyRecordWithBookIdCount(int accountId, int bookId);
    public RecordDto MyRecordWithBookId(int accountId, int bookId);
}
