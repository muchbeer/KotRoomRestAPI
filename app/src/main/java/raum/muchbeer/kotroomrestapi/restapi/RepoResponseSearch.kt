package raum.muchbeer.kotroomrestapi.restapi

import com.google.gson.annotations.SerializedName
import raum.muchbeer.kotroomrestapi.model.Repo


//this is the model
data class RepoResponseSearch(@SerializedName("total_count") val total: Int = 0,
                              @SerializedName("items") val items: List<Repo> = emptyList(),
                              val nextPage: Int? = null)