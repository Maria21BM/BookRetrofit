package com.example.bookappretrofit

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.bookappretrofit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnDetailsClickListener, OnDeleteClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bookAdapter: BookAdapter
    private lateinit var viewModel: BooksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[BooksViewModel::class.java]

        viewModel.bookListLiveData.observe(this){
            books -> bookAdapter.addBooks(books)
            bookAdapter.notifyDataSetChanged()
            checkListVisibility()
        }

        bookAdapter = BookAdapter(this, viewModel, this, this)
        setUpRecyclerView()
//        setUpObservers()
    }

    private fun checkListVisibility() {
        if (bookAdapter.bookList.isEmpty()) {
            binding.empty.root.visibility = View.VISIBLE
        } else {
            binding.empty.root.visibility = View.GONE
        }
    }

    override fun onDetailsClick(book: BookItem) {
        val intent = Intent(this@MainActivity, DetailsActivity::class.java)
        intent.putExtra("passingObject", book)
        intent.putExtra("isFavourite", book.isFavourite)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getBooks()
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this)
        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        }
        binding.recyclerView.adapter = bookAdapter
    }

    override fun onDeleteClick(position: Int) {
        bookAdapter.handleDeleteButton(position)
    }
}