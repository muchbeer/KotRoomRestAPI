package raum.muchbeer.kotroomrestapi.db

import android.util.Log
import androidx.lifecycle.LiveData
import raum.muchbeer.kotroomrestapi.model.Repo
import java.util.concurrent.Executor

private const val LOG_TAG = "LocalRepository"

class LocalRepository(
    private val repoDao: RepoDao,
    private val ioExecutor: Executor) {

    /**
     * Insert a list of repos in the database, on a background thread.
     */
    fun insert(repos: List<Repo>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            Log.d(LOG_TAG, "inserting ${repos.size} repos")
            repoDao.insert(repos)
            insertFinished()
        }
    }
    fun reposByName(name: String): LiveData<List<Repo>> {
        // appending '%' so we can allow other characters to be before and after the query string
        val query = "%${name.replace(' ', '%')}%"
        return repoDao.reposByName(query)
    }


}