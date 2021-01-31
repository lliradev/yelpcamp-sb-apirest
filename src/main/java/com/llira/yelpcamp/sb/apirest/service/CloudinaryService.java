package com.llira.yelpcamp.sb.apirest.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class CloudinaryService {
    private final Logger log;
    private final Cloudinary cloudinary;

    public CloudinaryService() {
        Map<String, String> params = new HashMap<>();
        params.put("cloud_name", "your_cloud_name");
        params.put("api_key", "your_api_key");
        params.put("api_secret", "your_api_secret");
        this.cloudinary = new Cloudinary(params);
        this.log = LoggerFactory.getLogger(CloudinaryService.class);
    }

    public Map<?, ?> upload(MultipartFile multipartFile) throws IOException {
        File file = convert(multipartFile);
        Map<?, ?> res = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        boolean isDelete = file.delete();
        log.info("File deleted: {}", isDelete);
        return res;
    }

    public Map<?, ?> delete(String id) throws IOException {
        return cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(UUID.randomUUID().toString() + "_" +
                Objects.requireNonNull(multipartFile.getOriginalFilename()).replace(" ", ""));
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multipartFile.getBytes());
        fos.close();
        return file;
    }
}
