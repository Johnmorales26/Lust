package com.lust.app.data.entities

import androidx.annotation.DrawableRes
import com.lust.app.R

enum class LocationCategory(val id: Int) {

    HOTEL(1),
    GYM(2);

    companion object {
        fun fromId(id: Int): LocationCategory {
            return entries.find { it.id == id } ?: HOTEL
        }
    }

    @DrawableRes
    fun getIcon(): Int {
        return when (this) {
            HOTEL -> R.drawable.ic_hotel
            GYM -> R.drawable.ic_gym
        }
    }

}