# Panel: Dosya Yükleme Kılavuzu

Bu dizin, sizin GitHub repoya dosyalarınızı (kaynak kod, keystore, vs.) yükleyebilmeniz için kısa ve açık adımları içerir. Aşağıdaki yöntemlerden birini seçerek dosyalarınızı `ci/add-apk-workflow` branch'ine yükleyin — ben ardından PR açıp CI workflow'unu çalıştıracağım ve APK üretimine yardımcı olacağım.

Dikkat: Özel anahtar/keystore gibi hassas dosyaları doğrudan repoya commit etmeyin. Keystore kullanacaksak bunu GitHub Secrets olarak ekleyeceğiz. Aşağıda hem `secret` yöntemi hem de geçici yükleme yolları anlatılmıştır.

---

## Önerilen akış (kolay ve güvenli)
1. Yerel bilgisayarınızda dosyalarınızı bir klasörde toplayın (ör. C:\Users\You\samed-project). `app` klasörünüz ve gradle wrapper dosyaları olmalı.
2. Keystore kullanacaksanız (release imzası): keystore dosyanızı localde tutun ve GitHub Secrets olarak yükleyin (asla doğrudan commit etmeyin). README'nin altındaki "Keystore ve Secrets" bölümüne bakın.
3. Git ile dosyalarınızı `ci/add-apk-workflow` branch'ine gönderin (aşağıdaki Git adımlarını kullanın).
4. GitHub üzerinde PR açın (veya ben sizin için PR açabilirim). PR onaylandıktan sonra ben workflow'u tetikleyip APK'yı üretebilirim.

---

## Yöntem A — Git (önerilen, Windows PowerShell örneği)
Aşağıdaki komutları PowerShell'de çalıştırın. `C:\path\to\project` kısmını kendi proje klasör yolunuzla değiştirin.

1) Proje kökünde terminal açın:
   cd C:\path\to\project

2) Yeni branch oluşturup checkout alın (yerelde):
   git checkout -b add-files-for-ci

3) Değişiklikleri ekleyin (örnek olarak tüm dosyayı):
   git add .

4) Commit yapın:
   git commit -m "Add project files for CI APK build"

5) Uzak branch oluşturun (GitHub `ci/add-apk-workflow` branch'ine push etmek için):
   git push origin HEAD:ci/add-apk-workflow

Notlar:
- Eğer GitHub repo'nuz uzak olarak `origin` değilse `git remote -v` ile doğrulayın.
- Bu push, yereldeki `add-files-for-ci` branch'inizi doğrudan uzak `ci/add-apk-workflow` branch'ine yazacaktır (mevcutsa üzerine yazabilir). Eğer uzak branch zaten içerik içeriyorsa ve üzerine yazmak istemiyorsanız önce `git fetch origin` ve `git rebase origin/ci/add-apk-workflow` yapın.

## Yöntem B — GitHub Web UI (kolay, tek dosya/az dosya için)
1) https://github.com/samed5266-ui/samedyazilim4/tree/ci/add-apk-workflow adresine gidin.
2) `Add file` → `Upload files` seçeneğini kullanın ve dosyalarınızı sürükleyip bırakın.
3) Commit mesajı girin ve "Commit directly to the ci/add-apk-workflow branch" seçeneğini seçin.

Not: Birden fazla dosya/klasör yüklerken web UI bazen başarısız olabilir; büyük dosyalar için Git kullanın.

---

## Keystore ve Secrets (Güvenlik için önemli!)
- **Keystore (.jks)** dosyasını asla doğrudan repoya commit etmeyin.
- Keystore'u GitHub Secrets olarak ekleyin. Workflow'umuz `KEYSTORE_BASE64` şeklinde base64 kodlanmış keystore bekleyebilir.

Windows PowerShell ile keystore'u base64'e çevirme örneği:

```powershell
$b = [System.IO.File]::ReadAllBytes("C:\path\to\my-release-key.jks")
[Convert]::ToBase64String($b) | Out-File -Encoding ascii key.b64.txt
```
key.b64.txt içindeki tek satırı GitHub → Settings → Secrets and variables → Actions → New repository secret olarak `KEYSTORE_BASE64` adıyla ekleyin. Diğer secret'lar:
- KEYSTORE_PASSWORD
- KEY_ALIAS
- KEY_PASSWORD

Ben secrets eklendikten sonra workflow'u kullanıp keystore'u runner içine decode ederek kullanacağım.

---

## PR ve CI
- Dosyalarınızı `ci/add-apk-workflow` branch'ine yükledikten sonra GitHub'da bu branch için bir Pull Request açın (veya ben sizin için açarım).
- PR üzerinden kodu gözden geçirip merge ettiğinizde veya manuel olarak Actions -> Build Android APK -> Run workflow ile tetikleyebilirsiniz.
- CI tamamlandığında `Actions` -> seçili workflow run -> `Artifacts` bölümünden üretilen APK'yı indirebilirsiniz.

---

## Yardım gerekiyorsa — bana şunları gönderin
- PowerShell terminal'de çalıştırdığınız `git` komutları ve çıkan mesajlar
- Eğer push hatası alırsanız terminal hata çıktısını kopyalayın
- Eğer keystore veya secrets ekleme konusunda yardım isterseniz, ben adım adım gösterebilirim

---

Ben şimdi bu README dosyasını `ci/add-apk-workflow` branch'ine ekledim. Dosyayı yükledikten sonra bana "yükledim" deyin; ben PR açıp workflow'u tetiklemede yardımcı olurum ve APK üretimi için adımları tamamlarım.
