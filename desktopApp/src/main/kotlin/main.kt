import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension
import br.com.orcacor.App
import br.com.orcacor.di.appModules
import org.koin.core.context.startKoin

fun main() = application {
    startKoin { modules(appModules) }

    Window(
        title = "Orcacor Designers",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(350, 600)
        App()
    }
}

