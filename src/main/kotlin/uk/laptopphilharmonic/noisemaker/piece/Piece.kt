package uk.laptopphilharmonic.noisemaker.piece

import uk.laptopphilharmonic.noisemaker.synth.SineSynth
import uk.laptopphilharmonic.noisemaker.synth.Synth

/** The container for all data in your piece, to be passed to a NoiseMaker instance for playing/export */
class Piece {
    /** All the Voice instances attached to this Piece */
    val allVoices = mutableListOf<Voice>()
    /** Tempo in beats per minute (BPM) */
    var tempo = 120.0

    /**
     * Creates a new Voice in this piece that you can use to play notes
     * @param synth - What type of synthesizer to use (default is SineSynth)
     * @param volume - How loud to play notes in this synth (0.0 = silent, 1.0 = maximum possible)
     * @param pan - Where to place this if it's a stereo mix (-1.0 = full left, 1.0 = full right)
     */
    fun addVoice(
        synth: Synth = SineSynth(),
        volume: Double = 1.0,
        pan: Double = 0.0
    ): Voice {
        val voice = Voice(this, synth, volume, pan)
        allVoices.addLast(voice)
        return voice
    }

    /** Length in milliseconds of the whole piece (includes any decay time in envelopes) */
    val length: Double get() {
        val lastVoiceAndNote: Pair<Voice, Note?> = allVoices.map { it to (
                if (it.allNotes.isEmpty()) null else {
                    it.allNotes.sortedByDescending { n -> n.endTime }[0]
                })
        }.sortedByDescending { it.second?.endTime ?: 0.0 }[0]

        return (lastVoiceAndNote.second?.endTime ?: 0.0) + (lastVoiceAndNote.first.synth.envelope?.release?.toDouble() ?: 0.0)
    }

    /** Which notes are playing at this point? */
    fun notesPlayingAtTime(millis: Double): List<VoiceAndNote> =
        allVoices.flatMap { voice ->
            voice.allNotes.filter { note ->
                note.startTime <= millis && (note.endTime + (voice.synth.envelope?.release ?: 0)) >= millis
            }.map { VoiceAndNote(voice, it) }
        }
}

data class VoiceAndNote(
    val voice: Voice,
    val note: Note,
)

/** Beats per minute */
data class BPM(
    val bpm: Double
) {
    /** Length of individual beat in milliseconds */
    val beatLength = 60000.0 / bpm
}

val Number.bpm get() = BPM(this.toDouble())