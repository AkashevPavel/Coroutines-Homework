package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DiContainer {

    private fun buildRetrofit(baseUrl: String) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val catsService by lazy { buildRetrofit("https://catfact.ninja/").create(CatsService::class.java) }

    val imageService by lazy { buildRetrofit("https://api.thecatapi.com/v1/images/").create(ImageService::class.java) }

    val presenterScope get() = CoroutineScope(CoroutineName("CatsCoroutine") + Dispatchers.Main)
}