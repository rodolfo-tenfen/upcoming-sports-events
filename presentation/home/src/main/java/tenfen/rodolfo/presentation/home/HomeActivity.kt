package tenfen.rodolfo.presentation.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.koin.androidx.compose.KoinAndroidContext
import tenfen.rodolfo.presentation.theme.R
import tenfen.rodolfo.presentation.theme.UpcomingSportsEventsTheme

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            KoinAndroidContext {
                UpcomingSportsEventsTheme {
                    HomeActivityContent()
                }
            }
        }
    }
}

@Composable
private fun HomeActivityContent() {
    Surface(modifier = Modifier.fillMaxSize()) {
        HomeScreen(stringResource(R.string.app_name))
    }
}
