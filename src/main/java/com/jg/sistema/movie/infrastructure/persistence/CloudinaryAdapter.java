package com.jg.sistema.movie.infrastructure.persistence;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.jg.sistema.movie.application.port.out.CloudStorePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CloudinaryAdapter implements CloudStorePort {

    private final Cloudinary cloudinary;

    @Override
    public Map upload(Object file, Map params) throws IOException {
        if(params == null || params.isEmpty()) return cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        return cloudinary.uploader().upload(file, params);
    }
}
