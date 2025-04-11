package otus.homework.coroutines

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    suspend fun onInitComplete() {
        catsService.getCatFact().also { fact ->
            _catsView?.populate(fact)
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}