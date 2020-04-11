package raum.muchbeer.kotroomrestapi

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import raum.muchbeer.kotroomrestapi.db.LocalRepository
import raum.muchbeer.kotroomrestapi.db.RepoDatabase
import raum.muchbeer.kotroomrestapi.repository.GitRepository
import raum.muchbeer.kotroomrestapi.restapi.GitServices
import raum.muchbeer.kotroomrestapi.viewmodel.GitViewModelFactory
import java.util.concurrent.Executors

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {
    /**
     * Creates an instance of [LocalRepository] based on the database DAO.
     */
    private fun provideCache(context: Context): LocalRepository {
        val database = RepoDatabase.getInstance(context)
        return LocalRepository(database.reposDao(), Executors.newSingleThreadExecutor())
    }

    private fun provideGithubRepository(context: Context): GitRepository {
        return GitRepository(GitServices.create(), provideCache(context))
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return GitViewModelFactory(provideGithubRepository(context))
    }
}