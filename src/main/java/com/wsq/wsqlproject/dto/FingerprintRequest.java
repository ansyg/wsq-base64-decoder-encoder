package com.wsq.wsqlproject.dto;

import lombok.Data;

@Data
public class FingerprintRequest {
    private String base64Image;
    private String wsqString;
    private String imageType;
}
