open class BinaryTree<T> {
    protected val _Datas = mutableListOf<Node<T>>()

    fun AddNode(left : Node<T>, right : Node<T>)  = _Datas.add(Node(left, right))

    fun GetNode(value : T?) : Node<T>? = _Datas.firstOrNull { it == value }
    fun GetAncestor(value : T) : Node<T>? = _Datas.firstOrNull { it.left == value || it.right == value }
    fun GetAncestor(node : Node<T>) : Node<T>? = _Datas.firstOrNull { it.left == node.left || it.right == node.right }

    fun GetNodeLevel(value : T) : Int {
        //  Climb up to the root
        var currentNode = GetNode(value)
        var ancestor = GetAncestor(value)
        var level = 1
        while(ancestor != null) {
            currentNode = ancestor
            var ancestor = GetAncestor(currentNode)
            level ++
        }
        return level
    }
}
//----------------------------------------------------------------------------------------------------------------------
data class Node<T>(var left : Node<T>, var right : Node<T>)