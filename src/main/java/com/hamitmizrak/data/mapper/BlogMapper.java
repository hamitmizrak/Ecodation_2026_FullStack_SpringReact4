package com.hamitmizrak.data.mapper;

import com.hamitmizrak.business.dto.BlogCategoryDto;
import com.hamitmizrak.business.dto.BlogDto;
import com.hamitmizrak.data.entity.BlogCategoryEntity;
import com.hamitmizrak.data.entity.BlogEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BlogMapper {

    // Entity ==> Dto
    public BlogDto toDto(BlogEntity entity){
        if(entity == null) return null;
        return BlogDto.builder()
                .blogId(entity.getBlogId())
                .header(entity.getHeader())
                .title(entity.getTitle())
                .content(entity.getContent())
                .url(entity.getUrl())
                .image(entity.getImage())
                .systemCreatedDate(entity.getSystemCreatedDate())
                // ilişkisel
                .blogCategoryDto(BlogCategoryMapper.toDto(entity.getBlogCategoryToBlogEntity()))
                .build();
    }


    // Entity ==> Dto
    public BlogEntity toEntity(BlogDto dto){
        if(dto == null) return null;
        return BlogEntity.builder()
                .blogId(dto.getBlogId())
                .header(dto.getHeader())
                .title(dto.getTitle())
                .content(dto.getContent())
                .url(dto.getUrl())
                .image(dto.getImage())
                .systemCreatedDate(dto.getSystemCreatedDate())
                .build();
    }

}
