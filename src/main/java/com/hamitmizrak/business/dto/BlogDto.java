package com.hamitmizrak.business.dto;

import com.hamitmizrak.audit.AuditingAwareBaseDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.util.Date;

// LOMBOK
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Log4j2
public class BlogDto extends AuditingAwareBaseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    // ID
    private Long blogId;

    // BLOG HEADER
    @NotEmpty(message = "{blog.header.validation.constraints.NotNull.message}")
    @Size(min = 5, message = "{blog.header.least.validation.constraints.NotNull.message}")
    private String header;


    // BLOG TITLE
    @NotEmpty(message = "{blog.title.validation.constraints.NotNull.message}")
    @Size(min = 5, message = "{blog.title.least.validation.constraints.NotNull.message}")
    private String title;


    // BLOG CONTENT
    @NotEmpty(message = "{blog.content.validation.constraints.NotNull.message}")
    @Size(min = 5, message = "{blog.content.least.validation.constraints.NotNull.message}")
    private String content;


    // BLOG URL
    @NotEmpty(message = "{blog.url.validation.constraints.NotNull.message}")
    @Size(min = 5, message = "{blog.url.least.validation.constraints.NotNull.message}")
    private String url;

    // BLOG PICTURE
    @Builder.Default
    private String image="resim.png";

    // DATE
    private Date systemCreatedDate;

    // Blog(N) - BlogCategory(1)
    private BlogCategoryDto blogCategoryDto;

}
