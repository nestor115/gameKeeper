package com.example.gamekeeper.sampledata;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryConfig {
    private static Cloudinary cloudinary;

    public static Cloudinary getInstance() {
        if (cloudinary == null) {
            cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "dxgk71sz7",
                    "api_key", "795251117794363",
                    "api_secret", "RJp_HzvaYZNktbfxKJP7UcJtsnA"
            ));
        }
        return cloudinary;
    }
}

