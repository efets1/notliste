package com.passanger.not_defteri_uygulamas

import androidx.recyclerview.widget.RecyclerView

/**
 * ADIM 6: Not silme ve liste operasyonlarını içeren yardımcı fonksiyonlar.
 * Bu dosya, seçilen notun listeden kaldırılmasını sağlar.
 */

/**
 * YENİ KAVRAMLAR:
 * - 'id': Silinecek notu benzersiz bir şekilde belirler.
 * - 'removeAll': Veri listesinden (notes) verilen koşula uyan kaydı tamamen kaldırır.
 * - 'adapter.notifyDataSetChanged()': Yalnızca görünümü gizlemek veri listesini değiştirmediği için,
 *   veri silindikten sonra adaptörü uyararak arayüzün yeni durumunu göstermesini sağlar.
 *
 * @param notes Notların tutulduğu dinamik veri listesi (MutableList<Note>)
 * @param adapter Ekranın güncellenmesini sağlayan RecyclerView Adaptörü
 * @param id Silinecek olan notun benzersiz kimliği
 * @return Not listeden silindiyse true döner
 */
fun removeNoteById(
    notes: MutableList<Note>,
    adapter: RecyclerView.Adapter<*>,
    id: Int
): Boolean {
    // 'removeAll' fonksiyonu belirtilen koşula (it.id == id) uyan elemanı/kaydı listeden kaldırır.
    // DİKKAT: Yalnızca ekrandaki satırı gizlemek veri listesini değiştirmez; bu yüzden
    // önce gerçek veri listemizden (notes) kaydı kaldırıyoruz.
    val isRemoved = notes.removeAll { note -> note.id == id }

    // Silme işleminden sonra verimiz değiştiği için adaptörü güncelliyoruz.
    if (isRemoved) {
        adapter.notifyDataSetChanged()
    }

    return isRemoved
}
