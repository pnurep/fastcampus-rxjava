package com.maryang.fastrxjava.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.maryang.fastrxjava.R
import com.maryang.fastrxjava.entity.GithubRepo
import com.maryang.fastrxjava.entity.User
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.android.synthetic.main.activity_github_repos.*


class GithubReposActivity : AppCompatActivity() {

    private val disposable = CompositeDisposable()

    private val viewModel: GithubReposViewModel by lazy {
        GithubReposViewModel()
    }
    private val adapter: GithubReposAdapter by lazy {
        GithubReposAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_repos)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = this.adapter

        refreshLayout.setOnRefreshListener { load() }

        load(true)
    }

    private fun load(showLoading: Boolean = false) {
        if (showLoading)
            showLoading()

        disposable.add(viewModel
            .getGithubRepos()
            .subscribeWith(object : DisposableSingleObserver<List<GithubRepo>>() {
                override fun onSuccess(t: List<GithubRepo>) {
                    hideLoading()
                    adapter.items = t
                }

                override fun onError(e: Throwable) {
                    hideLoading()
                }
            }))
    }

    private fun load2() {
        disposable.add(viewModel.getUser()
            .subscribeWith(object : DisposableMaybeObserver<User>() {
                override fun onSuccess(t: User) {  //null 이 아닌것만 온다는 보장이 있다.

                }

                override fun onComplete() { //null 이 오면 호출된다.

                }

                override fun onError(e: Throwable) { //에러가 나면 호출

                }
            }))
    }

    private fun load3() {
        disposable.add(viewModel.updateUser()
            .subscribeWith(object : DisposableCompletableObserver() {
                override fun onComplete() {

                }

                override fun onError(e: Throwable) {

                }
            }))
    }

    private fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.GONE
        refreshLayout.isRefreshing = false
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

}