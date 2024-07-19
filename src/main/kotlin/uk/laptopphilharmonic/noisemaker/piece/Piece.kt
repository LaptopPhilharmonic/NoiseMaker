package uk.laptopphilharmonic.noisemaker.piece

import uk.laptopphilharmonic.noisemaker.synth.SineSynth
import uk.laptopphilharmonic.noisemaker.synth.Synth

class Piece {
    val allVoices = mutableListOf<Voice>()

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
    val length: Int get() {
        val lastVoiceAndNote: Pair<Voice, Note?> = allVoices.map { it to (
                if (it.allNotes.isEmpty()) null else {
                    it.allNotes.sortedByDescending { n -> n.end }[0]
                })
        }.sortedByDescending { it.second?.end ?: 0 }[0]

        return (lastVoiceAndNote.second?.end ?: 0) + (lastVoiceAndNote.first.synth.envelope?.release ?: 0)
    }

    /** Which notes are playing at this point? */
    fun notesPlayingAtTime(millis: Double): List<VoiceAndNote> =
        allVoices.flatMap { voice ->
            voice.allNotes.filter { note ->
                note.start <= millis && (note.end + (voice.synth.envelope?.release ?: 0)) >= millis
            }.map { VoiceAndNote(voice, it) }
        }
}

data class VoiceAndNote(
    val voice: Voice,
    val note: Note,
)