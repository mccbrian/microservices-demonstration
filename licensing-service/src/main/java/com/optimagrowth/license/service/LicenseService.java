package com.optimagrowth.license.service;

import com.optimagrowth.license.controller.LicenseController;
import com.optimagrowth.license.model.License;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Random;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class LicenseService {

    private final MessageSource message;
    private static final Random random = new Random();

    public LicenseService(@Qualifier("messageSource") MessageSource message) {
        this.message = message;
    }

    public License getLicense(String licensedId, String organizationId) {
        License license = License.builder()
                .id(random.nextInt(1000))
                .licenseId(licensedId)
                .organizationId(organizationId)
                .description("Software Product")
                .productName("Ostock")
                .licenseType("full")
                .build();

        displayRelatedLinks(license, organizationId);

        return license;
    }

    public String createLicense(License license, String organizationId, Locale locale) {
        String responseMessage = null;
        if (license != null) {
            license.setOrganizationId(organizationId);
            responseMessage = String
                    .format(message.getMessage("license.create.message", null, locale), license);
        }
        return responseMessage;
    }

    public String updateLicense(License license, String organizationId) {
        String responseMessage = null;
        if (license != null) {
            license.setOrganizationId(organizationId);
            responseMessage = String.format("License %s updated", license);
        }
        return responseMessage;
    }

    public String deleteLicense(String licenseId, String organizationId) {
        String responseMessage;
        responseMessage = String.format("Deleting license with id %s for the organization %s", licenseId, organizationId);
        return responseMessage;
    }

    private void displayRelatedLinks(@NotNull License license, String organizationId) {
        license.add(linkTo(methodOn(LicenseController.class)
                        .getLicense(organizationId, license.getLicenseId()))
                        .withSelfRel(),
                linkTo(methodOn(LicenseController.class)
                        .createLicense(organizationId, license, null))
                        .withRel("createLicense"),
                linkTo(methodOn(LicenseController.class)
                        .updateLicense(organizationId, license))
                        .withRel("updateLicense"),
                linkTo(methodOn(LicenseController.class)
                        .deleteLicense(organizationId, license.getLicenseId()))
                        .withRel("deleteLicense"));
    }

}
