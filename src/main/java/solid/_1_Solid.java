package solid;

public class _1_Solid {

    /*

    S1-) SOLID Nedir ?
    C1-) SOLID;
     -Nesneye dayalı tasarım ,
     - bakım
     - genişletilmek
     - test edilebilir olmasını hedefleyen beş temel prensip türüdür


    S1-) SOLID Hakkında
    C1-)
     - Birbirinden bağımsız değildir
     - Çoğu zaman beraber kullanılır
     - Amaç sadece temiz kod yazmak değil dirençli mimari kurmaktır.
     - Spring, Java EE, Jakarta EE SOLID çok sık kullanılır.


    S1-) SOLID Açılımı Nedir ?
    C1-) SOLID
     - S: Single Responsibility
     - O: Open/Closed
     - L: Liskov Substitution
     - I: Interface Segregation
     - D: Dependency Inversion


    S1-) SOLID Türkçe Anlamları ?
    C1-)
     - S: Single Responsibility (SRP) ==> Tek sorumluk
     - O: Open/Closed           (OCP) ==> Genişlemeye açık
     - L: Liskov Substitution   (LSP) ==> Yerine geçebilirlik
     - I: Interface Segregation (ISP) ==> Küçük ama amaç odaklı arayüz
     - D: Dependency Inversion  (DIP) ==> Soyuta bağlılık


    S1-) S: Single Responsibility  ?
    C1-) S:
     - Anlamı: Bir sınıfın veya modülün yalnızca bir sorumluğu, dolayısıyla tek bir değişim nedeni olamalıdır.
     - Amacı : Bakımı kolylaştırmak, yan etkileri azlatmak ve test kapsamını küçültmek
     - Örneği: OrderService sadece sipariş kurallını yöneten: NotificationService
     - İhlal belirtisi: Aynı sınıfta valdiation, veri tabanı kaydı, loglama , eposta gönderimi


    S1-) O: Open/Closed  ?
    C1-)
     - Anlamı: Sistem bileşeni genişlemeye açık, mevcut çalışan kodu değiştirmeye kapalı olmalıdır.
     - Amacı : Yeni davranıi eklerken stabil kodu bozmadan çalıştırmak
     - Örneği: PaymentProcessor arayüzü vardır, CreditCardPayment, TransferPayment yeni bir sınıf eklemek
     - İhlal belirtisi: Her yeni ödeme tipinde if-else veya switch yeni case ekleniyorsa OCP zayıftır



    S1-) L: Liskov Substitution  ?
    C1-)
     - Anlamı: Alt sınıf, üst sınıfın beklentilerini bozmadan onun yerine kullanabilmesidir
     - Amacı : Kalıtımın güvenli ve davranışsal oalrak tutarlılığı ve kullanılmasını sağlamak
     - Örneği:
     - İhlal belirtisi



    S1-) I: Interface Segregation?
    C1-)
     - Anlamı: İstemciler kullamadıkları metodlara bağımlı bırakılmamalıdır.
     - Amacı : Gereksiz bağımlılıkları ve boş implementasyonları azaltmak
     - Örneği: PRinter, Scanenr, fax ayrı ayrı arayüzlerde kullanmak
     - İhlal belirtisi: deploy(), code(), eat()



    S1-) D: Dependency Inversion ?
    C1-)
     - Anlamı: ÜSt seviye modüller alt seviye modüüleri değil, her ikisini de soyutlamalara bağlı olmaldır.
     - Amacı : Bağımlılıkalr gevşektir ve test edilebilir
     - Örneği:
     - İhlal belirtisi



    S1-) SOLID ve Java Spring İlişkisi
    C1-)
     - @Service
     - @Repository
     - @Component
     -


    S1-) SOLID Nedir ?
    C1-)
     -
     -
     -
     -

    S1-) SOLID Nedir ?
    C1-)
     -
     -
     -
     -


    S1-) SOLID Nedir ?
    C1-)
     -
     -
     -
     -




     */



}
