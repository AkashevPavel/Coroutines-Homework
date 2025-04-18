package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(throwable)
    }

    private val _uiState = MutableStateFlow<CatsUIState?>(null)
    val uiState = _uiState.onStart { load() }
        .filterNotNull()
        .shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            replay = 1
        )

    fun load() {
        viewModelScope.launch(exceptionHandler) {
            val fact = async { catsService.getCatFact() }
            val image = async { imageService.getCatImage() }

            _uiState.emit(
                CatsUIState(
                    fact = fact.await(),
                    image = image.await()[0]
                )
            )
        }
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