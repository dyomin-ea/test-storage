package domain.comand

import domain.storage.Storage

data class Put(override val key: String, val value: String) : EditOperation {

    override fun execute(storage: Storage) {
        storage[key] = value
    }
}