package main

import domain.storage.MapStorage
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import presentation.ArgsValidator
import presentation.StorageViewModel
import java.io.InputStream
import java.io.OutputStream

suspend fun main() {
    run(System.`in`, System.out)
}

suspend fun run(inputStream: InputStream, outputStream: OutputStream) {
    coroutineScope {
        val viewModel = StorageViewModel(
            MapStorage(),
            ArgsValidator()
        )

        viewModel.messageFlow
            .writeTo(outputStream)
            .launchIn(this)

        inputStream.asFlow()
            .onEach { viewModel.onCommand(it) }
            .launchIn(this)
    }
}

fun Flow<String>.writeTo(outputStream: OutputStream): Flow<String> {
    val writer = outputStream.writer()
    return this
        .onCompletion { writer.close() }
        .onEach {
            with(writer) {
                write(it)
                appendLine()
                flush()
            }
        }
}

fun InputStream.asFlow(): Flow<String> {
    val reader = bufferedReader()
    return reader.lineSequence()
        .asFlow()
        .onCompletion { reader.close() }
}