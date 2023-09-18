package com.optimagrowth.license.service;

import com.optimagrowth.license.model.License;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Random;

@Service
public class LicenseService {

    private final MessageSource message;
    private static final Random random = new Random();

    public LicenseService(@Qualifier("messageSource") MessageSource message) {
        this.message = message;
    }

    public License getLicense(String licensedId, String organizationId) {
        return License.builder()
                .id(random.nextInt(1000))
                .licenseId(licensedId)
                .organizationId(organizationId)
                .description("Software Product")
                .productName("Ostock")
                .licenseType("full")
                .build();
    }

    public String createLicense(License license, String organizationId, Locale locale) {
        String responseMessage = null;
        if (license != null) {
            license.setOrganizationId(organizationId);
            responseMessage = String
                    .format(message.getMessage("license.create.message", null, locale), license.toString());
        }
        return responseMessage;
    }

    public String updateLicense(License license, String organizationId) {
        String responseMessage = null;
        if (license != null) {
            license.setOrganizationId(organizationId);
            responseMessage = String.format("License %s updated", license.toString());
        }
        return responseMessage;
    }

    public String deleteLicense(String licenseId, String organizationId) {
        String responseMessage;
        responseMessage = String.format("Deleting license with id %s for the organization %s", licenseId, organizationId);
        return responseMessage;
    }

}
