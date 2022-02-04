package domain.comand

import domain.storage.Storage

interface Operation {

    fun execute(storage: Storage)
}