package raum.muchbeer.kotroomrestapi.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import raum.muchbeer.kotroomrestapi.db.LocalRepository
import raum.muchbeer.kotroomrestapi.model.RepoResultSearch
import raum.muchbeer.kotroomrestapi.restapi.GitServices
import raum.muchbeer.kotroomrestapi.restapi.searchRepos

private const val LOG_TAG = "GitRepository"

class GitRepository( private val service: GitServices, private val localData: LocalRepository) {

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 1

    // LiveData of network errors.
    private val networkErrors = MutableLiveData<String>()

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false



    fun search(query: String): RepoResultSearch {
        Log.d(LOG_TAG, "New query: $query")
        lastRequestedPage = 1
        requestAndSaveData(query)

        // Get data from the local cache
        val data = localData.reposByName(query)

        return RepoResultSearch(data, networkErrors)
    }

    fun requestMore(query: String) {  requestAndSaveData(query) }

    private fun requestAndSaveData(query: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        searchRepos(service, query, lastRequestedPage, NETWORK_PAGE_SIZE, { repos ->
            localData.insert(repos) {
                lastRequestedPage++
                isRequestInProgress = false
            }
        }, { error ->
            networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }

}