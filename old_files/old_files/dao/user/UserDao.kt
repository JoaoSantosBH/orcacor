package com.jomar.senhorpintor.dao.user

import androidx.room.*
import com.jomar.senhorpintor.model.entities.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)
    
    @Query("SELECT * FROM User")
    fun getUser(): User
}