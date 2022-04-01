package controllers

import models.Note
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class NoteAPI {
    private var notes = ArrayList<Note>()

    fun add(note: Note): Boolean {
        return notes.add(note)
    }

    fun deleteNote(indexToDelete: Int): Note? {
        return if (isValidListIndex(indexToDelete, notes)) {
            notes.removeAt(indexToDelete)
        } else null
    }

    fun listAllNotes(): String {
        return if (notes.isEmpty()) {
            "No notes stored"
        } else {
            var listOfNotes = ""
            for (i in notes.indices) {
                listOfNotes += "${i}: ${notes[i]} \n"
            }
            val listOfNotes1 = listOfNotes
            listOfNotes1
        }
    }

    fun updateNote(indexToUpdate: Int, note: Note?): Boolean {
        //find the note object by the index number
        val foundNote = findNote(indexToUpdate)

        //if the note exists, use the note details passed as parameters to update the found note in the ArrayList.
        if ((foundNote != null) && (note != null)) {
            foundNote.noteTitle = note.noteTitle
            foundNote.notePriority = note.notePriority
            foundNote.noteCategory = note.noteCategory
            return true
        }

        //if the note was not found, return false, indicating that the update was not successful
        return false
    }

    fun listActiveNotes(): String {
        return if (numberOfActiveNotes() == 0) {
            "No active notes stored"
        } else {
            var listOfActiveNotes = ""
            for (note in notes) {
                if (!note.isNoteArchived) {
                    listOfActiveNotes += "${notes.indexOf(note)}: $note \n"
                }
            }
            listOfActiveNotes
        }
    }

    fun listArchivedNotes(): String {
        return if (numberOfArchivedNotes() == 0) {
            "No archived notes stored"
        } else {
            var listOfArchivedNotes = ""
            for (note in notes) {
                if (note.isNoteArchived) {
                    listOfArchivedNotes += "${notes.indexOf(note)}: $note \n"
                }
            }
            listOfArchivedNotes
        }
    }

    fun listNotesBySelectedPriority(priority: Int): String {
        return if (notes.isEmpty()) {
            "No notes stored"
        } else {
            var listOfNotes = ""
            for (i in notes.indices) {
                if (notes[i].notePriority == priority) {
                    listOfNotes +=
                        """$i: ${notes[i]}
                        """.trimIndent()
                }
            }
            if (listOfNotes.equals("")) {
                "No notes with priority: $priority"
            } else {
                "${numberOfNotesByPriority(priority)} notes with priority $priority: $listOfNotes"
            }
        }
    }

    fun numberOfNotes(): Int {
        return notes.size
    }

    fun numberOfArchivedNotes(): Int {
        //return notes.stream().filter { obj: Note -> obj.isNoteArchived }.count().toInt()
        var counter = 0
        for (note in notes) {
            if (note.isNoteArchived) {
                counter++
            }
        }
        return counter
    }

    fun numberOfActiveNotes(): Int {
        //return notes.stream().filter { p: Note -> !p.isNoteArchived }.count().toInt()
        var counter = 0
        for (note in notes) {
            if (!note.isNoteArchived) {
                counter++
            }
        }
        return counter
    }

    fun numberOfNotesByPriority(priority: Int): Int {
        //return notes.stream().filter { p: Note -> p.notePriority == priority }.count().toInt()
        var counter = 0
        for (note in notes) {
            if (note.notePriority == priority) {
                counter++
            }
        }
        return counter
    }

    fun findNote(index: Int): Note? {
        return if (isValidListIndex(index, notes)) {
            notes[index]
        } else null
    }

    //utility method to determine if an index is valid in a list.
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }

    fun isValidIndex(index: Int) :Boolean{
        return isValidListIndex(index, notes);
    }
}

class NoteAPITest {

    private var learnKotlin: Note? = null
    private var summerHoliday: Note? = null
    private var codeApp: Note? = null
    private var testApp: Note? = null
    private var swim: Note? = null
    private var populatedNotes: NoteAPI? = NoteAPI()
    private var emptyNotes: NoteAPI? = NoteAPI()



    @BeforeEach
    fun setup(){
        learnKotlin = Note("Learning Kotlin", 5, "College", false)
        summerHoliday = Note("Summer Holiday to France", 1, "Holiday", false)
        codeApp = Note("Code App", 4, "Work", false)
        testApp = Note("Test App", 4, "Work", false)
        swim = Note("Swim - Pool", 3, "Hobby", false)

        //adding 5 Note to the notes api
        populatedNotes!!.add(learnKotlin!!)
        populatedNotes!!.add(summerHoliday!!)
        populatedNotes!!.add(codeApp!!)
        populatedNotes!!.add(testApp!!)
        populatedNotes!!.add(swim!!)
    }

    @AfterEach
    fun tearDown(){
        learnKotlin = null
        summerHoliday = null
        codeApp = null
        testApp = null
        swim = null
        populatedNotes = null
        emptyNotes = null
    }

    @Nested
    inner class UpdateNotes {
        @Test
        fun `updating a note that does not exist returns false`(){
            assertFalse(populatedNotes!!.updateNote(6, Note("Updating Note", 2, "Work", false)))
            assertFalse(populatedNotes!!.updateNote(-1, Note("Updating Note", 2, "Work", false)))
            assertFalse(emptyNotes!!.updateNote(0, Note("Updating Note", 2, "Work", false)))
        }

        @Test
        fun `updating a note that exists returns true and updates`() {
            //check note 5 exists and check the contents
            assertEquals(swim, populatedNotes!!.findNote(4))
            assertEquals("Swim - Pool", populatedNotes!!.findNote(4)!!.noteTitle)
            assertEquals(3, populatedNotes!!.findNote(4)!!.notePriority)
            assertEquals("Hobby", populatedNotes!!.findNote(4)!!.noteCategory)

            //update note 5 with new information and ensure contents updated successfully
            assertTrue(populatedNotes!!.updateNote(4, Note("Updating Note", 2, "College", false)))
            assertEquals("Updating Note", populatedNotes!!.findNote(4)!!.noteTitle)
            assertEquals(2, populatedNotes!!.findNote(4)!!.notePriority)
            assertEquals("College", populatedNotes!!.findNote(4)!!.noteCategory)
        }
    }

    @Nested
    inner class DeleteNotes {

        @Test
        fun `deleting a Note that does not exist, returns null`() {
            assertNull(emptyNotes!!.deleteNote(0))
            assertNull(populatedNotes!!.deleteNote(-1))
            assertNull(populatedNotes!!.deleteNote(5))
        }

        @Test
        fun `deleting a note that exists delete and returns deleted object`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertEquals(swim, populatedNotes!!.deleteNote(4))
            assertEquals(4, populatedNotes!!.numberOfNotes())
            assertEquals(learnKotlin, populatedNotes!!.deleteNote(0))
            assertEquals(3, populatedNotes!!.numberOfNotes())
        }
    }
    @Nested
    inner class AddNotes {
        @Test
        fun `adding a Note to a populated list adds to ArrayList`() {
            val newNote = Note("Study Lambdas", 1, "College", false)
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertTrue(populatedNotes!!.add(newNote))
            assertEquals(6, populatedNotes!!.numberOfNotes())
            assertEquals(newNote, populatedNotes!!.findNote(populatedNotes!!.numberOfNotes() - 1))
        }

        @Test
        fun `adding a Note to an empty list adds to ArrayList`() {
            val newNote = Note("Study Lambdas", 1, "College", false)
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.add(newNote))
            assertEquals(1, emptyNotes!!.numberOfNotes())
            assertEquals(newNote, emptyNotes!!.findNote(emptyNotes!!.numberOfNotes() - 1))
        }
    }

    @Nested
    inner class ListNotes {

        @Test
        fun `listAllNotes returns No Notes Stored message when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.listAllNotes().lowercase().contains("no notes"))
        }

        @Test
        fun `listAllNotes returns Notes when ArrayList has notes stored`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val notesString = populatedNotes!!.listAllNotes().lowercase()
            assertTrue(notesString.contains("learning kotlin"))
            assertTrue(notesString.contains("code app"))
            assertTrue(notesString.contains("test app"))
            assertTrue(notesString.contains("swim"))
            assertTrue(notesString.contains("summer holiday"))
        }
    }

}