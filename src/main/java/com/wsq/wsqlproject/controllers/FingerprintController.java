package com.wsq.wsqlproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wsq.wsqlproject.dto.FingerprintRequest;
import com.wsq.wsqlproject.dto.FingerprintResponse;
import com.wsq.wsqlproject.dto.ImageType;
import com.wsq.wsqlproject.service.FingerprintService;
import org.springframework.http.ResponseEntity;
import java.util.Base64;

@RestController
@RequestMapping("/api/fingerprint")
public class FingerprintController {
    @Autowired
    private FingerprintService fingerprintService;

    @PostMapping("/convert-to-wsq")
    public ResponseEntity<?> convertToWsq(@RequestBody(required = false) FingerprintRequest request) {
        String base64Image = request.getBase64Image();
        if (base64Image == null || base64Image.isEmpty()) {
            return ResponseEntity.badRequest().body("Bad Request: Missing base64 image");
        }
        try {

            FingerprintResponse response = fingerprintService.convertBase64ToWSQ(base64Image);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad Request: Invalid input");
        }
    }

    @PostMapping("/convert-wsq-to-base64")
    public ResponseEntity<?> convertWsqToBase64(
            @RequestBody(required = false) FingerprintRequest request) {
        byte[] wsqString = Base64.getDecoder().decode(request.getWsqString());

        String imageType = request.getImageType();
        if (wsqString == null || wsqString.length == 0) {
            return ResponseEntity.badRequest().body("Bad Request: Missing Wsq String");
        }

        if (imageType == null || imageType.isEmpty()) {
            return ResponseEntity.badRequest().body("Bad Request: Missing image Type ");
        }
        try {

            FingerprintResponse response = fingerprintService.convertWsqToBase64(wsqString, ImageType.valueOf(imageType));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bad Request: Invalid input");
        }
    }
}
