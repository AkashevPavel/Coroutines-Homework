package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val presenterScope: CoroutineScope
) {

    private var _catsView: ICatsView? = null
    private var _job: Job? = null

    fun onInitComplete() {
        _job = presenterScope.launch {
            try {
//                imageService.getCatImage().also {
//                    println("image url ${it.url}")
//                }
                catsService.getCatFact().also { fact ->
                    _catsView?.populate(fact)
                }
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