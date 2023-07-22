package com.example.bookappretrofit

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
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

        setUpRecyclerView()

        binding.retry.root.setOnClickListener {
            loadData()
        }

        binding.empty.reload.setOnClickListener {
            viewModel.getBooks()
        }

        loadData()
    }

    private fun checkInternetConnection(): Boolean {
        var cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    private fun loadData() {
        if (checkInternetConnection()) {
            viewModel.getBooks()
            viewModel.bookListLiveData.observe(this) { books ->
                if (books != null) {
                    bookAdapter.addBooks(books)
//                bookAdapter.notifyDataSetChanged()
                    checkListVisibility()
                }
            }
            binding.retry.root.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        } else {
            binding.retry.root.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        }
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
        loadData()
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        }
        bookAdapter = BookAdapter(this, viewModel, this, this)
        binding.recyclerView.adapter = bookAdapter
    }

    override fun onDeleteClick(position: Int) {
        bookAdapter.handleDeleteButton(position)
    }
}