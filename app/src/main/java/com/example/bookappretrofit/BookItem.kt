package com.example.bookappretrofit

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "books")
data class BookItem(
    var id: Int,
    var bookImage: String,
    var bookName: String,
    var authorImage: String,
    var authorName: String,
    var isbn: String,
    var bookType: BookType,
    //
    var imageFromCamera: Bitmap?,
    var description: String,
    var isFavourite: Boolean = false,
    var isExpanded: Boolean = false,
) : Parcelable

enum class BookType(val type: String) {
        FINANCE("Finance"),
        SELF_HELP_BOOK("Self-Help Book"),
        KIDS("Kids Book")
}

enum class BookTypeImage(val type: String, val imageResource: Int) {
        FINANCE(
                "Finance",
                R.drawable.ic_financial
        ),
        SELF_HELP_BOOK("Self-Help Book", R.drawable.ic_self_improvement), KIDS(
                "Kids Book",
                R.drawable.ic_child
        );

        companion object {
                fun getImage(type: String): Int {
                        return values().find { it.type == type }?.imageResource ?: R.drawable.ic_financial
                }
        }
}