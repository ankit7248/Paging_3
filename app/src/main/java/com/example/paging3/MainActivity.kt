package com.example.paging3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paging3.Paging.Loader_Adapter
import com.example.paging3.Paging.QuotePagingAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Infinite Scrolling

//    private lateinit var binding: ActivityMainBinding
    lateinit var recylerView: RecyclerView
    lateinit var adapter: QuotePagingAdapter
    lateinit var quoteViewModel: QuoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate( savedInstanceState)
        setContentView(R.layout.activity_main)

        recylerView = findViewById(R.id.quoteList)
        adapter = QuotePagingAdapter()
        quoteViewModel = ViewModelProvider(this@MainActivity)[QuoteViewModel::class.java]

        recylerView.layoutManager = LinearLayoutManager(this@MainActivity)
        recylerView.setHasFixedSize(true)
        recylerView.adapter = adapter.withLoadStateHeaderAndFooter( // we can put where we have to show progress bar in header , footer , in both
            header = Loader_Adapter(),
            footer = Loader_Adapter()
        )

        quoteViewModel.list.observe(this, Observer {  // set quoteViewmodel in RecylerView adapter
            adapter.submitData(lifecycle,it)
        })
    }
}