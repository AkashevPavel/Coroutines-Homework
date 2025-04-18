package otus.homework.coroutines

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private lateinit var fact: TextView
    private lateinit var image: ImageView
    private lateinit var loadButton: Button

    private val viewModel by lazy {
        ViewModelProvider(
            this, CatsViewModel.provideFactory(diContainer.catsService, diContainer.imageService)
        )[CatsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null)
        setContentView(view)
        fact = view.findViewById(R.id.fact_textView)
        image = view.findViewById(R.id.catImage)
        loadButton = view.findViewById(R.id.button)

        configureFact()
        setListeners()
    }

    private fun setListeners() {
        loadButton.setOnClickListener {
            viewModel.load()
        }
    }

    private fun configureFact() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                fact.text = state.fact.fact
                Picasso.get().load(state.image.url).into(image)
            }
        }
    }
}