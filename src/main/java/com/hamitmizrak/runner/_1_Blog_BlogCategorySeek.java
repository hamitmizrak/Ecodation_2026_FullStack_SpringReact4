package com.hamitmizrak.runner;

import com.hamitmizrak.data.entity.BlogCategoryEntity;
import com.hamitmizrak.data.entity.BlogEntity;
import com.hamitmizrak.data.repository.IBlogCategoryRepository;
import com.hamitmizrak.data.repository.IBlogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

// LOMBOK
@RequiredArgsConstructor
@Log4j2
@Configuration
@Order(1)
public class _1_Blog_BlogCategorySeek {

    // Injection
    private final IBlogCategoryRepository iBlogCategoryRepository;
    private final IBlogRepository iBlogRepository;

    //
    @Bean
    public CommandLineRunner blogAndBlogCategoryCommandLineRunner() {
        return args -> {
            System.out.println("Blog Command Line Runner Çalılıyor");

            // Tekil ile başla (Category(1))
            BlogCategoryEntity bilgisayarCategor = new BlogCategoryEntity();
            bilgisayarCategor.setCategoryName("Bilgisayar");
            iBlogCategoryRepository.save(bilgisayarCategor);


            BlogCategoryEntity tabletCategor = new BlogCategoryEntity();
            tabletCategor.setCategoryName("Tablet");
            iBlogCategoryRepository.save(tabletCategor);

            // Çoğul  (Blog(N))
            BlogEntity blogEntity1 = new BlogEntity();
            blogEntity1.setHeader("Header-1");
            blogEntity1.setTitle("Title-1");
            blogEntity1.setContent("Content-4");
            blogEntity1.setBlogCategoryToBlogEntity(bilgisayarCategor);
            iBlogRepository.save(blogEntity1);


            BlogEntity blogEntity2 = new BlogEntity();
            blogEntity2.setHeader("Header-2");
            blogEntity2.setTitle("Title-2");
            blogEntity2.setContent("Content-2");
            blogEntity2.setBlogCategoryToBlogEntity(tabletCategor);
            iBlogRepository.save(blogEntity2);
        };
    }
}
