package com.example.bookappretrofit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.bookappretrofit.databinding.ActivityMainBinding
import com.example.bookappretrofit.databinding.LayoutEmptyBinding
import retrofit2.Response

class MainActivity : AppCompatActivity(), OnDetailsClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bookList: ArrayList<BookItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookList = arrayListOf(
            BookItem(0, "https://cdn4.libris.ro/img/pozeprod/10845/10844464-1.jpg","The Little Book of Common Sense Investing", "https://ei.marketwatch.com/Multimedia/2018/11/29/Photos/ZG/MW-GZ333_bogle__20181129124951_ZG.jpg?uuid=2bfdb2e6-f3ff-11e8-a4e5-ac162d7bc1f7", "John C. Bogle", "9780060555665",
                BookType.FINANCE, null, "Description"
            ),
            BookItem(1, "https://m.media-amazon.com/images/I/719njS5bQhL._SY522_.jpg","The Little Prince", "https://upload.wikimedia.org/wikipedia/commons/7/7f/11exupery-inline1-500.jpg", "Antoine De Saint-Exupery", "9780060555665",
                BookType.KIDS, null, "Description"
            ),
            BookItem(2, "https://libris.to/media/jacket/04008460_intelligent-investor.jpg","The Intelligent Investor", "https://upload.wikimedia.org/wikipedia/commons/2/2a/Benjamin_Graham_%281894-1976%29_portrait_on_23_March_1950.jpg", "Benjamin Grahm", "9780060555665",
                BookType.FINANCE, null, "Description"
            ),
            BookItem(3, "https://libris.to/media/jacket/21063216_happiness-project-the-10th-anniversary-edition.jpg","The Happiness Project", "https://gretchenrubin.com/wp-content/uploads/elementor/thumbs/gretche-rubin_hero_homepage-q2953oqvxvbipoj8k2ayi78po84bdyu301or10q67c.jpg", "Gretchen Rubin", "9780061583254",
                BookType.SELF_HELP_BOOK, null, "Description"
            ),
            BookItem(4, "https://cdn4.libris.ro/img/pozeprod/1207/1206116-1.jpg","The Little Book That Still beats The Market", "https://www.worldtopinvestors.com/wp-content/uploads/2018/01/Joel-Greenblatt-world-top-investors.jpg", "Joel Greenblatt", "9780060555665",
                BookType.FINANCE, null, "Description"
            ),
            BookItem(5, "https://cdn4.libris.ro/img/pozeprod/10860/10859251-1.jpg","The 5 Love Languages", "https://upload.wikimedia.org/wikipedia/commons/5/51/Gary_D._Chapman.jpg", "Gary Chapman", "9780789919779",
                BookType.SELF_HELP_BOOK, null, "Description"
            )

        )

        var bookAdapter = BookAdapter(this, bookList, this)
        binding.recyclerView.adapter = bookAdapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        }

        val retrofitService = RetrofitInstance
            .getRetrofitInstance()
            .create(BookService::class.java)

        val responseLiveData: LiveData<Response<BookList>> = liveData {
            val response = retrofitService.getBooks()
            emit(response)
        }

        responseLiveData.observe(this, Observer { response ->
            if (response.isSuccessful) {
                val bookListResponse = response.body()
                if (bookListResponse != null) {
                    bookList.clear()
                    this.bookList.addAll(bookListResponse)
                    bookAdapter.notifyDataSetChanged()
                }
            }else{
                binding.empty.root.visibility = View.VISIBLE
            }
        })
    }

    override fun onDetailsClick(book: BookItem) {
        val intent = Intent(this@MainActivity, DetailsActivity::class.java)
        intent.putExtra("passingObject", book)
        intent.putExtra("isFavourite", book.isFavourite)
        startActivity(intent)
    }
}