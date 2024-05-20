package com.example.attractions.data

import com.example.attractions.R
import com.example.attractions.model.Attraction

class Datasource() {
    fun loadAttractions(): List<Attraction> {
        return listOf<Attraction>(
            Attraction(R.string.ueno_park, R.drawable.ueno_park),
            Attraction(R.string.tokyo_national_museum, R.drawable.tokyo_national_museum),
            Attraction(R.string.imperial_palace, R.drawable.imperial_palace),
            Attraction(R.string.ghibli_museum, R.drawable.ghibli_museum),
            Attraction(R.string.sensoji_temple, R.drawable.sensoji_temple),
            Attraction(R.string.shinjuku_gyoen_national_garden, R.drawable.shinjuku_gyoen_national_garden),
            Attraction(R.string.meiji_shrine, R.drawable.meiji_shrine),
            Attraction(R.string.shibuya_crossing, R.drawable.shibuya_crossing),
            Attraction(R.string.tokyo_skytree, R.drawable.tokyo_skytree),
            Attraction(R.string.seafood_stall, R.drawable.seafood_stall)
        )
    }
}
