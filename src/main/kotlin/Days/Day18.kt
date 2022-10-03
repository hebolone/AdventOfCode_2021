package Days

class Day18 : AlgosBase() {
    override fun Basic(input : MutableList<String>) : Int {
        val lines = ParseInput(input)
        var current : Group? = null
        var step = 1
        for(line in lines) {
            if(current == null) current = line else current += line
            step ++
        }

        return current?.Magnitude() ?: -1
    }

    override fun Advanced(input : MutableList<String>) : Int {
        val lines = ParseInput(input)
        var magnitude = 0

        (lines.indices).forEach {
            var index = 0
            var resultingMagnitude : Int
            do {
                if(lines[it].toString() != lines[index].toString()) {
                    val result = lines[it] + lines[index]
                    resultingMagnitude = result.Magnitude()
                    if(resultingMagnitude > magnitude) {
                        magnitude = resultingMagnitude
                    }
                }
                index ++
            } while(index < lines.size)
        }

        return magnitude
    }

    //region Private
    private enum class TPOSITION { LEFT, RIGHT }
    private enum class TSNAILTYPE { DESCENDANT, INTEGER }
    private enum class TACTION { EXPLODE, SPLIT }
    private data class SnailValue(val snailType : TSNAILTYPE, var value : Any)
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

        fun containsOnlyIntegers() : Boolean = left?.snailType == TSNAILTYPE.INTEGER && right?.snailType == TSNAILTYPE.INTEGER

        fun getPositionFromAncestor(snailFish : SnailFish) : TPOSITION = if(ancestor?.left?.value == snailFish) TPOSITION.LEFT else TPOSITION.RIGHT

        fun calculateMagnitude() : Int {
            var leftValue = left?.value as? Int ?: -1
            if(leftValue == -1)
                leftValue = (left?.value as SnailFish).calculateMagnitude()

            var rightValue = right?.value as? Int ?: -1
            if(rightValue == -1)
                rightValue = (right?.value as SnailFish).calculateMagnitude()

            return (3 * leftValue) + (2 * rightValue)
        }
    }
    private class Group(initString : String) {
        private data class Position(val index : Int, val value : Int, val position : TPOSITION, val snailFish: SnailFish)
        private data class ActionToDo(var action : TACTION, var snailFish: SnailFish, var position : TPOSITION? = null)
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
            var buffer = ""
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
                            buffer += c
                        }

                        ',' -> {
                            //  Change left to right
                            if(buffer != "") {
                                current?.left = SnailValue(TSNAILTYPE.INTEGER, buffer.toInt())
                                _Positions.add(Position(_Positions.size + 1, buffer.toInt(), TPOSITION.LEFT, current !!))
                                buffer = ""
                            }
                            position = TPOSITION.RIGHT
                        }

                        ']' -> {
                            //  Set the new current: the father of this one
                            if(buffer != "") {
                                current?.right = SnailValue(TSNAILTYPE.INTEGER, buffer.toInt())
                                _Positions.add(Position(_Positions.size + 1, buffer.toInt(), TPOSITION.RIGHT, current !!))
                                buffer = ""
                            }
                            current = current?.ancestor
                        }
                    }
                }
            }
        }

        private fun SearchForExplosion(snailFish: SnailFish) : ActionToDo? {
            if(snailFish.GetNumberOfAncestors() == 4 && snailFish.containsOnlyIntegers())
                return ActionToDo(TACTION.EXPLODE, snailFish)

            //  Continue searching recursively
            var action : ActionToDo?

            if(snailFish.left?.snailType == TSNAILTYPE.DESCENDANT) {
                action = SearchForExplosion(snailFish.left?.value as SnailFish)
                if(action != null) return action
            }
            if(snailFish.right?.snailType == TSNAILTYPE.DESCENDANT) {
                action = SearchForExplosion(snailFish.right?.value as SnailFish)
                if(action != null) return action
            }

            return null
        }

        private fun SearchForSplit(snailFish: SnailFish) : ActionToDo? {
            var action : ActionToDo?

            if (snailFish.leftAsInt >= 10)
                return ActionToDo(TACTION.SPLIT, snailFish, TPOSITION.LEFT)
            else if(snailFish.left?.snailType == TSNAILTYPE.DESCENDANT) {
                action = SearchForSplit(snailFish.left?.value as SnailFish)
                if(action != null) return action
            }

            if (snailFish.rightAsInt >= 10)
                return ActionToDo(TACTION.SPLIT, snailFish, TPOSITION.RIGHT)
            else if(snailFish.right?.snailType == TSNAILTYPE.DESCENDANT) {
                action = SearchForSplit(snailFish.right?.value as SnailFish)
                if(action != null) return action
            }

            return null
        }

        fun Reduce() : Group {
            //  Search for condition of explosion:
            //  1 - inside 4 pair (explosion)
            //  2 - one number is 10 or more (split)
            //println("Before action: $this")
            var actionToDo : ActionToDo?
            do {
                do {
                    //  Explode
                    actionToDo = SearchForExplosion(_SnailFishes.single { it.ancestor == null })
                    if(actionToDo != null) {
                        actionToDo.snailFish.toString()
                        Explode(actionToDo.snailFish)
                        CalculateSnailFishes(this.toString())
                        //println("Action (${actionToDo?.action}): $this (on snail: $snailFishExecuted)")
                    }
                } while(actionToDo != null)

                //  Split
                actionToDo = SearchForSplit(_SnailFishes.single { it.ancestor == null })
                if(actionToDo != null) {
                    actionToDo.snailFish.toString()
                    Split(actionToDo.snailFish, actionToDo.position !!)
                    CalculateSnailFishes(this.toString())
                    //println("Action (${actionToDo?.action}): $this (on snail: $snailFishExecuted)")
                }
            } while(actionToDo != null)
            return this
        }

        private fun Explode(snailFish : SnailFish) {
            //  Search leftmost
            val leftMost = SearchOnTheLeft(snailFish)
            leftMost?.snailFish?.sum(leftMost.position, snailFish.leftAsInt)

            //  Search rightmost
            val rightMost = SearchOnTheRight(snailFish)
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
        }

        private fun Split(snailFish : SnailFish, position : TPOSITION) {
            val values = when(position) {
                TPOSITION.LEFT -> {
                    val leftValue = snailFish.leftAsInt / 2
                    val rightValue = snailFish.leftAsInt - leftValue
                    Pair(leftValue, rightValue)
                }
                TPOSITION.RIGHT -> {
                    val leftValue = snailFish.rightAsInt / 2
                    val rightValue = snailFish.rightAsInt - leftValue
                    Pair(leftValue, rightValue)
                }
            }

            val newSnailFish = SnailFish(
                snailFish,
                SnailValue(TSNAILTYPE.INTEGER, values.first),
                SnailValue(TSNAILTYPE.INTEGER, values.second)
            )
            _SnailFishes.add(newSnailFish)

            when(position) {
                TPOSITION.LEFT -> snailFish.left = SnailValue(TSNAILTYPE.DESCENDANT, newSnailFish)
                TPOSITION.RIGHT -> snailFish.right = SnailValue(TSNAILTYPE.DESCENDANT, newSnailFish)
            }
        }

        private fun SearchOnTheLeft(snailFish : SnailFish) : Position? {
            //  Starting from current snail fish I have to search inside IntegerPositions list
            val startingPosition : Position = _Positions.first { it.snailFish == snailFish }
            return _Positions.lastOrNull { it.index < (startingPosition.index)  && it.snailFish !== snailFish }
        }

        private fun SearchOnTheRight(snailFish : SnailFish) : Position? {
            val startingPosition = _Positions.first { it.snailFish == snailFish }
            return _Positions.firstOrNull { it.index > startingPosition.index  && it.snailFish !== snailFish }
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

        operator fun plus(addedSnailFish : Group) : Group = Group("[$this,$addedSnailFish]").Reduce()

        override fun toString(): String = _SnailFishes.single { it.ancestor == null }.toString()

        fun Magnitude() : Int = _SnailFishes.single { it.ancestor == null }.calculateMagnitude()
    }

    private fun ParseInput(lines : MutableList<String>) : List<Group> {
        val retValue = mutableListOf<Group>()
        lines.filter { ! it.startsWith('-') }.forEach {
            retValue.add(Group(it))
        }
        return retValue
    }
    //endregion
}