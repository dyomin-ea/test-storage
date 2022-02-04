package presentation

import domain.missing.value.MissingValueException
import presentation.res.Command.BEGIN
import presentation.res.Command.COMMIT
import presentation.res.Command.COUNT
import presentation.res.Command.DELETE
import presentation.res.Command.EXIT
import presentation.res.Command.GET
import presentation.res.Command.HELP
import presentation.res.Command.ROLLBACK
import presentation.res.Command.SET
import presentation.res.Messages
import domain.storage.Storage
import domain.storage.TransactionalStorage
import domain.transaction.Transactional
import domain.validation.onInvalid
import domain.validation.onValid
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.system.exitProcess

class StorageViewModel(
    private val initialStorage: Storage,
    private val argsValidator: ArgsValidator,
) {

    private var storage = initialStorage

    val messageFlow = MutableSharedFlow<String>()

    suspend fun onCommand(command: String) {
        val args = command.split("\\s".toRegex())
        argsValidator.validate(args)
            .onValid { parseCommand(args) }
            .onInvalid { messageFlow.emit(it.getReason()) }
    }

    private suspend fun parseCommand(command: List<String>) {
        when (command[0].uppercase()) {
            SET      -> set(command[1], command[2])
            GET      -> get(command[1])
            DELETE   -> remove(command[1])
            COUNT    -> count(command[1])
            BEGIN    -> begin()
            COMMIT   -> commit()
            ROLLBACK -> rollback()
            EXIT     -> exit()
            HELP     -> help()
        }
    }

    private fun set(key: String, value: String) {
        storage[key] = value
    }

    private suspend fun get(key: String) {
        val message = try {
            storage[key]
        } catch (e: MissingValueException) {
            Messages.MISSING_VALUE
        }
        messageFlow.emit(message)
    }

    private suspend fun count(value: String) {
        val message = storage.count { it.second == value }
        messageFlow.emit(message.toString())
    }

    private fun remove(key: String) {
        storage.remove(key)
    }

    private fun begin() {
        storage = TransactionalStorage(storage)
    }

    private suspend fun rollback() {
        val currentStorage = storage
        if (currentStorage is Transactional) {
            storage = currentStorage.rollback
        } else {
            messageFlow.emit(Messages.NO_STARTED_TRANSACTION)
        }
    }

    private suspend fun commit() {
        val currentStorage = storage
        if (currentStorage is Transactional) {
            currentStorage.createCommit().execute(initialStorage)
            storage = initialStorage
        } else {
            messageFlow.emit(Messages.NO_STARTED_TRANSACTION)
        }
    }

    private fun exit() {
        exitProcess(0)
    }

    private suspend fun help() {
        messageFlow.emit(Messages.HELP)
    }
}

