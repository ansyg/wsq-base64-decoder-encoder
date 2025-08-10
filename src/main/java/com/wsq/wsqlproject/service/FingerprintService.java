package com.wsq.wsqlproject.service;

import org.springframework.stereotype.Service;

import com.wsq.wsqlproject.dto.FingerprintResponse;
import com.wsq.wsqlproject.dto.ImageType;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.awt.color.ColorSpace;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class FingerprintService {

        public FingerprintResponse convertBase64ToWSQ(String base64Image) {
                FingerprintResponse result = new FingerprintResponse();
                result.originalBase64 = base64Image;

                try {
                        // Validate input
                        if (base64Image == null || base64Image.isEmpty()) {
                                throw new IllegalArgumentException("Base64 string is empty");
                        }

                        // Decode Base64
                        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
                        if (bufferedImage == null) {
                                throw new IOException("Unsupported image format");
                        }

                        // Convert to grayscale
                        BufferedImage grayImage = convertToGrayscale(bufferedImage);

                        // Create Bitmap for WSQ
                        Bitmap bitmap = new Bitmap(
                                        ((DataBufferByte) grayImage.getRaster().getDataBuffer()).getData(),
                                        grayImage.getWidth(),
                                        grayImage.getHeight(),
                                        500, // DPI (adjust as needed)
                                        8, // Bit depth
                                        1 // Bands (grayscale)
                        );

                        // Encode to WSQ
                        ByteArrayOutputStream wsqOutput = new ByteArrayOutputStream();
                        WSQEncoder.encode(wsqOutput, bitmap, 2.25f); // Bitrate (0.75-2.25)
                        result.wsqBase64 = Base64.getEncoder().encodeToString(wsqOutput.toByteArray());

                } catch (Exception e) {
                        result.error = "Conversion failed: " + e.getMessage();
                }

                return result;
        }

        // Converts WSQ bytes → PNG/JPEG in Base64
        public FingerprintResponse convertWsqToBase64(byte[] wsqBytes, ImageType imageType) {
                FingerprintResponse result = new FingerprintResponse();
                try {
                        // Store original WSQ as Base64
                        result.setWsqBase64(Base64.getEncoder().encodeToString(wsqBytes));

                        // Decode WSQ → BufferedImage
                        ByteArrayInputStream wsqInput = new ByteArrayInputStream(wsqBytes);
                        BitmapWithMetadata bitmap = WSQDecoder.decode(wsqInput);

                        // Convert to BufferedImage
                        BufferedImage image = new BufferedImage(
                                        bitmap.getWidth(),
                                        bitmap.getHeight(),
                                        BufferedImage.TYPE_BYTE_GRAY);
                        image.getRaster().setDataElements(0, 0, bitmap.getWidth(), bitmap.getHeight(),
                                        bitmap.getPixels());

                        // Encode to PNG/JPEG → Base64
                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                        ImageIO.write(image, imageType.name(), output);
                        result.setOriginalBase64(Base64.getEncoder().encodeToString(output.toByteArray()));

                } catch (Exception e) {
                        result.setError("WSQ to Base64 conversion failed: " + e.getMessage());
                }
                return result;
        }

        private BufferedImage convertToGrayscale(BufferedImage original) {
                BufferedImage grayImage = new BufferedImage(
                                original.getWidth(),
                                original.getHeight(),
                                BufferedImage.TYPE_BYTE_GRAY);
                new ColorConvertOp(
                                ColorSpace.getInstance(ColorSpace.CS_GRAY),
                                null).filter(original, grayImage);
                return grayImage;
        }
}