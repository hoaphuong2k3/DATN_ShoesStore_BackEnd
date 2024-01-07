package com.example.shoestore.core.common.mapper;

import com.example.shoestore.core.common.mapper.base.BaseMapper;
import com.example.shoestore.core.product.admin.dto.ImageDTO;
import com.example.shoestore.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageMapper extends BaseMapper<Image, ImageDTO> {

    List<ImageDTO> entityListToDTOList(List<Image> entityList);

    List<Image> createsToEntities(Long shoesDetailId, List<MultipartFile> files);

}
