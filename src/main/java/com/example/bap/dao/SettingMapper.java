package com.example.bap.dao;

import com.example.bap.dto.SettingDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SettingMapper {
    public List<SettingDto> SettingReadAll();
    public SettingDto SettingReadOne(int settingId);
    public SettingDto getSpKeyword(String keyword);
    public void SettingSave(SettingDto settingDto);
}
