package raum.muchbeer.kotroomrestapi.model

import androidx.lifecycle.LiveData

data class RepoResultSearch(
    val data: LiveData<List<Repo>>,
    val networkErrors: LiveData<String>
)