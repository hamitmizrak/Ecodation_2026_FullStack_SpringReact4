package com.hamitmizrak.data.entity;

import com.hamitmizrak.audit.AuditingAwareBaseDto;
import com.hamitmizrak.business.dto.BlogCategoryDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.hibernate.annotations.CreationTimestamp;


import java.util.Date;

// LOMBOK
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Log4j2

// ENTITY
// BlogEntity(N) - BlogCategory(1)
@Entity
@Table(name="blogs")
public class BlogEntity extends AuditingAwareBaseDto {

    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blogId;

    // BLOG HEADER
    @Column(nullable = false, length = 450)
    private String header;

    // BLOG TITLE
    private String title;

    // BLOG CONTENT
    @Lob
    private String content;

    // BLOG URL
    private String url;

    // BLOG PICTURE
    @Column(length = 350)
    private String image;

    // DATE
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date systemCreatedDate;

    // Blog(N) - BlogCategory(1)
    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name="blogCategoryId",nullable = false)
    private BlogCategoryEntity blogCategoryToBlogEntity;

}
