package com.example.daniel.time4takes

import android.arch.persistence.room.*
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import android.content.Context
import kotlin.coroutines.experimental.coroutineContext


@Entity(tableName = "User")
data class User(@PrimaryKey(autoGenerate = true) var uid: Long?,
    @ColumnInfo(name = "first_name") var firstName: String?,
    @ColumnInfo(name = "last_name") var lastName: String?
){
    constructor():this(null, "", "")
}

@Dao
interface UserDao {
    @get:Query("SELECT * FROM user")
    val all: List<User>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " + "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)
}

@Database(entities = arrayOf(User::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase::class.java, "user.db")
                            .build()
                }
            }
            return INSTANCE

            fun destroyInstance() {
                INSTANCE = null
            }
        }
    }
}
