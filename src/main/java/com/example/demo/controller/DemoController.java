package com.example.demo.controller;

import com.example.demo.security.SymmetricEncryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/encryption")
public class DemoController {

    private final SymmetricEncryption symmetricEncryption;

    @Autowired
    public DemoController(SymmetricEncryption symmetricEncryption) {
        this.symmetricEncryption = symmetricEncryption;
    }

    @PostMapping("/encrypt")
    public ResponseEntity<Map<String, String>> encryptFile(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            String encryptionKey = symmetricEncryption.generateEncryptionKey();
            byte[] encryptedContent = symmetricEncryption.encrypt(file.getBytes(), encryptionKey);
            String base64EncryptedContent = symmetricEncryption.encodeBase64(encryptedContent);

            Map<String, String> response = new HashMap<>();
            response.put("encryptionKey", encryptionKey);
            response.put("encryptedContent", base64EncryptedContent);
            response.put("originalFilename", file.getOriginalFilename());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/decrypt")
    public ResponseEntity<Resource> decryptFile(@RequestParam("file") MultipartFile file, @RequestParam("encryptionKey") String encryptionKey) throws IOException {
        try {
            byte[] encryptedContent = file.getBytes(); // Utilisez getBytes() pour obtenir les données du fichier
            byte[] decryptedContent = symmetricEncryption.decrypt(encryptedContent, encryptionKey);
            ByteArrayResource resource = new ByteArrayResource(decryptedContent);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", file.getOriginalFilename());
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}
