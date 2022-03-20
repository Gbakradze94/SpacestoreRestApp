package com.admin.service.serviceimpl;

import com.admin.repository.SettingRepository;
import com.admin.service.SettingService;
import com.admin.util.settings.EmailSettingBag;
import com.spacestore.common.entity.setting.Setting;
import com.spacestore.common.entity.setting.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingServiceImpl implements SettingService {

    @Autowired
    private SettingRepository settingRepository;


    @Override
    public EmailSettingBag getEmailSettings() {
        List<Setting> settings = settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
        settings.addAll(settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES));

        return new EmailSettingBag(settings);
    }
}
