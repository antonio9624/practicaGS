package com.example.mvvmpokemon.model

data class Location(var lat: Double, var long: Double){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Location

        if (lat != other.lat) return false
        if (long != other.long) return false

        return true
    }

    override fun hashCode(): Int {
        var result = lat.hashCode()
        result = 31 * result + long.hashCode()
        return result
    }
}

