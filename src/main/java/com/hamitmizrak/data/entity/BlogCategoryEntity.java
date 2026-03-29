package com.hamitmizrak.data.entity;

import com.hamitmizrak.audit.AuditingAwareBaseDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

// LOMBOK
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Log4j2

// ENTITY
// BlogCategory(1)- BlogEntity(N)
@Entity
@Table(name="blog_categories")
public class BlogCategoryEntity extends AuditingAwareBaseDto {


    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blogCategoryId;

    // CATEGORY NAME
    @Column(nullable = false, unique = true,length = 255)
    private String categoryName;

    // DATE
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date systemCreatedDate;

    // BlogCategory(1)- Blog(N)
    @OneToMany(mappedBy = "blogCategoryToBlogEntity", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = false)
    private List<BlogEntity> blogCategoryToBlogEntityList;

}
