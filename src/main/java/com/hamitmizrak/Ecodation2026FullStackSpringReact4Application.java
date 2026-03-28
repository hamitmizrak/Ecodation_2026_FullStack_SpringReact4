package com.hamitmizrak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// Mongo aktif etmek ici
// @EnableMongoRepositories

// Aspect aktif etmek icin
// @EnableAspectJAutoProxy(proxyTargetClass = true)

// Asenkron açmak icin
// @EnableAsync

// Spring Cache aktif etmek gerekiyor.
// @EnableCaching

// Auditing Aktif etmek
// Dikkat: public class AuditingAwareBean içindeki method ismi:auditorAwareBeanMethod
@EnableJpaAuditing(auditorAwareRef = "auditingAwareBeanMethod")

// Configuration Properties taramasını aç
//@ConfigurationPropertiesScan(basePackageClasses = com.hamitmizrak.security.jwt.JwtProps.class)
// Spring Security: Şimdilik dahil etme, çünkü Bcrypted kullancağım ancak Spring security için gerekli kütüphaneleri dahil
// Buradaki exclude ne zaman kapatmam gerekiyor ? cevap: Spring Security ile çalıştığımız zaman kapat

// SCAN
//@EntityScan(basePackages = "com.hamitmizrak.techcareer_2025_backend_1.data.entity")//Entity bulamadığı zaman
//@EnableJpaRepositories(basePackages = "com.hamitmizrak.techcareer_2025_backend_1.data.repository") //Repository bulamadığı zaman
//@ComponentScan("com")
@SpringBootApplication(exclude = {
        // Spring Security Dahil etme
        //SecurityAutoConfiguration.class,
        SecurityAutoConfiguration.class,
        org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class,

        // JWT
        // Spring Security JWT kullanmak için exclude yapmamalıyız.......
        // org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        // org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class,

        // REDIS
        // Eğer Redis bağımlılığını kaldırmak istemiyorsanız ancak Redis yapılandırmasını devre dışı bırakmak istiyorsanız
        //RedisAutoConfiguration.class,
}
)
//@SpringBootApplication
public class Ecodation2026FullStackSpringReact4Application {

    public static void main(String[] args) {
        SpringApplication.run(Ecodation2026FullStackSpringReact4Application.class, args);
    }

}
