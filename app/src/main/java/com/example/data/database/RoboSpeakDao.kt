package com.example.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RoboSpeakDao {
    @Query("SELECT * FROM user_progress WHERE id = 1")
    fun getUserProgress(): Flow<UserProgressEntity?>

    @Query("SELECT * FROM user_progress WHERE id = 1")
    suspend fun getUserProgressOnce(): UserProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProgress(progress: UserProgressEntity)

    @Update
    suspend fun updateUserProgress(progress: UserProgressEntity)

    @Query("SELECT * FROM saved_vocab ORDER BY bookmarkedAt DESC")
    fun getAllSavedVocab(): Flow<List<SavedVocabEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSavedVocab(item: SavedVocabEntity)

    @Query("UPDATE saved_vocab SET isLearned = :isLearned WHERE wordId = :wordId")
    suspend fun updateVocabLearned(wordId: String, isLearned: Boolean)

    @Query("SELECT COUNT(*) FROM saved_vocab WHERE isLearned = 1")
    fun getLearnedVocabCount(): Flow<Int>

    @Insert
    suspend fun insertChatMessage(message: ChatHistoryEntity)

    @Query("SELECT * FROM chat_history WHERE topicId = :topicId ORDER BY timestamp ASC LIMIT 50")
    suspend fun getRecentChat(topicId: String): List<ChatHistoryEntity>

    @Query("DELETE FROM chat_history")
    suspend fun clearChatHistory()
}
