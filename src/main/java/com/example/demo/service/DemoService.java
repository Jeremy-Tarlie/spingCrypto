package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.security.SymmetricEncryption;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class DemoService {
    private final SymmetricEncryption symmetricEncryption;

    public DemoService(SymmetricEncryption symmetricEncryption) {
        this.symmetricEncryption = symmetricEncryption;
    }

    public byte[] encryptFile(MultipartFile file) throws Exception {
        byte[] fileData = file.getBytes();
        return symmetricEncryption.encrypt(fileData);
    }

    public byte[] decryptFile(MultipartFile file) throws Exception {
        byte[] fileData = file.getBytes();
        return symmetricEncryption.decrypt(fileData);
    }
}
