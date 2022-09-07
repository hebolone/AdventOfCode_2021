package Days

class Day18 : AlgosBase() {
    override fun Basic(input : MutableList<String>) : Int {
        val snailFishes = ParseInput(input)
        snailFishes.forEach { it.Actions() }
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
        fun getSnailValue(snailFish : SnailFish) : TPOSITION = if(ancestor?.left?.value == snailFish) TPOSITION.LEFT else TPOSITION.RIGHT
    }
    private class Group {
        private val _SnailFishes : MutableList<SnailFish> = mutableListOf()
        val snailfishes : MutableList<SnailFish> = _SnailFishes
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
                            return@lit
                        }
                        var split = it.leftAsInt >= 10 || it.rightAsInt >= 10
                        if(split) {
                            actionToDo = ActionToDo(TACTION.SPLIT, it)
                            return@lit
                        }
                    }
                }

                when(actionToDo?.action) {
                    TACTION.EXPLODE -> actionToDo?.snailFish?.Explode()
                    TACTION.SPLIT -> actionToDo?.snailFish?.Split()
                }

                println("SnailFish: $this")
            } while(actionToDo != null)
        }

        private fun SnailFish.Explode() {
            //  Search leftmost
            val leftMost = this.SearchLeftMost()
            leftMost?.sum(TPOSITION.LEFT, this.leftAsInt)

            //  Search rightmost
            val rightMost = this.SearchRightMost()
            rightMost?.sum(TPOSITION.RIGHT, this.rightAsInt)

            //  Set my parents value as 0
            val position = this.getSnailValue(this)
            val newSnailValue = SnailValue(TSNAILTYPE.INTEGER, 0)
            when(position) {
                TPOSITION.LEFT -> this.ancestor?.left = newSnailValue
                TPOSITION.RIGHT -> this.ancestor?.right = newSnailValue
            }

            //  Delete current snailfish (it can't have no descendants)
            _SnailFishes.remove(this)
        }

        private fun SnailFish.Split() {
            //  Is this left or right?
            var current = 0
            var position = TPOSITION.LEFT
            if(this.leftAsInt >= 10) {
                current = leftAsInt
            } else {
                current = rightAsInt
                position = TPOSITION.RIGHT
            }

            var leftValue = current / 2
            val newSnailFish = SnailFish(
                this,
                SnailValue(TSNAILTYPE.INTEGER, leftValue),
                SnailValue(TSNAILTYPE.INTEGER, current - leftValue)
            )
            when(position) {
                TPOSITION.LEFT -> this.left = SnailValue(TSNAILTYPE.DESCENDANT, newSnailFish)
                TPOSITION.RIGHT -> this.right = SnailValue(TSNAILTYPE.DESCENDANT, newSnailFish)
            }

            _SnailFishes.add(newSnailFish)
        }

        private fun SnailFish.SearchLeftMost() : SnailFish? {
            var current = this
            while(current.ancestor != null && current.ancestor?.left?.snailType != TSNAILTYPE.INTEGER) {
                current = current.ancestor!!
            }
            return current.ancestor
        }

        private fun SnailFish.SearchRightMost() : SnailFish? {
            var current = this
            while(current.ancestor != null && current.ancestor?.right?.snailType != TSNAILTYPE.INTEGER) {
                current = current.ancestor!!
            }
            return current.ancestor
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

        override fun toString(): String = snailfishes.first { it.ancestor == null }.toString()
    }

    private fun ParseInput(lines : MutableList<String>) : MutableList<Group> {
        val retValue = mutableListOf<Group>()
        lines.filter { ! it.startsWith('-') }.forEach {
            var currentGroup = Group()
            var position = TPOSITION.LEFT
            var current : SnailFish? = null
            it.forEach { c -> run {
                when (c) {
                    '[' -> {
                        //  Create a new snailfish
                        val newSnail = SnailFish(current)
                        when(position) {
                            TPOSITION.LEFT -> current?.left = SnailValue(TSNAILTYPE.DESCENDANT, newSnail)
                            TPOSITION.RIGHT -> current?.right = SnailValue(TSNAILTYPE.DESCENDANT, newSnail)
                        }
                        current = newSnail
                        currentGroup.snailfishes.add(current!!)
                        position = TPOSITION.LEFT
                    }
                    in ('0'..'9') -> {
                        when(position) {
                            TPOSITION.LEFT -> current?.left = SnailValue(TSNAILTYPE.INTEGER, c.digitToInt())
                            TPOSITION.RIGHT -> current?.right = SnailValue(TSNAILTYPE.INTEGER, c.digitToInt())
                        }
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
            retValue.add(currentGroup)
        }
        return retValue
    }
    //endregion
}