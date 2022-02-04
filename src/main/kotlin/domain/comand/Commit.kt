package domain.comand

import domain.storage.Storage

data class Commit(val operations: Collection<EditOperation>) : Operation {

    companion object {
        val EMPTY = Commit(emptyList())
    }

    override fun execute(storage: Storage) {
        operations.forEach { it.execute(storage) }
    }

    operator fun plus(other: Commit): Commit {
        return Commit(operations + other.operations)
    }
}