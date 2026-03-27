package com.jomar.senhorpintor.model.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jomar.senhorpintor.extensions.readDate
import com.jomar.senhorpintor.extensions.writeDate
import java.sql.Date

@Keep
@Entity
data class Budget(
        @PrimaryKey(autoGenerate = true)
        val id: Int? = null,
        @ColumnInfo(name = "num_orcamento")
        val badgeNumber: String? = null,
        @ColumnInfo(name = "data_orcamento")
        var badgeDate: Date? = null,
        @ColumnInfo(name = "nome_destinatario")
        val badgeDestName: String? = null,
        @ColumnInfo(name = "email_destinatario")
        val badgeDestEmail: String? =null

):Parcelable {

        constructor(parcel: Parcel) : this(
                parcel.readValue(Int::class.java.classLoader) as? Int,
                parcel.readString(),
                parcel.readDate(),
                parcel.readString(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeValue(id)
                parcel.writeString(badgeNumber)
                parcel.writeDate(badgeDate)
                parcel.writeString(badgeDestName)
                parcel.writeString(badgeDestEmail)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<Budget> {
                override fun createFromParcel(parcel: Parcel): Budget {
                        return Budget(parcel)
                }

                override fun newArray(size: Int): Array<Budget?> {
                        return arrayOfNulls(size)
                }
        }
}