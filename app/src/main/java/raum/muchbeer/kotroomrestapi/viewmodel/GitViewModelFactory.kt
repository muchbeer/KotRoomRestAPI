package raum.muchbeer.kotroomrestapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import raum.muchbeer.kotroomrestapi.repository.GitRepository

class GitViewModelFactory(private val repository: GitRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepositoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RepositoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}