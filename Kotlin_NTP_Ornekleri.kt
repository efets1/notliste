/*
 * Kotlin ile nesne tabanlı programlama: ders örnekleri
 * Kotlin/JVM 1.9 veya üstü. Android bağımlılığı yoktur.
 * Ayrı bir Kotlin/JVM projesinde bu dosyanın main fonksiyonunu çalıştırın.
 * Komut satırı alternatifi (Kotlin derleyicisi kuruluysa):
 * kotlinc Kotlin_NTP_Ornekleri.kt -include-runtime -d ornekler.jar
 * java -jar ornekler.jar
 *
 * Bunlar bellekte çalışan öğretim modelleridir.
 * Ödeme örneği gerçek para çekmez. Notlar diske kaydedilmez.
 * Slaytlarda aynı isimle gösterilen alternatif sınıf tanımları bu dosyaya
 * aynı anda eklenmemelidir. Burada birbiriyle uyumlu örnekler birleştirildi.
 */

import kotlin.properties.Delegates

// 1. Sınıf, kurucu, özellik, this ve üye fonksiyonu
class Not(metin: String) {
    var metin: String = metin.trim()
        private set

    init { require(this.metin.isNotEmpty()) }

    fun duzenle(metin: String) {
        val temiz = metin.trim()
        require(temiz.isNotEmpty())
        this.metin = temiz
    }

    fun ozet(): String = metin.take(12)
}

// 2. Interface, uygulama, kapsülleme ve kurucudan bağımlılık alma
interface NotDeposu {
    fun ekle(metin: String)
    fun tumu(): List<String>
}

class BellekNotDeposu : NotDeposu {
    private val notlar = mutableListOf<String>()
    override fun ekle(metin: String) { notlar.add(metin) }
    override fun tumu(): List<String> = notlar.toList()
}

class NotYoneticisi(private val depo: NotDeposu) {
    fun ekle(girdi: String): Boolean {
        val metin = girdi.trim()
        if (metin.isEmpty()) return false
        depo.ekle(metin)
        return true
    }
}

// 3. Kalıtım, override, super ve çok biçimlilik
open class Bildirim {
    open fun mesaj(): String = "Yeni not"
}

class SesliBildirim : Bildirim() {
    override fun mesaj(): String = super.mesaj() + " / Ses çal"
}

// 4. Soyut sınıf
abstract class DisariAktarici {
    fun dosyaAdi(): String = "notlar.txt"
    abstract fun kodla(metin: String): String
}

class DuzMetinAktarici : DisariAktarici() {
    override fun kodla(metin: String): String = metin
}

// 5. Veri sınıfı, enum, sealed, extension ve generic
data class NotVerisi(
    val id: Int,
    val metin: String,
    val tamamlandi: Boolean = false
)

enum class Oncelik { DUSUK, NORMAL, YUKSEK }

sealed interface KayitSonucu {
    data class Basarili(val id: Int) : KayitSonucu
    data class Hatali(val neden: String) : KayitSonucu
    data object Bekliyor : KayitSonucu
}

fun mesaj(sonuc: KayitSonucu): String = when (sonuc) {
    is KayitSonucu.Basarili -> "Not: ${sonuc.id}"
    is KayitSonucu.Hatali -> sonuc.neden
    KayitSonucu.Bekliyor -> "Bekleniyor"
}

fun NotVerisi.kisaMetin(): String = metin.take(20)

class BellekDeposu<T> {
    private val veriler = mutableListOf<T>()
    fun ekle(veri: T) { veriler.add(veri) }
    fun tumu(): List<T> = veriler.toList()
}

// 6. Interface delegasyonu
class SayacliDepo(private val asil: NotDeposu) : NotDeposu by asil {
    var eklemeSayisi = 0
        private set

    override fun ekle(metin: String) {
        asil.ekle(metin)
        eklemeSayisi++
    }
}

// 7. object, companion ve özellik delegasyonu
object NotKurallari {
    const val EN_UZUN_METIN = 500
    fun uygunMu(metin: String): Boolean =
        metin.isNotBlank() && metin.length <= EN_UZUN_METIN
}

class DogrulanmisNot private constructor(val metin: String) {
    companion object {
        fun olustur(girdi: String): DogrulanmisNot {
            val metin = girdi.trim()
            require(NotKurallari.uygunMu(metin))
            return DogrulanmisNot(metin)
        }
    }
}

class Oturum {
    val depo: NotDeposu by lazy { BellekNotDeposu() }
    var durum: String by Delegates.observable("Hazır") { _, eski, yeni ->
        println("$eski / $yeni")
    }
}

// 8. Tip güvenli kimlik ve operator
@JvmInline
value class NotId(val deger: Long)

data class Sayac(val adet: Int) {
    operator fun plus(diger: Sayac): Sayac = Sayac(adet + diger.adet)
}

// 9. Gerçek program senaryosu: ödeme stratejisi (simülasyon)
interface OdemeYontemi {
    fun ode(kurus: Long): String
}

class DenemeOdeme : OdemeYontemi {
    override fun ode(kurus: Long): String = "Deneme ödemesi: $kurus kuruş"
}

class Kasa(private val yontem: OdemeYontemi) {
    fun tamamla(kurus: Long): String = yontem.ode(kurus)
}

// 10. Gerçek program senaryosu: tek kitap kopyasının ödünç verilmesi
class Kitap(val baslik: String) {
    var oduncte = false
        private set

    fun oduncVer(): Boolean {
        if (oduncte) return false
        oduncte = true
        return true
    }
}

// 11. Fonksiyon tipleri ve lambda
fun notlariGoster(notlar: List<String>, goster: (String) -> Unit) {
    for (not in notlar) goster(not)
}

fun main() {
    println("1. Nesne ve referans")
    val not = Not("Süt al")
    val ayniNot = not
    ayniNot.duzenle("Ekmek al")
    check(not.metin == "Ekmek al")
    println(not.ozet())

    println("2. Not defteri iş kuralları")
    val depo = BellekNotDeposu()
    val yonetici = NotYoneticisi(depo)
    check(!yonetici.ekle("   "))
    check(depo.tumu().isEmpty())
    check(yonetici.ekle("  Süt al  "))
    check(depo.tumu() == listOf("Süt al"))
    notlariGoster(depo.tumu()) { println(it) }

    println("3. Çok biçimlilik")
    val bildirimler: List<Bildirim> = listOf(Bildirim(), SesliBildirim())
    for (bildirim in bildirimler) println(bildirim.mesaj())

    println("4. Veri sınıfı ve eşitlik")
    val ilk = NotVerisi(1, "Süt al")
    val ayniDeger = NotVerisi(1, "Süt al")
    check(ilk == ayniDeger)
    check(ilk !== ayniDeger)
    val son = ilk.copy(tamamlandi = true)
    check(!ilk.tamamlandi && son.tamamlandi)
    println(son)

    println("5. Generic ve sealed")
    val modeller = BellekDeposu<NotVerisi>()
    modeller.ekle(ilk)
    check(modeller.tumu().single() == ilk)
    println(mesaj(KayitSonucu.Basarili(1)))
    println(mesaj(KayitSonucu.Hatali("Boş metin")))

    println("6. Delegation ve üretim")
    val sayacli = SayacliDepo(BellekNotDeposu())
    sayacli.ekle("Kotlin çalış")
    check(sayacli.eklemeSayisi == 1)
    check(sayacli.tumu() == listOf("Kotlin çalış"))
    println(DogrulanmisNot.olustur("  Hazır  ").metin)
    val oturum = Oturum()
    check(oturum.depo === oturum.depo)
    oturum.durum = "Açık"
    check((Sayac(2) + Sayac(3)).adet == 5)
    check(NotId(42).deger == 42L)

    println("7. Ödeme ve kütüphane")
    println(Kasa(DenemeOdeme()).tamamla(2500))
    val kitap = Kitap("Kotlin Dersleri")
    check(kitap.oduncVer())
    check(!kitap.oduncVer())
    println("Kitap ödünçte: ${kitap.oduncte}")

    println("Bütün kontroller geçti.")
}
