package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            CatsViewModel.provideFactory(diContainer.catsService, diContainer.imageService)
        )[CatsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(
            diContainer.catsService,
            diContainer.imageService,
            diContainer.presenterScope
        )
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.apply {
                detachView()
            }
        }
        catsPresenter.cancelCoroutine()
        super.onStop()
    }
}