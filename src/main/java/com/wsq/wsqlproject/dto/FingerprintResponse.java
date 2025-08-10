package com.wsq.wsqlproject.dto;

import lombok.Data;

@Data
public class FingerprintResponse {
    public String originalBase64;
    public String wsqBase64;
    public String error;

}
