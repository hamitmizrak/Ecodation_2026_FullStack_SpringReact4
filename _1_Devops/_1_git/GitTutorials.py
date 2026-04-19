"""
S1-) Git Nedir ?
c1-) Git,
-Dağıtık bir (VCS) sürüm kontrol sistemidir.
-Kaynak kodundaki değişiklikleri takip eder, gelişitiricilerin paralel çalışmasını mümkün kılar
- Git'i günlük geliştirme akışının omurgasıdır.
- Git doğru kullanmak proje kalitesini doğrudan etkiler

S2-) Git Mimarisi Hakkında Bilgiler Veriniz ?
C2-) Git Mimarisi,
- Working Tree      : Geliştiricinin diskte gördüğü mevcut çalışma ortamıdır
- Staging Area      : Bir sonraki commit'e gidecek içeeriğin hazırlanması ara katmandır (git add .)
- Local Repository  : Commit nesnelerin referanslarını tutludğu yerel depodur.
- Remote Repository : Git sybycybda bulunan verilerin uzak serverlarda tutulmasıdır (GitHub, GitLab,Bitbucket)


S-3)Git Nesne Modeli Hakkında Bilgi veriniz ?
C-3)Git, içeriği ağırlıklı olarak nesneler ve referanslar üzerinden saklar.
- blob : Dosyanın içeriğini bulundurğu yerdir.
- tree : Dizin yapısını ve blob/tree referanslarıdır
- commit :Geçmişisn ana omurgasını oluşturur.
- tag : Veriyonlama ve release için kullanılır.
-

S-4) Ref, HEAD, Branch kavramları hakkında bilgi veriniz.
C-4)
- Branch: Bir commit kopyası değildir. Commit zicirindeki son noktayı gösterir.
- HEAD : Aktif çalışma bağlamını gösteren özel referanstır.
- Ref: Referanslar
-
-

S-5) Git Yaşam Döngüsü Açıklayınız ?
C-5) Git Yaşam Döngüsü;
- Dosya Working Tree'de düzenlenir
- git status ile değişiklikleri gözlemle
- git add ile değişikliklier staging area alanına taşı
- git commit ile repository geçmişini kalıcı kayıt olmasını sağla
- git push ile paylaşılan uzak depoya gönderilir.



S-6) Neden SVN yerine Git Kullanıyoruz ?
C-6)  Git;
- Yerelde hızlı çalışır
- Branch ucuzdur.
- Çevrim dışı commit atılabilir.
- Dağıtık yapıya sahiptir.
- Daha esnektir

S-7) HEAD nedir ?
C-7) Head;
- Aktif brach veya doğrudan seçili commit'i gösteren referanstır.
- Örneğin: git reflog


S-8) Yanlışlıkla silinen commit'i nasıl bulursunuz ?
C-8) Önce  `git reflog` reflog ile HEAD hareketine bakarım
-


S-9) Git'te iyi bir commit nasıl olmalıdır ?
C-9) İyi bir Commit;
- Atomik değişikliği anlatmalı
- Kısa ve açıklayıcı olsun
- İzlenebilir olmalı
- Mümkünse birbiriyle ilişkisi olayan modüller gönderelim.


S-10) pull request neden önemli ?
C-10) pull request
- Kod inceleme
- Kalite kapısı
- Bilgi paylaşımı
- Audit izi sağlar


S-11) Git ile CI/CD ilişkisi nedir ?
C-11) Branch, tag ve merge olayları pipeline'lar ile tetiklenir.


S-12) Branch ile Tag arasındaki farklar ?
C-12)
- Branch: Hareketlidir Yeni commit geldikçe ilerler
- Tag   : Çoğunlukla sabittir ve release(sürüm) noktasını işaret eder.


S-13) Detached HEAD nednen tehlikelidir ?
C-13) Branch üstünde değil doğrudan commit üstünden çalışdığı için yeni commitlerin sonradan kaybolması gibi görüğlebilir.
- Yani Branch oluşturmadan bıraklırsa takip zorlaşır.

S-14) Cherry-pick ile merge arasındaki fark nedir ?
C-14) Cherry-pick - Merge;
- Cherry-pick: Seçilen comiti tekil taşır.
- Merge      : Dal geçmişini bütünsel olarak entegre eder.


S-15) Conflict sonrasında yapılaması gerekenler nelerdir ?
C-15)
-
-
-
-
-


S-16) Merge conflict yaşamamak için ne yaparsınız ?
C-16)
-
-
-
-
-



S-17) Force Push hangi koşulalrda kabul edilir ?
C-17)
-
-
-
-
-


S-18) Büyük binary dosyalar için standart Git yeterli midir  ?
C-18)
-
-
-
-
-



S-18) Git komutlarını açıklayınız ?
C-18) Aşağıda verilen komutların ne işe yaradığını yazınız ?
-
-
-
-
-





S-)
C-)
-
-
-
-
-

"""