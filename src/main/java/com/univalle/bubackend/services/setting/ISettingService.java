package com.univalle.bubackend.services.setting;

import com.univalle.bubackend.DTOs.setting.SettingRequest;
import com.univalle.bubackend.DTOs.setting.SettingResponse;

public interface ISettingService {
    SettingResponse createSetting(SettingRequest settingRequest);
    SettingResponse getSetting(Integer id);
    SettingResponse editSetting(SettingRequest settingRequest);
}
