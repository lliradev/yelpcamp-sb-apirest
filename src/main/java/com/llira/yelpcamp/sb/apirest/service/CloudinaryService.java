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
    private final Logger log = LoggerFactory.getLogger(CloudinaryService.class);
    private final Cloudinary cloudinary;

    public CloudinaryService() {
        Map<String, String> params = new HashMap<>();
        params.put("cloud_name", "");
        params.put("api_key", "");
        params.put("api_secret", "");
        this.cloudinary = new Cloudinary(params);
    }

    public Map<?, ?> upload(MultipartFile multipartFile) {
        File file;
        Map<?, ?> res = null;
        try {
            file = convert(multipartFile);
            res = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            boolean isDelete = file.delete();
            log.info("File deleted: {}", isDelete);
        } catch (IOException e) {
            log.error("{}", e.getMessage());
            e.printStackTrace();
        }
        return res;
    }

    public Map<?, ?> delete(String id) throws IOException {
        return cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
    }

    private File convert(MultipartFile multipartFile) {
        File file = null;
        try {
            file = new File(UUID.randomUUID() + "_" + Objects
                    .requireNonNull(multipartFile.getOriginalFilename())
                    .replace(" ", ""));
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();
        } catch (IOException e) {
            log.error("{}", e.getMessage());
            e.printStackTrace();
        }
        return file;
    }
}
