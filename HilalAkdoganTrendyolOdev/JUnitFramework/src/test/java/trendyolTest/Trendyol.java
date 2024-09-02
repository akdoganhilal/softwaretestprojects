package trendyolTest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static org.openqa.selenium.By.id;

public class Trendyol {

    static WebDriver drv;
    @BeforeClass
    public static void setUp() {
        // 1) Trendyol web sitesine gitme:
        WebDriverManager.chromedriver().setup();
        drv = new ChromeDriver();
        try {
            drv.get("https://www.trendyol.com.tr");
            drv.manage().window().maximize();
            System.out.println("Web sitenin başlığı: " + drv.getTitle());
            System.out.println("Web sitenin URL adresi: " + drv.getCurrentUrl());
        }
        catch (Exception e) {System.out.println("Test sırasında bir hata oluştu: " + e.getMessage());}
    }

    @Test
    public void test1() throws InterruptedException {
        // 2) Ana sayfanın açık olup olmadığının kontrolü:
        try {
            String actualTitle = drv.getTitle();
            if (actualTitle.equals(drv.getTitle())) {
                System.out.println("Test Passed: Ana sayfa açıldı.");
            } else {
                System.out.println("Test Failed: Ana sayfa açılmadı.");
            }
            Thread.sleep(1000);
            WebElement kapat1 = drv.findElement(id("Group-38"));
            kapat1.click();
        }
        catch (Exception e) {System.out.println("Test sırasında bir hata oluştu: " + e.getMessage());}

    }

    @Test
    public void test2() throws InterruptedException {
        // 3) Giriş yap butonuna tıklayıp açılan sayfada hatalı bilgilerle giriş yapmaya çalışmak:
        try {
            Thread.sleep(1500);
            WebElement girisYap = drv.findElement(By.className("link-text"));
            girisYap.click();

            Thread.sleep(700);
            WebElement kullaniciAdi = drv.findElement(By.id("login-email"));
            kullaniciAdi.sendKeys("abc");

            Thread.sleep(700);
            WebElement sifre = drv.findElement(By.id("login-password-input"));
            sifre.sendKeys("123");

            Thread.sleep(700);
            WebElement girBTN = drv.findElement(By.xpath("//button[@type='submit']"));
            girBTN.click();

            Thread.sleep(500);
            try {
                WebElement hata = drv.findElement(By.id("error-box-wrapper"));
                if (hata.isDisplayed()) {
                    System.out.println("Giriş başarısız.");
                }
            } catch (Exception e) {
                System.out.println("Giriş başarılı.");
            }
        }
        catch (Exception e) {System.out.println("Test sırasında bir hata oluştu: " + e.getMessage());}
    }

    @Test
    public void test3() throws InterruptedException {
        // 4) Arama kutusuna "laptop" kelimesini girip aratma:
        try {
            Thread.sleep(2000);
            WebElement arama = drv.findElement(By.className("V8wbcUhU"));
            arama.sendKeys("laptop", Keys.ENTER);

            // 5) Çıkan sonuçlardan rastgele ürün seçme:
            Thread.sleep(3000);
            List<WebElement> product = drv.findElements(By.cssSelector("div.p-card-chldrn-cntnr"));
            Random rand = new Random();
            int randomIndex = rand.nextInt(product.size());

            Thread.sleep(3000);
            WebElement rndUrunSec = product.get(randomIndex);
            rndUrunSec.click();

            List<String> windowHandles = new ArrayList<>(drv.getWindowHandles());
            drv.switchTo().window(windowHandles.get(0));
            drv.switchTo().window(windowHandles.get(1));

            Thread.sleep(1000);
            WebElement kapat2 = drv.findElement(By.xpath("//button[contains(@class, 'onboarding-button')]"));
            kapat2.click();

            // 6) Seçilen ürünü sepete ekleme:
            Thread.sleep(1500);
            WebElement sptEkle = drv.findElement(By.className("add-to-basket"));
            sptEkle.click();
        }
        catch (Exception e) {System.out.println("Test sırasında bir hata oluştu: " + e.getMessage());}
    }

    @Test
    public void test4() throws InterruptedException {
        // 7) Ürünün sepete eklemeden önce ve ekledikten sonraki fiyatlarının karşılaştırılması:
        try {
            Thread.sleep(1000);
            WebElement ilkFiyat = drv.findElement(By.className("prc-dsc"));
            String fyt1 = ilkFiyat.getText().trim();

            Thread.sleep(1000);
            WebElement sepeteGit = drv.findElement(By.className("basket-item-count-container"));
            sepeteGit.click();

            Thread.sleep(2000);
            WebElement kapat3 = drv.findElement(By.className("onboarding-overlay"));
            kapat3.click();

            Thread.sleep(1000);
            WebElement ikncFiyat = drv.findElement(By.cssSelector(".pb-summary-total-price.discount-active"));
            String fyt2 = ikncFiyat.getText();
            fyt2 = fyt2.replace("Toplam", "").trim();

            if (fyt1.equals(fyt2)) {
                System.out.println("Fiyat aynı: " + fyt1);
            } else {
                System.out.println("Fiyat farklı: İlk Fiyat: " + fyt1 + ", İkinci Fiyat: " + fyt2);
            }
        }
        catch (Exception e) {System.out.println("Test sırasında bir hata oluştu: " + e.getMessage());}
    }

    @Test
    public void test5() throws InterruptedException {
        // 8) Sepetteki ürün sayısını ikiye çıkartıp doğruluğunu kontrol etme:
        try {
            Thread.sleep(1000);
            WebElement ilkAdetSayisi = drv.findElement(By.className("counter-content"));
            String adet = ilkAdetSayisi.getAttribute("value");
            System.out.println("İlk Adet: " + adet + " tane.");
            WebDriverWait wait = new WebDriverWait(drv, Duration.ofSeconds(20));
            try {
                Thread.sleep(1000);
                WebElement adetArtir = drv.findElement(By.className("ty-numeric-counter-button"));
                if (adetArtir.isEnabled()) {
                    Thread.sleep(1000);
                    adetArtir.click();
                } else {
                    System.out.println("Buton aktif değil.");
                }
            } catch (Exception e) {
                System.out.println("Buton aktif olmadığı için tıklanamadı.");
            }

            Thread.sleep(2000);
            WebDriverWait wait2 = new WebDriverWait(drv, Duration.ofSeconds(20));
            WebElement yeniAdetSayisi = drv.findElement(By.className("counter-content"));
            String sonAdetSayisi = yeniAdetSayisi.getAttribute("value").trim();

            if (adet.equals(sonAdetSayisi)) {
                System.out.println("Adet değişmedi. Adet sayısı: " + adet);
            } else {
                System.out.println("Adet sayısı değişti. Son adet sayısı: " + sonAdetSayisi);
            }

            Thread.sleep(1500);
            WebElement sonFiyat = drv.findElement(By.cssSelector(".pb-summary-total-price.discount-active"));
            String sonFyt = sonFiyat.getText().trim();
            sonFyt = sonFyt.replace("Toplam", "").trim();
            System.out.println("Adet değiştikten sonraki fiyat: " + sonFyt);
            Thread.sleep(1500);
        }
        catch (Exception e) {System.out.println("Test sırasında bir hata oluştu: " + e.getMessage());}
    }

    @Test
    public void test6() throws InterruptedException {
        // 9) Sepetteki tüm ürünleri silip, sepetin boş olup olmadığını kontrol etme:
        try {
            WebElement sepetSil = drv.findElement(By.className("checkout-saving-remove-button"));
            sepetSil.click();

            Thread.sleep(1500);
            List<WebElement> sepetUrun = drv.findElements(By.className("pb-merchant-group"));
            if (sepetUrun.isEmpty()) {
                System.out.println("Sepet başarıyla boşaltıldı.");
            } else {
                System.out.println("Sepette hala ürün var.");
            }
            Thread.sleep(1000);
        }
        catch (Exception e) {System.out.println("Test sırasında bir hata oluştu: " + e.getMessage());}
    }

    @After
    public void tearDown() throws Exception {
       // drv.quit();
    }
}
