package com.lust.app.data.entities

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.lust.app.R

enum class PlaceType(val id: Int, val title: Int, val image: Int, val color: Color) {
    ALL(0, R.string.place_type_all, R.drawable.ic_all, Color(0xFFFF9800)),
    PARK(1, R.string.place_type_park, R.drawable.ic_park, Color(0xFF4CAF50)),
    PUBLIC_BATHROOMS(
        2,
        R.string.place_type_public_bathrooms, R.drawable.ic_bathrooms, Color(
            0xFF2196F3
        )
    ),
    STEAM_ROOM(
        3,
        R.string.place_type_steam_room, R.drawable.ic_steam_room, Color(
            0xFF9C27B0
        )
    ),
    HOTEL(
        4,
        R.string.place_type_hotel, R.drawable.ic_hotel, Color(
            0xFFFF5722
        )
    ),
    CLUB(5, R.string.place_type_club, R.drawable.ic_club, Color(0xFF673AB7)),
    SEX_SHOP(
        6,
        R.string.place_type_sex_shop, R.drawable.ic_sex_shop, Color(
            0xFFE91E63
        )
    ),
    NUDIST_BEACH(
        7,
        R.string.place_type_nudist_beach, R.drawable.ic_nudist_beach, Color(
            0xFF03A9F4
        )
    ),
    BAR(8, R.string.place_type_bar, R.drawable.ic_beer, Color(0xFF795548)),
    BEACH(
        9,
        R.string.place_type_beach, R.drawable.ic_beach, Color(
            0xFF00BCD4
        )
    ),
    GYM(10, R.string.place_type_gym, R.drawable.ic_gym, Color(0xFF8BC34A)),
    CYBER_CAFE(
        11,
        R.string.place_type_cyber_cafe, R.drawable.ic_cyber, Color(
            0xFF607D8B
        )
    ),
    CINEMA(12, R.string.place_type_cinema, R.drawable.ic_cinema, Color(0xFFFFC107)),
    TEMPORARY(
        13,
        R.string.place_type_temporary, R.drawable.ic_temporary, Color(
            0xFFFFEB3B
        )
    );

    companion object {
        fun fromId(id: Int): PlaceType {
            return entries.find { it.id == id } ?: ALL
        }

        @DrawableRes
        fun getIcon(id: Int): Int {
            return entries.find { it.id == id }?.image ?: R.drawable.ic_all
        }
    }
}