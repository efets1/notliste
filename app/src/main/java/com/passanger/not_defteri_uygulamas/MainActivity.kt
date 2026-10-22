package com.passanger.not_defteri_uygulamas

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * ADIM 4: Ana ekran kodlarımız (MainActivity).
 * Kullanıcı etkileşimlerini alıp listeyi ve ekran durumunu güncelliyoruz.
 */
class MainActivity : AppCompatActivity() {

    // Note modellerimizi tutacağımız dinamik liste
    private val noteList = mutableListOf<Note>()

    // Her yeni nota verilecek otomatik artan ID sayacı
    private var nextNoteId = 1

    private lateinit var noteAdapter: NoteAdapter
    private lateinit var textViewEmptyList: TextView
    private lateinit var recyclerViewNotes: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Arayüz bileşenlerini koda bağlıyoruz
        val editTextNote = findViewById<EditText>(R.id.editTextNote)
        val buttonAdd = findViewById<Button>(R.id.buttonAdd)
        textViewEmptyList = findViewById(R.id.textViewEmptyList)
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes)

        // YENİ KAVRAM: Callback
        // Adaptörü oluştururken 'onDeleteClick' callback fonksiyonunu tanımlıyoruz.
        // Satırdaki "Sil" düğmesine basıldığında bu lambda bloğu çalışır ve 'noteId'yi alır.
        noteAdapter = NoteAdapter(noteList) { noteId ->
            // YENİ KAVRAM: id ve removeAll
            // NoteOperations.kt içindeki removeNoteById fonksiyonu 'removeAll { it.id == id }' kullanarak
            // yalnızca seçilen id'ye sahip notu listeden siler ve adaptörü günceller.
            if (removeNoteById(noteList, noteAdapter, noteId)) {
                Toast.makeText(this, "Not silindi", Toast.LENGTH_SHORT).show()
            }

            // Son not silindiyse veya listede not kaldıysa ekran durumunu güncelliyoruz
            updateEmptyListState()
        }

        recyclerViewNotes.layoutManager = LinearLayoutManager(this)
        recyclerViewNotes.adapter = noteAdapter

        // Başlangıçta listenin boş olma durumunu kontrol ediyoruz
        updateEmptyListState()

        // "Ekle" butonuna basıldığında yapılacak işlemler
        buttonAdd.setOnClickListener {
            val noteText = editTextNote.text.toString().trim()

            if (noteText.isNotEmpty()) {
                // Yeni Not nesnesi oluşturup benzersiz id atıyoruz
                val newNote = Note(id = nextNoteId++, text = noteText)
                noteList.add(newNote)

                // DİKKAT: Veri listesi değiştiği için adaptörü güncelliyoruz
                noteAdapter.notifyDataSetChanged()

                editTextNote.text.clear()

                // Yeni not eklendiğinde boş liste mesajını gizleyip listeyi gösteriyoruz
                updateEmptyListState()
            }
        }
    }

    /**
     * Liste boşsa "Boş liste" mesajını görünür yapar, liste doluysa RecyclerView'ı gösterir.
     */
    private fun updateEmptyListState() {
        if (noteList.isEmpty()) {
            textViewEmptyList.visibility = View.VISIBLE
            recyclerViewNotes.visibility = View.GONE
        } else {
            textViewEmptyList.visibility = View.GONE
            recyclerViewNotes.visibility = View.VISIBLE
        }
    }
}
