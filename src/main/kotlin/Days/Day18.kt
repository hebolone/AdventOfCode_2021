package Days

class Day18 : AlgosBase() {
    override fun Basic(input : MutableList<String>) : Int {
        val snailFishes = ParseInput(mutableListOf("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]"))
        snailFishes.forEach { it.Actions() }

//        var addeSnailFishes = Group("[[1,2],3]") + Group("[3,4]") + Group("[7,7]")
//        println("$addeSnailFishes")

        return -1
    }

    override fun Advanced(input : MutableList<String>) : Int {
        return -1
    }

    //region Private
    private enum class TPOSITION { LEFT, RIGHT }
    private enum class TSNAILTYPE { DESCENDANT, INTEGER }
    private enum class TACTION { EXPLODE, SPLIT }
    private data class SnailValue(val snailType : TSNAILTYPE, var value : Any)
    private data class ActionToDo(var action : TACTION, var snailFish: SnailFish)
    private data class SnailFish(val ancestor : SnailFish?, var left : SnailValue? = null, var right : SnailValue? = null) {
        override fun toString(): String = "[${left?.value.toString()},${right?.value.toString()}]"
        val leftAsInt : Int
            get() = (left?.value as? Int) ?: -1
        val rightAsInt : Int
            get() = (right?.value as? Int) ?: -1
        fun sum(position : TPOSITION, value : Int) =
            when(position) {
                TPOSITION.LEFT -> left = SnailValue(TSNAILTYPE.INTEGER, leftAsInt + value)
                TPOSITION.RIGHT -> right = SnailValue(TSNAILTYPE.INTEGER, rightAsInt + value)
            }

        fun containsInteger() : Boolean = left?.snailType == TSNAILTYPE.INTEGER || right?.snailType == TSNAILTYPE.INTEGER

        fun getPositionFromAncestor(snailFish : SnailFish) : TPOSITION = if(ancestor?.left?.value == snailFish) TPOSITION.LEFT else TPOSITION.RIGHT
    }
    private class Group(initString : String) {
        private data class Position(val index : Int, val value : Int, val position : TPOSITION, val snailFish: SnailFish)
        private var _SnailFishes : MutableList<SnailFish> = mutableListOf<SnailFish>()
        private var _Positions : MutableList<Position> = mutableListOf<Position>()

        init {
            CalculateSnailFishes(initString)
        }

        private fun CalculateSnailFishes(definition : String) {
            _SnailFishes.clear()
            _Positions.clear()
            var position = TPOSITION.LEFT
            var current: SnailFish? = null
            definition.forEach { c ->
                run {
                    when (c) {
                        '[' -> {
                            //  Create a new snailfish
                            val newSnail = SnailFish(current)
                            when (position) {
                                TPOSITION.LEFT -> current?.left = SnailValue(TSNAILTYPE.DESCENDANT, newSnail)
                                TPOSITION.RIGHT -> current?.right = SnailValue(TSNAILTYPE.DESCENDANT, newSnail)
                            }
                            current = newSnail
                            _SnailFishes.add(current!!)
                            position = TPOSITION.LEFT
                        }

                        in ('0'..'9') -> {
                            when (position) {
                                TPOSITION.LEFT -> current?.left = SnailValue(TSNAILTYPE.INTEGER, c.digitToInt())
                                TPOSITION.RIGHT -> current?.right = SnailValue(TSNAILTYPE.INTEGER, c.digitToInt())
                            }
                            //  Add to position list
                            _Positions.add(Position(_Positions.size + 1, c.digitToInt(), position, current !!))
                        }

                        ',' -> {
                            //  Change left to right
                            position = TPOSITION.RIGHT
                        }

                        ']' -> {
                            //  Set the new current: the father of this one
                            current = current?.ancestor
                        }
                    }
                }
            }
        }

        fun Actions() {
            //  Search for condition of explosion:
            //  1 - inside 4 pair (explosion)
            //  2 - one number is 10 or more (split)
            println("SnailFish: $this")
            var actionToDo : ActionToDo?
          do {
                actionToDo = null
                run lit@ {
                    _SnailFishes.forEach {
                        var explosion = it.GetNumberOfAncestors() >= 4
                        if(explosion) {
                            actionToDo = ActionToDo(TACTION.EXPLODE, it)

                            println("Going to explode: $it")

                            return@lit
                        }
//                        var split = it.leftAsInt >= 10 || it.rightAsInt >= 10
//                        if(split) {
//                            actionToDo = ActionToDo(TACTION.SPLIT, it)
//                            return@lit
//                        }
                    }
                }

                when(actionToDo?.action) {
                    TACTION.EXPLODE -> Explode(actionToDo?.snailFish !!)
                    TACTION.SPLIT -> Split(actionToDo?.snailFish !!)
                }

                println("Explosion: $this")
            } while(actionToDo != null)
        }

        private fun Explode(snailFish : SnailFish) {
            //  Search leftmost
            val leftMost = SearchLeftSide(snailFish)
            leftMost?.snailFish?.sum(leftMost.position, snailFish.leftAsInt)

            //  Search rightmost
            val rightMost = SearchRightSide(snailFish)
            rightMost?.snailFish?.sum(rightMost.position, snailFish.rightAsInt)

            //  Set my parents value as 0
            val position = snailFish.getPositionFromAncestor(snailFish)
            val newSnailValue = SnailValue(TSNAILTYPE.INTEGER, 0)
            when(position) {
                TPOSITION.LEFT -> snailFish.ancestor?.left = newSnailValue
                TPOSITION.RIGHT -> snailFish.ancestor?.right = newSnailValue
            }

            //  Have to delete current snailfish, so let's recalculate
            _SnailFishes.remove(snailFish)

            //  Recalculating indexes
            CalculateSnailFishes(this.toString())
        }

        private fun Split(snailFish : SnailFish) {
//            //  Is this left or right?
//            var current = 0
//            var position = TPOSITION.LEFT
//            if(this.leftAsInt >= 10) {
//                current = leftAsInt
//            } else {
//                current = rightAsInt
//                position = TPOSITION.RIGHT
//            }
//
//            var leftValue = current / 2
//            val newSnailFish = SnailFish(
//                this,
//                SnailValue(TSNAILTYPE.INTEGER, leftValue),
//                SnailValue(TSNAILTYPE.INTEGER, current - leftValue)
//            )
//            when(position) {
//                TPOSITION.LEFT -> this.left = SnailValue(TSNAILTYPE.DESCENDANT, newSnailFish)
//                TPOSITION.RIGHT -> this.right = SnailValue(TSNAILTYPE.DESCENDANT, newSnailFish)
//            }
//
//            _SnailFishes.add(newSnailFish)
        }

        private fun SearchLeftSide(snailFish : SnailFish) : Position? {
            //  Starting from current snail fish I have to search inside IntegerPositions list
            var currentPosition : Position? = _Positions.first { it.snailFish == snailFish }
            var found = false

            while(!found && currentPosition != null) {
                var current = currentPosition.index - 1
                currentPosition = _Positions.firstOrNull { it.index == current }
                found = currentPosition?.snailFish?.containsInteger() ?: false
            }
            return if(found) currentPosition else null
        }

        private fun SearchRightSide(snailFish : SnailFish) : Position? {
            var currentPosition : Position? = _Positions.first { it.snailFish == snailFish }
            var found = false
            //currentPosition = _Positions.firstOrNull { (it.index > currentPosition?.index ?: -1) && (it.snailFish != currentPosition?.snailFish) }

            while(!found && currentPosition != null) {
                var current = currentPosition.index + 1
                currentPosition = _Positions.firstOrNull { (it.index > currentPosition?.index ?: -1) && (it.snailFish != currentPosition?.snailFish) }
                found = currentPosition?.snailFish?.containsInteger() ?: false
            }
            return if(found) currentPosition else null
        }

        private fun SnailFish.GetNumberOfAncestors() : Int {
            var retValue = 0
            var current = this
            while(current.ancestor != null) {
                retValue ++
                current = current.ancestor!!
            }
            return retValue
        }

        operator fun plus(added : Group) : Group = Group("[$this,$added]")

        override fun toString(): String = _SnailFishes.single { it.ancestor == null }.toString()
    }

    private fun ParseInput(lines : MutableList<String>) : MutableList<Group> {
        val retValue = mutableListOf<Group>()
        lines.filter { ! it.startsWith('-') }.forEach {
            var currentGroup = Group(it)
            retValue.add(currentGroup)
        }
        return retValue
    }
    //endregion
}