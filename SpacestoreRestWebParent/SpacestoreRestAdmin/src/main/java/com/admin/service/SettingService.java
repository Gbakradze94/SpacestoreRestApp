package com.admin.service;

import com.admin.util.settings.EmailSettingBag;
import com.spacestore.common.entity.setting.Setting;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SettingService {

    EmailSettingBag getEmailSettings();


}
