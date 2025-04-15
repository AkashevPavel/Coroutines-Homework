package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

data class CatsUIState(
    val fact: Fact,
    val image: Image
)

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService,
    private val presenterScope: CoroutineScope
) {

    private var _catsView: ICatsView? = null
    private var _job: Job? = null

    fun onInitComplete() {
        _job = presenterScope.launch {
            try {
                val image = imageService.getCatImage().first()
                val fact = catsService.getCatFact()

                _catsView?.populate(CatsUIState(fact, image))
            } catch (e: CancellationException) {
                throw e
            } catch (e: SocketTimeoutException) {
                showToast("Не удалось получить ответ от сервера")
            } catch (e: Exception) {
                CrashMonitor.trackWarning(e)
                e.message?.let(::showToast)
            }
        }
    }

    private fun showToast(message: String) {
        _catsView?.showToast(message)
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun cancelCoroutine() {
        _job?.run {
            if (isActive) {
                cancel()
            }
        }
    }
}