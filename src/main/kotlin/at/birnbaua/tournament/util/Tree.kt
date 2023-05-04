package at.birnbaua.tournament.util

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Field


class Tree<K,V> {

    data class TreeEntry<K,V> constructor(
        var children: Set<K> = mutableSetOf(),
        var values: List<V> = mutableListOf()
    )

    var desc: String? = null


    @JsonSerialize(using = StringMapSerializer::class)
    var entries: MutableMap<K?,TreeEntry<K,V>> = mutableMapOf()

    @JsonIgnore
    fun addOrReplace(key: K?, children: Set<K>, values: List<V>) {
        if ((key != null) && (key.toString().lowercase() == "null")) throw java.lang.IllegalArgumentException("Key must not have name \"null\"!")
        if(this.entries.isEmpty() && key != null) throw IllegalArgumentException("Tree must contain root node with key == null!")
        this.entries[key] = TreeEntry(children,values)
    }
    @JsonIgnore
    fun remove(key: K?) : Boolean {
        if(this.entries.size > 1 && key == null) throw IllegalArgumentException("Tree must contain root node with key == null if other nodes are present!")
        return this.entries.remove(key) != null
    }
    @JsonIgnore
    fun getValuesOf(key: K?) : List<V> { return this.entries[key]!!.values }
    @JsonIgnore
    private fun getAllValuesOf(key: K?) : List<V> {
        if(this.entries[key]!!.children.isNotEmpty()) {
            return this.entries[key]!!.children.flatMap { getAllValuesOf(it) }.plus(this.entries[key]!!.values)
        }
        return this.entries[key]!!.values
    }
    @JsonIgnore
    fun getFirstLeafNode(key: K?) : TreeEntry<K,V> {
        if(exists(key).not()) throw NoSuchElementException("This tree does not contain a node with the key: $key")
        if(this.entries[key]!!.children.isNotEmpty()) {
            return getFirstLeafNode(this.entries[key]!!.children.first())
        }
        return this.entries[key]!!
    }
    @JsonIgnore
    fun getAllLeafNodes(key: K?) : List<TreeEntry<K,V>> {
        if(exists(key).not()) throw NoSuchElementException("This tree does not contain a node with the key: $key")
        if(this.entries[key]!!.children.isNotEmpty()) {
            return this.entries[key]!!.children.flatMap { getAllLeafNodes(it) }.toList()
        }
        return listOf(this.entries[key]!!)
    }
    @JsonIgnore
    fun exists(key: K?) : Boolean {
        return this.entries.containsKey(key)
    }
    @JsonIgnore
    fun getAllLeafValuesOf(key: K?) : List<V> {
        if(this.entries[key]!!.children.isNotEmpty()) {
            return this.entries[key]!!.children.flatMap { getAllLeafValuesOf(it) }
        }
        return this.entries[key]!!.values
    }
    @JsonIgnore
    private fun getAllValues() : List<V> {
        if(isEmpty()) return listOf()
        return getAllValuesOf(null)
    }
    @JsonIgnore
    fun getAllNodes() : List<TreeEntry<K,V>> {
        return getAllChildren(null)
    }
    @JsonIgnore
    fun getAllChildren(key: K?) : List<TreeEntry<K,V>> {
        if(this.entries.containsKey(key).not()) {
            return listOf()
        }
        if(this.entries[key]!!.children.isNotEmpty()) {
            return this.entries[key]!!.children.flatMap { getAllChildren(it) }
        }
        return listOf(this.entries[key]!!)
    }

    @JsonIgnore
    fun isEmpty() : Boolean { return this.entries.isEmpty() }
    @JsonIgnore
    fun isNotEmpty() : Boolean { return this.entries.isNotEmpty() }

}