import kotlin.experimental.or

object Categories {
    const val none: Short = 0
    const val naughty: Short = 1
    const val food: Short = 2
    const val santa: Short = 4
    const val house: Short = 8
    const val lights: Short = 16
    const val ropes: Short = 32

    val whatSantaCollidesWith = naughty or santa or lights or ropes
    val whatHousesCollideWith = naughty or ropes
    val whatLightsCollideWith = naughty or santa or house
    val whatRopesCollideWith = naughty or santa or house
}