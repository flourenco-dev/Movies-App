package com.flourenco.movieschallenge.model

import androidx.annotation.StringRes
import com.flourenco.movieschallenge.R

enum class DetailInfoType(@StringRes val headerId: Int) {
    Duration(R.string.detail_info_duration_section_label),
    Description(R.string.detail_info_description_section_label),
    Rating(R.string.detail_info_rating_section_label),
    Genres(R.string.detail_info_genres_section_label),
    Type(R.string.detail_info_type_section_label),
    Directors(R.string.detail_info_directors_section_label),
    Writers(R.string.detail_info_writers_section_label),
    Companies(R.string.detail_info_companies_section_label),
    Countries(R.string.detail_info_countries_section_label),
    Stars(R.string.detail_info_stars_section_label)
}