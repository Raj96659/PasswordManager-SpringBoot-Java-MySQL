package com.passwordmanager.service;

import com.passwordmanager.dto.*;
import com.passwordmanager.util.PasswordGeneratorUtil;
import com.passwordmanager.util.PasswordStrengthUtil;
import org.springframework.stereotype.Service;

@Service
public class PasswordUtilityService {

    public PasswordGenerateResponse generatePassword(
            PasswordGenerateRequest request) {

        String password = PasswordGeneratorUtil.generatePassword(
                request.getLength(),
                request.isUseUpper(),
                request.isUseLower(),
                request.isUseNumbers(),
                request.isUseSpecial()
        );

        String strength =
                PasswordStrengthUtil.checkStrength(password);

        PasswordGenerateResponse response =
                new PasswordGenerateResponse();

        response.setGeneratedPassword(password);
        response.setStrength(strength);

        return response;
    }
}
