package com.di.dithub.feature.search

import com.di.dithub.base.BaseLiveData
import com.di.dithub.base.BaseViewModel
import com.di.dithub.client.apis.GitApi
import com.di.dithub.model.response.RepoInfo

class SearchViewModel(private val gitApi: GitApi) : BaseViewModel() {

    private var currPage: Int = 1
    var hasMore: Boolean = true
    private val tempResult = mutableListOf<RepoInfo>()

    private val _reposResult = BaseLiveData<MutableList<RepoInfo>>()
    val repoResult: BaseLiveData<MutableList<RepoInfo>>
        get() = _reposResult

    fun fetchSearch(key: String) {
        currPage = 1
        tempResult.clear()
        loadMore(key)
    }

    fun loadMore(key: String) {
        request {
            val result = gitApi.searchRepos(key = key, currPage = currPage)
            tempResult.addAll(result.items)
            hasMore = tempResult.size < result.total
            if (hasMore) currPage++
            _reposResult.update(tempResult)
        }
    }

    override fun onCleared() {
        super.onCleared()
        tempResult.clear()
    }
}