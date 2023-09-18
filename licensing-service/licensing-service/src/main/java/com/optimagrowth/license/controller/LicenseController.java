package com.optimagrowth.license.controller;

import com.optimagrowth.license.model.License;
import com.optimagrowth.license.service.LicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1/organization/{organizationId}/license")
public class LicenseController {

    private final LicenseService service;

    @PostMapping()
    public ResponseEntity<String> createLicense(@PathVariable String organizationId, @RequestBody License request,
                                                @RequestHeader (value = "Accept-Language", required = false) Locale locale) {
        return new ResponseEntity<>(service.createLicense(request, organizationId, locale), HttpStatus.CREATED);
    }

    @GetMapping("/{licenseId}")
    @ResponseStatus(HttpStatus.OK)
    public License getLicense(@PathVariable String organizationId, @PathVariable String licenseId) {
        License license = service.getLicense(licenseId, organizationId);
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
        return license;
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> updateLicense(@PathVariable String organizationId, @RequestBody License request) {
        return ResponseEntity.ok(service.updateLicense(request, organizationId));
    }

    @DeleteMapping(value = "/{licenseId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteLicense(@PathVariable String organizationId, @PathVariable String licenseId) {
        return ResponseEntity.ok(service.deleteLicense(licenseId, organizationId));
    }

}
