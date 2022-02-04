package presentation.res

object Messages {

    val HELP = """
            SET <key> <value>   // store the value for key
            GET <key>           // return the current value for key
            DELETE <key>        // remove the entry for key
            COUNT <value>       // return the number of keys that have the given value
            BEGIN               // start a new transaction
            COMMIT              // complete the current transaction
            ROLLBACK            // revert to state prior to BEGIN call
            EXIT                // finish the process
        """.trimIndent()

    const val MISSING_VALUE = "key not set"

    const val NO_STARTED_TRANSACTION = "no transaction"
}