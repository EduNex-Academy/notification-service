// src/main/java/com/edu/notification_service/service/CertificateService.java
package com.edu.notification_service.service;

import com.edu.notification_service.dto.CertificateRequest;
import com.edu.notification_service.domain.Certificate;
import org.springframework.stereotype.Service;

@Service
public class CertificateService {
    public Certificate generateCertificate(CertificateRequest request) {
        Certificate certificate = new Certificate();
        certificate.setUserId(request.getUserId());
        certificate.setCourseName(request.getCourseName());
        certificate.setIssueDate(java.time.LocalDate.now());
        certificate.setCertificateId(java.util.UUID.randomUUID().toString());
        return certificate;
    }
}
