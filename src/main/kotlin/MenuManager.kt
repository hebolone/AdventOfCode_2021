class MenuManager(iProgramTitle : String) {
    //  *** PROPERTIES ***
    var MAX_EMPTY_LINES : Int = 3
    //  *** MEMBERS ***
    private var m_CanContinue : Boolean = true
    private var m_MenuList : MutableList<IMenu>
    private var m_AvailableMenus : MutableList<IMenu> = mutableListOf()
    private val mnuQuit : CMenu
    private val mnuPrint : CMenu
    private val mnuAbout : CMenu
    private val mnuBack : CMenu
    private val m_MenuChain : CMenuChain = CMenuChain(iProgramTitle)

    //  *** DATA TYPES ***
    private class CMenuChain(iTitle : String) {
        private var m_MenuChain : MutableList<IMenu> = mutableListOf()
        private val m_MenuTitle = iTitle
        val CurrentAncestor : IMenu?
            get() = if (m_MenuChain.count() == 0) null else m_MenuChain.last()

        fun Push(iMenu : IMenu) {
            m_MenuChain.add(iMenu)
        }

        fun Pop() {
            val lastMenu : IMenu = m_MenuChain.last()
            m_MenuChain.remove(lastMenu)
        }

        fun GetMenuHeader() : String {
            var menuHeader : String = m_MenuTitle
            m_MenuChain.forEach {
                menuHeader += " >> " + it.Title
            }
            return menuHeader
        }
    }

    //  *** INIT ***
    init {
        mnuQuit = CMenu("q", "Quit", { m_CanContinue = false })
        mnuPrint = CMenu("p", "Print Menu", { PrintSummary() })
        mnuAbout = CMenu("a", "About", { Print("Code by Sm3P") })
        mnuBack = CMenu("b", "Go back", {
            m_MenuChain.Pop()
            BuildMenuList()
        })
        m_MenuList = mutableListOf(mnuQuit, mnuPrint, mnuAbout, mnuBack)
    }

    //  *** METHODS ***
    fun AddMenu(iMenu : IMenu) = m_MenuList.add(iMenu)
    fun AddMenu(iShortCut : String, iTitle : String, iFunc : () -> Unit) = AddMenu(CMenu(iShortCut, iTitle, iFunc))
    fun AddMenu(iShortCut : String, iTitle : String, iFunc : () -> Unit, iAncestor : IMenu?) = AddMenu(CMenu(iShortCut, iTitle, iFunc, iAncestor))

    fun Interpreter() {
        BuildMenuList()
        PrintSummary()
        var emptyLines = 0
        while (m_CanContinue) {
            if (emptyLines == MAX_EMPTY_LINES) {
                emptyLines = 0
                PrintSummary()
            }
            Print("> ", false)
            val userInput : List<String> = readLine()!!.split(',')
            val multiCommand : Boolean = userInput.size > 1
            userInput.forEach { i : String ->
                if (i != "") {
                    emptyLines = 0
                    ExecuteCommand(i)
//                    if (!multiCommand && m_CanContinue) {
//                        PrintSummary()
//                    }
                } else {
                    emptyLines++
                }
            }
            if(multiCommand && m_CanContinue)
                PrintSummary()
        }
        Print("Bye bye, true believer")
    }

    fun ExecuteInstantCommands(iCommands : String){
        BuildMenuList()
        val splitCommands : List<String> = iCommands.split(',')
        splitCommands.forEach { c : String ->
            if (c != "") ExecuteCommand(c)
        }
    }

    private fun PrintSummary() {
        //  Print menu summary
        Print("")
        Print(m_MenuChain.GetMenuHeader())
        //  Print list of menus
        for (m : IMenu in m_AvailableMenus) {
            Print(m.TitleHeader)
        }
    }

    private fun ExecuteCommand(iShortCut : String) {
        val menuFound : IMenu? = FindMenu(iShortCut)
        menuFound?.Invoke() ?: Print("Command $iShortCut not found")
        //  Search for menus with this ancestor
        if (menuFound != null) {
            val menuWithThisAncestor = m_MenuList.filter { it.Ancestor == menuFound }
            if (menuWithThisAncestor.count() != 0) {
                m_MenuChain.Push(menuFound)
                BuildMenuList()
            }
        }
    }

    private fun FindMenu(iShortCut : String) : IMenu? = m_AvailableMenus.firstOrNull { m -> m.ShortCut == iShortCut }

    private fun BuildMenuList() {
        m_AvailableMenus = m_MenuList.filter { it.Ancestor == m_MenuChain.CurrentAncestor }.toMutableList()
        if (m_MenuChain.CurrentAncestor != null) {
            //  I am in a sub-level menu
            m_AvailableMenus.add(mnuBack)
            m_AvailableMenus.add(mnuPrint)
            m_AvailableMenus.add(mnuQuit)
        } else {
            //  I am in top class menu. No need to show "back menu"
            m_AvailableMenus.remove(mnuBack)
        }
        m_AvailableMenus.sortBy { it.ShortCut }
    }

    private fun Print(iMessage : String, iNewLine : Boolean = true) = if (iNewLine) println(iMessage) else print(iMessage)
}
//-------------------------------------------------------------------------------------------------
interface IMenu {
    val ShortCut : String
    val Title : String
    val Func : () -> Unit
    val Ancestor : IMenu?
    val TitleHeader : String
    fun Invoke() = Func()
}

open class CMenu(iShortCut : String, iTitle : String, iFunc : () -> Unit, iAncestor : IMenu? = null) : IMenu {
    //  *** DATA TYPES ***
    //  *** PROPERTIES ***
    override val TitleHeader : String
        get() = "$ShortCut - $Title"
    //  *** MEMBERS ***
    override val ShortCut : String = iShortCut
    override val Title : String = iTitle
    override val Func : () -> Unit = iFunc
    override val Ancestor : IMenu? = iAncestor
    //  *** METHODS ***
}

class CMenuVar<T>(iShortCut : String, iTitle : String, iFunc : () -> Unit, iValueGetter : () -> T, iAncestor : IMenu? = null) : CMenu(iShortCut, iTitle, iFunc, iAncestor) {
    override val TitleHeader : String
        get() = "$ShortCut - $Title <${FuncGetValue()}>"
    private val FuncGetValue : () -> T = iValueGetter
}