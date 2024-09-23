package uk.laptopphilharmonic.noisemaker.piece

class Bar(
    val number: Int,
    val beats: Int,
    val startTime: Double,
    val tempo: BPM,
    private val bars: Bars,
) {
    /** Get the time in milliseconds of the start of the beat specified in this bar
     * @param n - 0-indexed number of the beat in question. Decimals welcome.
     * @param  - An optional function with the absolute time of the beat passed to it */
    fun beat(n: Number): Double {
        val beatNumber = n.toDouble()
        require(beatNumber >= 0)
        require(beatNumber < beats)
        val b = startTime + (beatNumber * tempo.beatLength)
        return b
    }

    /** The time in milliseconds of the end of the bar */
    val endOfBar get(): Double = startTime + (beats * tempo.beatLength)
    val nextBar get(): Bar? = bars.orderedBars.getOrNull(number + 1)
    val previousBar get(): Bar? = bars.orderedBars.getOrNull(number - 1)
}

/** Helper class to make it easier to refer to times in your piece */
class Bars(
    /** This list will build the "time canvas" for your piece */
    vararg data: BarsData
) {
    private val startingBeatsPerBar: Int
    private val startingTempo: BPM

    init {
        require(data.isNotEmpty())
        startingBeatsPerBar = data[0].beatsPerBar
        startingTempo = data[0].tempo
    }

    val orderedBars: MutableList<Bar> = mutableListOf()

    private val mostRecent: Bar? get() = orderedBars.lastOrNull()
    private val mostRecentBeats: Int get() = mostRecent?.beats ?: startingBeatsPerBar
    private val mostRecentTempo: BPM get() = mostRecent?.tempo ?: startingTempo

    init {
        data.forEach { d ->
            repeat(d.howMany) {
                bar(d.beatsPerBar, d.tempo)
            }
        }
    }

    /** By default, this will duplicate the previous bar's settings */
    private fun bar(beats: Int = mostRecentBeats, tempo: BPM = mostRecentTempo) {
        val bar = Bar(
            number = (mostRecent?.number ?: 0)  + 1,
            beats,
            startTime = (mostRecent?.startTime ?: 0.0) + (mostRecentBeats * mostRecentTempo.beatLength),
            tempo,
            bars = this
        )
        orderedBars += bar
    }

    fun forEach(fn: (b: Bar) -> Unit) {
        this.orderedBars.forEach(fn)
    }

    fun withEach(fn: Bar.() -> Unit) {
        this.orderedBars.forEach { it.fn() }
    }
}

data class BarsData(
    /** How many beats per bar for these bars? */
    val beatsPerBar: Int,
    /** What is the tempo for these bars? */
    val tempo: BPM,
    /** How many bars in this set? */
    val howMany: Int,
)