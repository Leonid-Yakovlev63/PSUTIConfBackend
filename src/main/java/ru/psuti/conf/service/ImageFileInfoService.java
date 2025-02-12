package ru.psuti.conf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.psuti.conf.entity.ImageFileInfo;
import ru.psuti.conf.repository.ImageFileInfoRepository;

import java.util.Optional;

@Service
public class ImageFileInfoService {

    @Autowired
    private ImageFileInfoRepository imageFileInfoRepository;

    public Optional<ImageFileInfo> getImageFileInfoById(Long id) {
        return imageFileInfoRepository.findById(id);
    }

    public ImageFileInfo saveImageFileInfo(ImageFileInfo imageFileInfo){
        return imageFileInfoRepository.save(imageFileInfo);
    }
}
