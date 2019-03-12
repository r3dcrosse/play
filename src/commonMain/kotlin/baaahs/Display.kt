package baaahs

expect fun getDisplay(): Display

interface Display {
    var buildTime: Long?

    fun forNetwork(): NetworkDisplay
    fun forPinky(): PinkyDisplay
    fun forBrain(): BrainDisplay
    fun forMapper(): MapperDisplay

    fun haveNewBuild(newBuildTime: Long)
}

interface NetworkDisplay {
    fun receivedPacket()
    fun droppedPacket()
}

interface PinkyDisplay {
    var brainCount: Int
    var beat: Int
    var color: Color?
}

interface BrainDisplay {
    fun haveLink(link: Network.Link)
}

interface MapperDisplay {
    var onStart: (() -> Unit)?
    var onStop: (() -> Unit)?
}
