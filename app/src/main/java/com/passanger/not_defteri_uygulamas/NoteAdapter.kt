package com.passanger.not_defteri_uygulamas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * ADIM 2: RecyclerView için Adapter.
 *
 * YENİ KAVRAM: Callback
 * 'onDeleteClick: (id: Int) -> Unit' bir callback (geri bildirim fonksiyonudur).
 * Kullanıcı her satırdaki "Sil" düğmesine tıkladığında, bu callback tetiklenerek
 * tıklanan satırdaki notun 'id' bilgisini dışarıya bildirir.
 */
class NoteAdapter(
    private val notes: List<Note>,
    private val onDeleteClick: (id: Int) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    // Her bir satırın içindeki bileşenleri (TextView ve Sil Butonu) tutanViewHolder
    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewNote: TextView = view.findViewById(R.id.textViewNoteText)
        val buttonDelete: Button = view.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = notes[position]
        holder.textViewNote.text = currentNote.text

        // Sil düğmesine tıklandığında Callback ile silinecek notun 'id' değerini bildiriyoruz
        holder.buttonDelete.setOnClickListener {
            onDeleteClick(currentNote.id)
        }
    }
}
