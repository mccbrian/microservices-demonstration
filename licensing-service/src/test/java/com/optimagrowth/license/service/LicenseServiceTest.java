package com.optimagrowth.license.service;

import com.optimagrowth.license.model.License;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("The Licence Service Should")
class LicenseServiceTest {

    @InjectMocks
    LicenseService service;

    @Mock
    MessageSource messageSource;

    private License license;

    @BeforeEach
    public void setUp() {
        license = License.builder()
                .id(100)
                .licenseId("321")
                .organizationId("123")
                .description("Software Product")
                .productName("product name")
                .licenseType("full")
                .build();
    }

    @Test
    void returnTheAssociatedLicenseWhenGivenTheLicenseAndOrganizationId() {
        License license = service.getLicense("123", "321");
        Assertions.assertEquals("123", license.getLicenseId());
    }

    @Test
    void returnAMessageWhenALicenseIsSuccessfullyCreated() {
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("License created %s");
        String result = service.createLicense("321", license, new Locale("en", "US", "message"));
        assertFalse(Strings.isEmpty(result));
    }

    @Test
    void returnAnEmptyMessageWhenTheLicenseToCreateIsNull() {
        String result = service.createLicense("321", null, new Locale("en", "US", "message"));
        assertTrue(Strings.isEmpty(result));
    }

    @Test
    void returnAMessageWhenALicenseIsSuccessfullyUpdated() {
        String result = service.updateLicense("321", license);
        assertFalse(Strings.isEmpty(result));
    }

    @Test
    void returnAnEmptyMessageWhenTheLicenseToUpdateIsNull() {
        String result = service.updateLicense("321", null);
        assertTrue(Strings.isEmpty(result));
    }

    @Test
    void returnAMessageWhenALicenseIsDeleted() {
        String message = service.deleteLicense("123", "321");
        assertFalse(Strings.isEmpty(message));
    }
}