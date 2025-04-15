package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.CoroutineExceptionHandler

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(throwable)
    }



    companion object {
        fun provideFactory(
            catsService: CatsService,
            imageService: ImageService
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                CatsViewModel(catsService, imageService)
            }
        }
    }
}