package com.maryang.fastrxjava.ui.user

import android.content.Context
import android.os.Bundle
import com.bumptech.glide.Glide
import com.maryang.fastrxjava.R
import com.maryang.fastrxjava.base.BaseActivity
import com.maryang.fastrxjava.data.repository.GithubRepository
import com.maryang.fastrxjava.entity.GithubRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_user.*
import org.jetbrains.anko.intentFor


class UserActivity : BaseActivity() {

    companion object {
        private const val KEY_USER = "KEY_USER"

        fun start(context: Context, user: GithubRepo.GithubRepoUser) {
            context.startActivity(
                context.intentFor<UserActivity>(
                    KEY_USER to user
                )
            )
        }
    }

    private val repository by lazy { GithubRepository() }
    private val disposable by lazy { CompositeDisposable() }
    private val listAdapter by lazy { UserRepoListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        intent.getParcelableExtra<GithubRepo.GithubRepoUser>(KEY_USER)?.let {
            supportActionBar?.run {
                title = it.userName
                setDisplayHomeAsUpEnabled(true)
            }

            bindView(it)
        }
    }

    private fun bindView(data: GithubRepo.GithubRepoUser) {

        Glide.with(this)
            .load(data.avatarUrl)
            .into(ownerImage)

        ownerName.text = data.userName

        repo_list.adapter = listAdapter

        getUserRepos(data.userName)

    }

    private fun getUserRepos(userName: String) {

        disposable += repository
            .getUserRepos(userName)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    listAdapter.data = it
                },
                onError = {
                    it.localizedMessage
                }
            )

    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

}
