package domain.transaction

import domain.comand.Commit
import domain.storage.Storage

interface Transactional {
    val rollback: Storage

    fun createCommit(): Commit
}