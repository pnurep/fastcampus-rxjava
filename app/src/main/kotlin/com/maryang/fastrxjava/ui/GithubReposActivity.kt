package com.maryang.fastrxjava.ui

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.maryang.fastrxjava.base.BaseApplication
import com.maryang.fastrxjava.entity.GithubRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_github_repos.*
import java.util.concurrent.TimeUnit


class GithubReposActivity : AppCompatActivity() {

    private val viewModel: GithubReposViewModel by lazy {
        GithubReposViewModel()
    }
    private val adapter: GithubReposAdapter by lazy {
        GithubReposAdapter()
    }

    private val searchSubject = PublishSubject.create<String>()
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.maryang.fastrxjava.R.layout.activity_github_repos)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = this.adapter

        refreshLayout.setOnRefreshListener { searchLoad(viewModel.searchText, false) }

        Log.d(BaseApplication.TAG, "current thread: ${Thread.currentThread().name}")

        searchText.addTextChangedListener(object : TextWatcher {

//            private val WHAT_SEARCH = 0
//            private val handler = Handler(Handler.Callback { message ->
//                when (message.what) {
//                    WHAT_SEARCH ->
//                        searchLoad(message.obj.toString(), true)
//                }
//                false
//            })

            override fun afterTextChanged(text: Editable?) {
//                handler.removeCallbacksAndMessages(null)
//                handler.sendMessageDelayed(Message().apply {
//                    what = WHAT_SEARCH
//                    obj = text.toString()
//                }, 400)

                searchSubject.onNext(text.toString())

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    private fun subjectSubscribe() {
        disposable.add(searchSubject
            .debounce(400, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                searchLoad(it, true)
            })
    }

    private fun searchLoad(search: String, showLoading: Boolean) {
        if (showLoading)
            showLoading()

        viewModel.searchGithubRepos(search)
            .subscribe(object : DisposableSingleObserver<List<GithubRepo>>() {
                override fun onSuccess(t: List<GithubRepo>) {
                    hideLoading()
                    adapter.items = t
                }

                override fun onError(e: Throwable) {
                    hideLoading()
                }
            })
    }

    private fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.GONE
        refreshLayout.isRefreshing = false
    }
}
