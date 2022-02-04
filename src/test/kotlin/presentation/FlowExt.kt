package presentation

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


suspend fun <T> Flow<T>.assert(body: suspend ContainerAssertions<T>.() -> Unit) {
    coroutineScope {
        val list = mutableListOf<T>()
        val containerAssertions = ContainerAssertions(list)
        val job = onEach { list.add(it) }.launchIn(this)
        launch {
            containerAssertions.body()
            job.cancelAndJoin()
        }
    }
}