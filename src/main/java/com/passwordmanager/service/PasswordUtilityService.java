package com.passwordmanager.service;

import com.passwordmanager.dto.*;
import com.passwordmanager.util.PasswordGeneratorUtil;
import com.passwordmanager.util.PasswordStrengthUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PasswordUtilityService {

    public List<PasswordGenerateResponse> generatePasswords(
            PasswordGenerateRequest request) {

        List<String> generated =
                PasswordGeneratorUtil.generatePasswords(
                        request.getLength(),
                        request.isUseUpper(),
                        request.isUseLower(),
                        request.isUseNumbers(),
                        request.isUseSpecial(),
                        request.isExcludeSimilar(),
                        request.getCount()
                );

        List<PasswordGenerateResponse> responses = new ArrayList<>();

        for (String password : generated) {

            PasswordGenerateResponse response =
                    new PasswordGenerateResponse();

            response.setGeneratedPassword(password);
            response.setStrength(
                    PasswordStrengthUtil.checkStrength(password)
            );

            responses.add(response);
        }

        return responses;
    }

}
