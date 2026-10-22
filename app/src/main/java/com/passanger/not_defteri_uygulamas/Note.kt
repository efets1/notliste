package com.passanger.not_defteri_uygulamas

/**
 * Not verisini temsil eden model sınıfı.
 *
 * YENİ KAVRAM: 'id' (Kimlik)
 * Her bir notun benzersiz bir ID'si bulunur. Silinecek veya güncellenecek
 * spesifik notu listeden doğru bir şekilde belirlemek için bu 'id' kullanılır.
 */
data class Note(
    val id: Int,
    val text: String
)
