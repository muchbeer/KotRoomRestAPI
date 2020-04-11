package raum.muchbeer.kotroomrestapi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import raum.muchbeer.kotroomrestapi.model.Repo
import raum.muchbeer.kotroomrestapi.model.RepoResultSearch
import raum.muchbeer.kotroomrestapi.repository.GitRepository

class RepositoryViewModel(private val repository: GitRepository) : ViewModel() {

    companion object {
        private const val VISIBLE_THRESHOLD = 5
    }
    private val queryLiveData = MutableLiveData<String>()

    private val repoResult: LiveData<RepoResultSearch> = Transformations.map(queryLiveData) {
        repository.search(it)  }

    val repos: LiveData<List<Repo>> = Transformations.switchMap(repoResult) { it -> it.data }

    val networkErrors: LiveData<String> = Transformations.switchMap(repoResult) { it ->
        it.networkErrors
    }
    /**
     * Search a repository based on a query string.
     */
    fun searchRepo(queryString: String) {
        queryLiveData.postValue(queryString)   }

    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            val immutableQuery = lastQueryValue()
            if (immutableQuery != null) {
                repository.requestMore(immutableQuery)
            }
        }
    }

    /**
     * Get the last query value.
     */
    fun lastQueryValue(): String? = queryLiveData.value

}