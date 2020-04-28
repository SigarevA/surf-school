package ru.samsung.itshool.memandos.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memes")
data class Mem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "mem_id")
    val id : Long = 0,
    @ColumnInfo(name = "mem_title")
    val title : String = "",
    @ColumnInfo(name = "mem_description")
    val description : String = "",
    @ColumnInfo(name = "mem_isFavorite" )
    val isFavorite : Boolean = false,
    @ColumnInfo(name = "mem_createdDate")
    val createdDate : Long?,
    @ColumnInfo(name = "mem_photoUrl")
    val photoUrl : String?
)