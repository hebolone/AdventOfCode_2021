class BinaryTree<T> {
    private val _Datas = mutableListOf<Node<T>>()

    fun AddNode(value : T, descendants : MutableList<Node<T>> = mutableListOf()) {
        val nodeToAdd = Node(value, descendants)
        _Datas.add(nodeToAdd)
    }

    fun AddDescendant(value : T, ancestor : Node<T>) = GetNode(value)?.descendants?.add(ancestor)
    fun AddDescendant(node : Node<T>, ancestor: Node<T>) = node.descendants.add(ancestor)

    fun GetNode(value : T?) : Node<T>? = _Datas.firstOrNull { it.value == value }

    val Datas
        get() = _Datas
}

data class Node<T>(val value : T, val descendants : MutableList<Node<T>>)