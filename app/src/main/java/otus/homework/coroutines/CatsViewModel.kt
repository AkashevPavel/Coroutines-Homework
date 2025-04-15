package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {

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