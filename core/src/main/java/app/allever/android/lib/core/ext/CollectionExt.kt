package app.allever.android.lib.core.ext

fun <T> MutableList<T>.removeItemIf(needRemove: (item: T) -> Boolean) {
    val iterator: MutableIterator<T> = this.iterator()
    while (iterator.hasNext()) {
        val bean = iterator.next()
        if (needRemove(bean)) {
            iterator.remove()
        }
    }
}