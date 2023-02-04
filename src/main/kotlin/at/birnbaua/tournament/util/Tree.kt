package at.birnbaua.tournament.util

class Tree<K,V> {

    class TreeEntry<K,V>(
        var children: Set<K> = mutableSetOf(),
        var values: List<V> = mutableListOf()
    )

    var desc: String? = null
    private var entries: MutableMap<K?,TreeEntry<K,V>> = mutableMapOf()

    fun addOrReplace(key: K?, children: Set<K>, values: List<V>) {
        if(this.entries.isEmpty() && key != null) throw IllegalArgumentException("Tree must contain root node with key == null!")
        this.entries[key] = TreeEntry(children,values)
    }
    fun remove(key: K?) : Boolean {
        if(this.entries.size > 1 && key == null) throw IllegalArgumentException("Tree must contain root node with key == null if other nodes are present!")
        return this.entries.remove(key) != null
    }
    fun getValuesOf(key: K?) : List<V> { return this.entries[key]!!.values }

    fun getAllValuesOf(key: K?) : List<V> {
        if(this.entries[key]!!.children.isNotEmpty()) {
            return this.entries[key]!!.children.flatMap { getAllValuesOf(it) }.plus(this.entries[key]!!.values)
        }
        return this.entries[key]!!.values
    }

    fun getAllLeafValuesOf(key: K?) : List<V> {
        if(this.entries[key]!!.children.isNotEmpty()) {
            return this.entries[key]!!.children.flatMap { getAllLeafValuesOf(it) }
        }
        return this.entries[key]!!.values
    }

    fun getAll() : List<V> {
        if(isEmpty()) return listOf()
        return getAllValuesOf(null)
    }

    fun isEmpty() : Boolean { return this.entries.isEmpty() }
    fun isNotEmpty() : Boolean { return this.entries.isNotEmpty() }

}