package com.example.flightmobileapp

data class SimulatorImage(
    val results: List<Result>
)

data class Result(
    val screenshot: String
)