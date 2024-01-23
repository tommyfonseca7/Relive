package com.example.s.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.football-data.org/v4/"
private const val API_KEY = "3376d91cfc6a4546908a824c66f4ba7b"


// Custom interceptor to add X-Auth-Token header to each request
private val authInterceptor = Interceptor { chain ->
    val request = chain.request().newBuilder()
        .addHeader("X-Auth-Token", API_KEY)
        .build()
    chain.proceed(request)
}

// Data class for representing the structure of each match in the API response
@Serializable
data class MatchInfo(
    @SerialName("id")
    val id: Long,
    @SerialName("homeTeam")
    val homeTeam: TeamInfo,
    @SerialName("awayTeam")
    val awayTeam: TeamInfo
)

@Serializable
data class TeamInfo(
    @SerialName("name")
    val name: String,
    @SerialName("crest")
    val crest: String
)

@Serializable
data class MatchesApiResponse(
    @SerialName("matches")
    val matches: List<MatchInfo>
)

@Serializable
data class TeamCrest(
    @SerialName("crest")
    val crest: String
)

@Serializable
data class MatchScore(
    @SerialName("home")
    val homeScore: Int,
    @SerialName("away")
    val awayScore: Int
)
@Serializable
data class FullScore(
    @SerialName("fullTime")
    val Score: MatchScore
)
@Serializable
data class MemoryFrontResponse(
    @SerialName("score")
    val scores: FullScore
)

// Define the Retrofit service interface
interface MatchesByCompetitionApi {
    @GET("competitions/{competitionCode}/matches")
    suspend fun getMatches(
        @Path("competitionCode") competitionCode: String,
        @Query("dateTo") dateTo: String,
        @Query("dateFrom") dateFrom: String,
        @Header("X-Auth-Token") apiKey: String = API_KEY
    ): MatchesApiResponse

    @GET("matches/{competitionCode}")
    suspend fun getInf(
        @Path("competitionCode") competitionCode: String,
        @Header("X-Auth-Token") apiKey: String = API_KEY
    ): MemoryFrontResponse

}

private val logger = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(logger)
    .build()

// Singleton object for accessing the Retrofit service
object MatchesApi {
    private val json = Json { ignoreUnknownKeys = true }

    val retrofitService: MatchesByCompetitionApi by lazy {
        Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
            .create(MatchesByCompetitionApi::class.java)
    }
}
