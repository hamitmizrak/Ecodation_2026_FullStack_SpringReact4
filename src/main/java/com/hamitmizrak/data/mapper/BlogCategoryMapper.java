package com.hamitmizrak.data.mapper;

import com.hamitmizrak.business.dto.BlogCategoryDto;
import com.hamitmizrak.data.entity.BlogCategoryEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BlogCategoryMapper {

    // Entity ==> Dto
    public BlogCategoryDto toDto(BlogCategoryEntity entity){
        if(entity == null) return null;
        return BlogCategoryDto.builder()
                .blogCategoryId(entity.getBlogCategoryId())
                .categoryName(entity.getCategoryName())
                .systemCreatedDate(entity.getSystemCreatedDate())
                .build();
    }


    // Entity ==> Dto
    public BlogCategoryEntity toEntity(BlogCategoryDto dto){
        if(dto == null) return null;
        return BlogCategoryEntity.builder()
                .blogCategoryId(dto.getBlogCategoryId())
                .categoryName(dto.getCategoryName())
                .systemCreatedDate(dto.getSystemCreatedDate())
                .build();
    }

}
