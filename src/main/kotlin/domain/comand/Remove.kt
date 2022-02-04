package domain.comand

import domain.storage.Storage

data class Remove(override val key: String) : EditOperation {

    override fun execute(storage: Storage) {
        storage.remove(key)
    }
}