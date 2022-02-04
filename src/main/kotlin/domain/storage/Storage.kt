package domain.storage

interface Storage : Iterable<Pair<String, String>> {

    val keys: Set<String>

    operator fun get(key: String): String

    operator fun set(key: String, value: String)

    fun remove(key: String)
}