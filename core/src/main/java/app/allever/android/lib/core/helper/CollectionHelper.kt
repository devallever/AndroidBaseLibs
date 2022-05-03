package app.allever.android.lib.core.helper

/**
 * @author allever
 */
object CollectionHelper {
    fun <T> checkEmptyOrNull(collection: Collection<T>?): Boolean {
        return collection == null || collection.isEmpty()
    }
}