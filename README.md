# WSQ Fingerprint Converter API

A lightweight **Spring Boot REST API** for converting fingerprint images between **Base64** and **WSQ (Wavelet Scalar Quantization)** formats, WSQ encode based on NBIS and/or JNBIS.  
Ideal for biometric systems, forensic applications, and identity verification workflows.

---

## ✨ Features
- **Convert Base64 → WSQ** — Accepts a Base64-encoded fingerprint image and returns the WSQ binary format.
- **Convert WSQ → Base64** — Converts WSQ fingerprint data back to a Base64 image in the desired format (e.g., BMP,PNG, JPEG).
- **Simple JSON API** — Easy to integrate into any client application.
- **Robust Error Handling** — Validates input and returns meaningful error responses.

---

## 📡 API Endpoints

### 1. Convert Base64 to WSQ
**POST** `/api/fingerprint/convert-to-wsq`  

**Request Body**
```json
{
  "base64Image": "<Base64 encoded fingerprint image>"
}

```

**Response Body**
```json
{
  "wsqString": "<WSQ encoded string>",
  "originalBase64":"<Base64 encoded string>"
}


```

### 2. Convert WSQ to base64
**POST** `api/fingerprint/convert-wsq-to-base64`  

**Request Body**
```json
{
  "wsqString": "<WSQ encoded string>",
  "imageType":"BMP"
}

```

**Response Body**
```json
{
  "wsqString": "<WSQ encoded string>",
  "originalBase64":"<Base64 encoded string>"
}

```

## 🛠 Tech Stack
  Java 17+
  Spring Boot
  Maven
  Base64 Encoding/Decoding
  WSQ Image Processing


## 🚀 Getting Started
### Prerequisites
  Java 17 or higher
  Maven

### Installation

   ```
      git clone https://github.com/ansyg/wsq-base64-decoder-encoder.git
      cd <your-repo>
      mvn clean install
```
## Running the Application
 ```
     mvn spring-boot:run
```
### The API will be available at:
```
  http://localhost:8080



