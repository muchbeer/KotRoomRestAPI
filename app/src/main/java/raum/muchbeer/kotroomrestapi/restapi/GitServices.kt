package raum.muchbeer.kotroomrestapi.restapi

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import raum.muchbeer.kotroomrestapi.model.Repo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.logging.Level


private const val LOG_TAG = "GitServices"
private const val IN_QUALIFIER = "in:name,description"

fun searchRepos(
    service: GitServices,
    query: String,
    page: Int,
    itemsPerPage: Int,
    onSuccess: (repos: List<Repo>) -> Unit,
    onError: (error: String) -> Unit
) {
    Log.d(LOG_TAG, "query: $query, page: $page, itemsPerPage: $itemsPerPage")

    val apiQuery = query + IN_QUALIFIER

    service.searchRepos(apiQuery, page, itemsPerPage).enqueue(
        object : Callback<RepoResponseSearch> {
            override fun onFailure(call: Call<RepoResponseSearch>?, t: Throwable) {
                Log.d(LOG_TAG, "fail to get data")
                onError(t.message ?: "unknown error")
            }

            override fun onResponse(
                call: Call<RepoResponseSearch>?,
                response: Response<RepoResponseSearch>
            ) {
                Log.d(LOG_TAG, "got a response $response")
                if (response.isSuccessful) {
                    val repos = response.body()?.items ?: emptyList()
                    onSuccess(repos)
                } else {
                    onError(response.errorBody()?.string() ?: "Unknown error")
                }
            }
        }
    )
}


/**
 * Github API communication setup via Retrofit.
 */
interface GitServices {

    /**
     * Get repos ordered by stars.
     */
    @GET("search/repositories?sort=stars")
    fun searchRepos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): Call<RepoResponseSearch>

    companion object {
        private const val BASE_URL = "https://api.github.com/"

        fun create(): GitServices {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GitServices::class.java)
        }
    }
}