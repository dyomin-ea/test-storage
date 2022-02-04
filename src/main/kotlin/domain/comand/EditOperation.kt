package domain.comand

sealed interface EditOperation : Operation {

    val key: String
}