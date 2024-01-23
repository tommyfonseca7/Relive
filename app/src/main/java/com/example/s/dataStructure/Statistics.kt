package com.example.s.dataStructure

import kotlin.random.Random

data class Statistics (
    var cornerKicks : Int,
    var freeKicks: Int,
    var goalKicks:Int,
    var offsides:Int ,
    var fouls: Int,
    var ballPossession: Int,
    var saves: Int,
    var throwIns: Int,
    var shots:Int,
    var shotsOnGoal:Int,
    var shotsOffGoal:Int,
    var yellowCards:Int,
    var yellowRedCards:Int,
    var redCards: Int
)

fun GenerateStatistics() : Statistics{
    var random = Random
    return Statistics (
        cornerKicks = random.nextInt(0, 15),
        freeKicks =  random.nextInt(0, 15),
        goalKicks =  random.nextInt(0, 15),
        offsides =  random.nextInt(0, 10),
        fouls = random.nextInt(0, 20),
        ballPossession = random.nextInt(0, 30),
        saves = random.nextInt(0, 10),
        throwIns = random.nextInt(0, 10),
        shots = random.nextInt(0, 15),
        shotsOnGoal =random.nextInt(0, 10),
        shotsOffGoal = random.nextInt(0, 10),
        yellowCards = random.nextInt(0, 10),
        yellowRedCards = random.nextInt(0, 5),
        redCards = random.nextInt(0, 5),
    )
}