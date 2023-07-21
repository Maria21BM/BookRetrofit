package com.example.bookappretrofit

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.bookappretrofit.databinding.CardFinancialItemLayoutBinding
import com.example.bookappretrofit.databinding.CardKidsItemLayoutBinding
import com.example.bookappretrofit.databinding.CardSelfhelpItemLayoutBinding
import com.example.bookappretrofit.databinding.LayoutEmptyBinding
import com.squareup.picasso.Picasso

class BookAdapter(
    var context: Context,
    var bookList: ArrayList<BookItem>,
    var onDetailsClickListener: OnDetailsClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var bindingFinancial: CardFinancialItemLayoutBinding
    private lateinit var bindingSelfHelp: CardSelfhelpItemLayoutBinding
    private lateinit var bindingKids: CardKidsItemLayoutBinding
    private lateinit var bindingEmpty: LayoutEmptyBinding
    var currentPosition: Int = -1

    companion object {
        const val VIEW_TYPE_NULL = -1
        const val VIEW_TYPE_FINANCE = 0
        const val VIEW_TYPE_SELF_HELP = 1
        const val VIEW_TYPE_KIDS = 2
        private const val CAMERA_PERMISSION_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_FINANCE -> {
                bindingFinancial = CardFinancialItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                FinancialBookViewHolder(bindingFinancial)
            }
            VIEW_TYPE_SELF_HELP -> {
                bindingSelfHelp = CardSelfhelpItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                SelfHelpBookViewHolder(bindingSelfHelp)
            }
            VIEW_TYPE_KIDS -> {
                bindingKids = CardKidsItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                KidsBookViewHolder(bindingKids)
            }
            VIEW_TYPE_NULL -> {
                bindingEmpty =
                    LayoutEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolderEmpty(bindingEmpty)
            }
            else -> {
                throw IllegalArgumentException("Invalid view type: $viewType")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val bookItem = bookList[position]
        when(holder) {
            is FinancialBookViewHolder -> holder.bind(bookItem)
            is SelfHelpBookViewHolder -> holder.bind(bookItem)
            is KidsBookViewHolder -> holder.bind(bookItem)
            else -> {
                throw IllegalArgumentException("Invalid view holder: ${holder.javaClass.simpleName}")
            }
        }
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (bookList.isEmpty()) {
            VIEW_TYPE_NULL
        } else {
            Log.d("Maria", "${bookList[position]} - $position")
            when (bookList[position].bookType) {
                BookType.FINANCE -> VIEW_TYPE_FINANCE
                BookType.SELF_HELP_BOOK -> VIEW_TYPE_SELF_HELP
                BookType.KIDS -> VIEW_TYPE_KIDS
            }
        }
    }

    inner class FinancialBookViewHolder(bindingFinancial: CardFinancialItemLayoutBinding) :
        RecyclerView.ViewHolder(bindingFinancial.root) {
        fun bind(book: BookItem) {
            Picasso.get().load(book.bookImage).into(bindingFinancial.bookImage)
            bindingFinancial.bookName.text = book.bookName
            Picasso.get().load(book.authorImage).into(bindingFinancial.authorImage)
            bindingFinancial.authorName.text = book.authorName
            bindingFinancial.isbn.text = book.isbn
            bindingFinancial.bookType.text = book.bookType.type

            // Set Drawable Image inside Text View
            bindingFinancial.bookType.setCompoundDrawablesWithIntrinsicBounds(
                BookTypeImage.getImage(
                    book.bookType.type
                ), 0, 0, 0
            )
            bindingFinancial.favourite.isChecked = book.isFavourite

            // Favourite
            bindingFinancial.favourite.setOnCheckedChangeListener { _, isChecked ->
                book.isFavourite = isChecked
            }

            bindingFinancial.description.text = book.description

            // Set the expand button
            bindingFinancial.bookName.maxLines = if (book.isExpanded) Int.MAX_VALUE else 1
            bindingFinancial.bookName.setOnClickListener {
                handleExpandButton(layoutPosition)
            }

            // Delete Button click listener
            bindingFinancial.delete.setOnClickListener {
                handleDeleteButton(layoutPosition)
            }

            // Share Button click listener
            bindingFinancial.bookName.setOnClickListener {
                handleShareButtonClick(bindingFinancial.bookName)
            }

            // Open Camera
        }
    }

    inner class SelfHelpBookViewHolder(bindingSelfHelp: CardSelfhelpItemLayoutBinding) :
        RecyclerView.ViewHolder(bindingSelfHelp.root) {
        fun bind(book: BookItem) {
            Picasso.get().load(book.bookImage).into(bindingSelfHelp.bookImage)
            bindingSelfHelp.bookName.text = book.bookName
            Picasso.get().load(book.authorImage).into(bindingSelfHelp.authorImage)
            bindingSelfHelp.authorName.text = book.authorName
            bindingSelfHelp.isbn.text = book.isbn
            bindingSelfHelp.bookType.text = book.bookType.type

            // Set Drawable Image inside Text View
            bindingSelfHelp.bookType.setCompoundDrawablesWithIntrinsicBounds(
                BookTypeImage.getImage(
                    book.bookType.type
                ), 0, 0, 0
            )
            bindingSelfHelp.favourite.isChecked = book.isFavourite

            // Favourite
            bindingSelfHelp.favourite.setOnCheckedChangeListener { _, isChecked ->
                book.isFavourite = isChecked
            }

            bindingSelfHelp.description.text = book.description

            // Set the expand button
            bindingSelfHelp.bookName.maxLines = if (book.isExpanded) Int.MAX_VALUE else 1
            bindingSelfHelp.bookName.setOnClickListener {
                handleExpandButton(layoutPosition)
            }

            // Delete Button click listener
            bindingSelfHelp.delete.setOnClickListener {
                handleDeleteButton(layoutPosition)
            }

            // Share Button click listener
            bindingSelfHelp.bookName.setOnClickListener {
                handleShareButtonClick(bindingSelfHelp.bookName)
            }

            // Open Camera
        }
    }

    inner class KidsBookViewHolder(bindingKids: CardKidsItemLayoutBinding) :
        RecyclerView.ViewHolder(bindingKids.root) {
        fun bind(book: BookItem) {
            Picasso.get().load(book.bookImage).into(bindingKids.bookImage)
            bindingKids.bookName.text = book.bookName
            Picasso.get().load(book.authorImage).into(bindingKids.authorImage)
            bindingKids.authorName.text = book.authorName
            bindingKids.isbn.text = book.isbn
            bindingKids.bookType.text = book.bookType.type

            // Set Drawable Image inside Text View
            bindingKids.bookType.setCompoundDrawablesWithIntrinsicBounds(
                BookTypeImage.getImage(
                    book.bookType.type
                ), 0, 0, 0
            )
            bindingKids.favourite.isChecked = book.isFavourite

            // Favourite
            bindingKids.favourite.setOnCheckedChangeListener { _, isChecked ->
                book.isFavourite = isChecked
            }

            bindingKids.description.text = book.description

            // Set the expand button
            bindingKids.bookName.maxLines = if (book.isExpanded) Int.MAX_VALUE else 1
            bindingKids.bookName.setOnClickListener {
                handleExpandButton(layoutPosition)
            }

            // Delete Button click listener
            bindingKids.delete.setOnClickListener {
                handleDeleteButton(layoutPosition)
            }

            // Share Button click listener
            bindingKids.bookName.setOnClickListener {
                handleShareButtonClick(bindingKids.bookName)
            }

            // Open Camera
        }
    }

    inner class ViewHolderEmpty(bindingEmpty: LayoutEmptyBinding) : RecyclerView.ViewHolder(bindingEmpty.root)

    fun handleShareButtonClick(bookName: TextView){
        val title = bookName.text
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, title)

        val chooser = Intent.createChooser(intent, "Share using...")
        context.startActivity(chooser)
    }

    fun handleDeleteButton(position: Int){
        bookList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, bookList.size)
    }

    fun handleExpandButton(position: Int){
        bookList[position].isExpanded = !bookList[position].isExpanded
        notifyItemChanged(position)
    }

}


