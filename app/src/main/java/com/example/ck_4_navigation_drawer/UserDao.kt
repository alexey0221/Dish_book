package com.example.ck_4_navigation_drawer

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Entity
data class User(
    @PrimaryKey val nickName: String = "Гость",
    val email:String,
    val authorization:Boolean = false
)

@Dao
interface UserDao {


    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT nickName FROM user")
    fun getNickName(): List<String>

    @Query("SELECT email FROM user")
    fun getEmail(): List<String>

    @Query("SELECT authorization FROM user")
    fun getAuthorization(): List<Boolean>

//    @Query("SELECT * FROM user WHERE name IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<User>

    @Insert
    fun insertAll(vararg users: User)

    @Query("DELETE FROM user")
    fun delete()
}


@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object{
        private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase{
            if (INSTANCE == null){
                var empty = false
                INSTANCE = Room.databaseBuilder(context, AppDatabase:: class.java, "database").build()
                GlobalScope.launch{
                    empty = INSTANCE!!.userDao().getAll().isEmpty()

                }
                if (empty)
                {
                    GlobalScope.launch{
                        INSTANCE!!.userDao().insertAll(User("Гость","",false))
                    }
                }

            }
            return INSTANCE as AppDatabase
        }
    }
}