@file:OptIn(kotlin.experimental.ExperimentalNativeApi::class)

import androidx.compose.ui.window.ComposeUIViewController
import br.com.orcacor.App
import br.com.orcacor.di.appModules
import kotlinx.coroutines.CoroutineExceptionHandler
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

internal val globalCoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    println("ORCACOR COROUTINE ERROR: ${throwable::class.simpleName}: ${throwable.message}")
    println(throwable.stackTraceToString())
}

private object KoinStarter {
    val init by lazy {
        println("ORCACOR: Starting Koin")
        startKoin { modules(appModules) }.also {
            println("ORCACOR: Koin started successfully")
        }
    }
}

fun MainViewController(): UIViewController {
    setUnhandledExceptionHook { throwable ->
        println("ORCACOR UNHANDLED: ${throwable::class.simpleName}: ${throwable.message}")
        println(throwable.stackTraceToString())
    }
    println("ORCACOR: MainViewController called")
    try {
        KoinStarter.init
    } catch (e: Throwable) {
        println("ORCACOR KOIN FAILED: ${e::class.simpleName}: ${e.message}")
        println(e.stackTraceToString())
        throw e
    }
    println("ORCACOR: Creating ComposeUIViewController")
    return ComposeUIViewController {
        println("ORCACOR: Composable started")
        App()
    }
}