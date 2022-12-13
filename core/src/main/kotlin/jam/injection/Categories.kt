import kotlin.experimental.or

object Categories {
    const val none: Short = 0
    const val naughty: Short = 1
    const val present: Short = 2
    const val santa: Short = 4
    const val house: Short = 8
    const val lights: Short = 16
    const val santasSense: Short = 32

    val whatSantaCollidesWith = naughty or santa or lights
    val whatHousesCollideWith = naughty or present or santasSense
    val whatPresentsCollideWith = house
    val whatSantasSenseCollidesWith = house
}