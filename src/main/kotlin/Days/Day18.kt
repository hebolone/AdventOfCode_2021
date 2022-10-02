package Days

class Day18 : AlgosBase() {
    override fun Basic(input : MutableList<String>) : Int {
//        val f1 = "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]"
//        val s1 = Group("[[[[4,3],4],4],[7,[[8,4],9]]]") + Group("[1,1]")

//        val snailFishes = ParseInput(mutableListOf(f2))
//        snailFishes.forEach { it.Actions() }
//        val s1 = Group("[[[[4,3],4],4],[7,[[8,4],9]]]") + Group("[1,1]")
//        val s1 = Group("[1,1]") +
//                Group("[2,2]") +
//                Group("[3,3]") +
//                Group("[4,4]") +
//                Group("[5,5]") +
//                Group("[6,6]")
//        val s1 = Group("[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]") +
//                  Group("[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]") +
//                Group("[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]") +
//                Group("[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]") +
//                Group("[7,[5,[[3,8],[1,4]]]]") +
//                Group("[[2,[2,2]],[8,[8,1]]]") +
//                Group("[2,9]") +
//                Group("[1,[[[9,3],9],[[9,0],[0,7]]]]") +
//                Group("[[[5,[7,4]],7],1]") +
//                Group("[[[[4,2],2],6],[8,7]]")

        val s1 = Group("[[[[7,0],[7,7]],[[7,7],[7,8]]],[[[7,7],[8,8]],[[7,7],[8,7]]]]") + Group("[7,[5,[[3,8],[1,4]]]]")

        var addedSnailFishes = s1
        addedSnailFishes.Reduce()
        println("$addedSnailFishes")
        println("Magnitude: ${s1.Magnitude()}")

        return -1
    }

    override fun Advanced(input : MutableList<String>) : Int {
        return -1
    }

    //region Private
    private enum class TPOSITION { LEFT, RIGHT }
    private enum class TSNAILTYPE { DESCENDANT, INTEGER }
    private enum class TACTION { EXPLODE, SPLIT, ANY }
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

        fun containsAtLeastOneInteger() : Boolean = left?.snailType == TSNAILTYPE.INTEGER || right?.snailType == TSNAILTYPE.INTEGER

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

        private fun SearchForAction(snailFish: SnailFish, actionSearched: TACTION) : ActionToDo? {
            when(actionSearched) {
                TACTION.EXPLODE -> {
                    //  Explosion
                    if(snailFish.GetNumberOfAncestors() == 4 && snailFish.containsOnlyIntegers())
                        return ActionToDo(TACTION.EXPLODE, snailFish)
                }
                TACTION.SPLIT -> {
                    //  Split
                    if (snailFish.leftAsInt >= 10)
                        return ActionToDo(TACTION.SPLIT, snailFish, TPOSITION.LEFT)

                    if (snailFish.rightAsInt >= 10)
                        return ActionToDo(TACTION.SPLIT, snailFish, TPOSITION.RIGHT)
                }
                TACTION.ANY -> {
                    //  Explosion
                    if(snailFish.GetNumberOfAncestors() >= 4 && snailFish.containsOnlyIntegers())
                        return ActionToDo(TACTION.EXPLODE, snailFish)

                    //  Split
                    if (snailFish.leftAsInt >= 10)
                        return ActionToDo(TACTION.SPLIT, snailFish, TPOSITION.LEFT)

                    if (snailFish.rightAsInt >= 10)
                        return ActionToDo(TACTION.SPLIT, snailFish, TPOSITION.RIGHT)
                }
            }

            //  Continue searching recursively
            var action : ActionToDo? = null
            if(snailFish.left?.snailType == TSNAILTYPE.DESCENDANT) {
                action = SearchForAction(snailFish.left?.value as SnailFish, actionSearched)
                if(action != null) return action
            }
            if(snailFish.right?.snailType == TSNAILTYPE.DESCENDANT) {
                action = SearchForAction(snailFish.right?.value as SnailFish, actionSearched)
                if(action != null) return action
            }
            return null
        }

//        fun Reduce() : Group {
//            //  Search for condition of explosion:
//            //  1 - inside 4 pair (explosion)
//            //  2 - one number is 10 or more (split)
//            println("Before action: $this")
//            var actionToDo : ActionToDo?
//            do {
//                actionToDo = SearchForAction(_SnailFishes.single { it.ancestor == null }, TACTION.ANY)
//
//                if(actionToDo != null) {
//                    val snailFishExecuted = actionToDo.snailFish.toString()
//                    when(actionToDo.action) {
//                        TACTION.EXPLODE -> Explode(actionToDo?.snailFish)
//                        TACTION.SPLIT -> Split(actionToDo?.snailFish, actionToDo?.position !!)
//                    }
//                    //  Recalculating indexes
//                    CalculateSnailFishes(this.toString())
//
//                    println("Action (${actionToDo?.action}): $this (on snail: $snailFishExecuted)")
//                    //PrintColored(_SnailFishes.single { it.ancestor == null }, actionToDo?.snailFish)
//                }
//
//            } while(actionToDo != null)
//            return this
//        }

        fun Reduce() : Group {
            //  Search for condition of explosion:
            //  1 - inside 4 pair (explosion)
            //  2 - one number is 10 or more (split)
            println("Before action: $this")
            var actionToDo : ActionToDo?
            do {
                do {
                    //  Explode
                    actionToDo = SearchForAction(_SnailFishes.single { it.ancestor == null }, TACTION.EXPLODE)
                    if(actionToDo != null) {
                        val snailFishExecuted = actionToDo.snailFish.toString()
                        Explode(actionToDo?.snailFish)
                        CalculateSnailFishes(this.toString())
                        println("Action (${actionToDo?.action}): $this (on snail: $snailFishExecuted)")
                    }
                } while(actionToDo != null)

                //  Split
                actionToDo = SearchForAction(_SnailFishes.single { it.ancestor == null }, TACTION.SPLIT)
                if(actionToDo != null) {
                    val snailFishExecuted = actionToDo.snailFish.toString()
                    Split(actionToDo?.snailFish, actionToDo?.position !!)
                    CalculateSnailFishes(this.toString())
                    println("Action (${actionToDo?.action}): $this (on snail: $snailFishExecuted)")
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
                    var leftValue = snailFish.leftAsInt / 2
                    var rightValue = snailFish.leftAsInt - leftValue
                    Pair(leftValue, rightValue)
                }
                TPOSITION.RIGHT -> {
                    var leftValue = snailFish.rightAsInt / 2
                    var rightValue = snailFish.rightAsInt - leftValue
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
            var startingPosition : Position? = _Positions.first { it.snailFish == snailFish }
            return _Positions.lastOrNull { it.index < (startingPosition?.index ?: -1)  && it.snailFish !== snailFish }
        }

        private fun SearchOnTheRight(snailFish : SnailFish) : Position? {
            var startingPosition = _Positions.first { it.snailFish == snailFish }
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