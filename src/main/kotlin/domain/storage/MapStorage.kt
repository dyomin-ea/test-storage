package domain.storage

import domain.missing.value.MissingValueException

class MapStorage : Storage {

    private val map = mutableMapOf<String, String>()

    override val keys: Set<String>
        get() = map.keys

    override fun iterator(): Iterator<Pair<String, String>> =
        map.map(Map.Entry<String, String>::toPair)
            .iterator()

    override fun get(key: String): String =
        map[key] ?: throw MissingValueException

    override fun set(key: String, value: String) {
        map[key] = value
    }

    override fun remove(key: String) {
        map.remove(key)
    }
}