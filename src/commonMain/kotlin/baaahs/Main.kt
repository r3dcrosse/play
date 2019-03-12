package baaahs

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.js.JsName

lateinit var main: Main

//fun main(args: Array<String>) {
//    main = Main()
//    main.start()
//}

class Main {
    var display = getDisplay()
    var network = FakeNetwork(display = display.forNetwork())
    var buildTime: Long? = null

    var sheepModel = SheepModel()
    val pinky = Pinky(network, display.forPinky())
    val mapper = Mapper(network, display.forMapper())

    fun start() {
        sheepModel.load()

        mapper.start()
        pinky.start()

        initThreeJs(sheepModel)
        sheepModel.panels.forEach { panel ->
            val jsPanelObj = addPanel(panel)
            SimBrain(network, display.forBrain(), JsPanel(jsPanelObj)).start()
        }
        startRender()

        doRunBlocking {
            delay(200000L)
        }
    }

    @JsName("watchForNewBuild")
    fun watchForNewBuild(buildTime: String?) {
        display.buildTime = buildTime?.toLong()
        GlobalScope.launch {
            while (true) {
                delay(5000)

                var newBuildTime: Long? = null
                try {
                    newBuildTime = getResource("buildTime.txt").toLong()
                } catch (e: Exception) {
                }
                if (newBuildTime != null && newBuildTime != display.buildTime) {
                    display.haveNewBuild(newBuildTime)
                }
            }
        }
    }
}

class JsPanel(private val jsPanelObj: Any) {
    var color: Color = Color.BLACK
        set(value) {
            setPanelColor(jsPanelObj, value)
            field = color
        }

    fun select() {
        selectPanel(jsPanelObj, true)
    }
}

external fun initThreeJs(sheepModel: SheepModel)
external fun addPanel(panel: SheepModel.Panel): Any
external fun startRender()
external fun selectPanel(panel: Any, isSelected: Boolean)
external fun setPanelColor(panel: Any, color: Color)

expect fun getTimeMillis(): Long
expect fun doRunBlocking(block: suspend () -> Unit)

expect fun getResource(name: String): String