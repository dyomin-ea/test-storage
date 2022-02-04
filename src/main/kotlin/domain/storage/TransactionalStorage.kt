package domain.storage

import domain.comand.Commit
import domain.comand.EditOperation
import domain.comand.Put
import domain.comand.Remove
import domain.missing.value.MissingValueException
import domain.transaction.Transactional

class TransactionalStorage(
    private val source: Storage,
) : Storage, Transactional {

    override val rollback: Storage
        get() = source

    private val edits = mutableMapOf<String, EditOperation>()

    override val keys: MutableSet<String> = buildSet { addAll(source.keys) }.toMutableSet()

    override fun iterator(): Iterator<Pair<String, String>> {
        val cursor = keys.iterator()
        return object : Iterator<Pair<String, String>> {
            override fun hasNext(): Boolean = cursor.hasNext()

            override fun next(): Pair<String, String> {
                val key = cursor.next()
                val value = get(key)
                return key to value
            }
        }
    }

    override fun get(key: String): String {
        if (key !in keys) throw MissingValueException
        return (edits[key] as? Put)?.value ?: source[key]
    }

    override fun set(key: String, value: String) {
        edits[key] = Put(key, value)
        keys.add(key)
    }

    override fun remove(key: String) {
        edits[key] = Remove(key)
        keys.remove(key)
    }

    override fun createCommit(): Commit {
        return if (source is Transactional) {
            source.createCommit()
        } else {
            Commit.EMPTY
        } + Commit(edits.map { it.value })
    }
}