package com.univalle.bubackend.services.setting;

import com.univalle.bubackend.DTOs.setting.SettingRequest;
import com.univalle.bubackend.DTOs.setting.SettingResponse;
import com.univalle.bubackend.exceptions.SettingNotFound;
import com.univalle.bubackend.models.Setting;
import com.univalle.bubackend.repository.SettingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class SettingServiceImpl implements ISettingService {

    private SettingRepository settingRepository;

    @Override
    public SettingResponse createSetting(SettingRequest settingRequest) {
        Setting setting = Setting.builder()
                .startSemester(settingRequest.startSemester())
                .endBeneficiaryLunch(settingRequest.endBeneficiaryLunch())
                .endBeneficiarySnack(settingRequest.endBeneficiarySnack())
                .endLunch(settingRequest.endLunch())
                .endSemester(settingRequest.endSemester())
                .endSnack(settingRequest.endSnack())
                .numLunch(settingRequest.numLunch())
                .numSnack(settingRequest.numSnack())
                .starBeneficiarySnack(settingRequest.starBeneficiarySnack())
                .starBeneficiaryLunch(settingRequest.starBeneficiaryLunch())
                .starLunch(settingRequest.starLunch())
                .starSnack(settingRequest.starSnack())
                .build();

        settingRepository.save(setting);
        return new SettingResponse(setting.getId(), "Ajustes creados exitosamente", settingRequest);
    }

    @Override
    public SettingResponse getSetting(Integer id) {
        Setting setting = settingRepository.findSettingById(id)
                .orElseThrow(() -> new SettingNotFound("Ajuste no encontrado"));

        SettingRequest settingRequest = new SettingRequest(
                setting.getId(),
                setting.getStartSemester(),
                setting.getEndSemester(),
                setting.getNumLunch(),
                setting.getNumSnack(),
                setting.getStarBeneficiaryLunch(),
                setting.getEndBeneficiaryLunch(),
                setting.getStarLunch(),
                setting.getEndLunch(),
                setting.getStarBeneficiarySnack(),
                setting.getEndBeneficiarySnack(),
                setting.getStarSnack(),
                setting.getEndSnack()
        );

        return new SettingResponse(setting.getId(), "Ajuste encontrado exitosamente", settingRequest);

    }

    @Override
    public SettingResponse editSetting(SettingRequest settingRequest) {
        Setting setting = settingRepository.findSettingById(settingRequest.id())
                .orElseThrow(() -> new SettingNotFound("Ajuste no encontrado"));

            setting.setStartSemester(settingRequest.startSemester());
            setting.setEndSemester(settingRequest.endSemester());
            setting.setNumLunch(settingRequest.numLunch());
            setting.setNumSnack(settingRequest.numSnack());
            setting.setStarBeneficiaryLunch(settingRequest.starBeneficiaryLunch());
            setting.setEndBeneficiaryLunch(settingRequest.endBeneficiaryLunch());
            setting.setStarLunch(settingRequest.starLunch());
            setting.setEndLunch(settingRequest.endLunch());
            setting.setStarBeneficiarySnack(settingRequest.starBeneficiarySnack());
            setting.setEndBeneficiarySnack(settingRequest.endBeneficiarySnack());
            setting.setStarSnack(settingRequest.starSnack());
            setting.setEndSnack(settingRequest.endSnack());

            settingRepository.save(setting);

            return new SettingResponse(setting.getId(), "Ajustes actualizados exitosamente", settingRequest);
        }
}
