import kotlin.experimental.or

object Categories {
    const val none: Short = 0
    const val naughty: Short = 1
    const val food: Short = 2
    const val santa: Short = 4
    const val walls: Short = 8
    const val lights: Short = 16
    const val ropes: Short = 32

    val whatBlobsCollideWith = naughty or food or walls or santa or lights or ropes
    val whatFoodCollidesWith = naughty
    val whatHumansCollideWith = naughty or santa or walls or lights or ropes
    val whatWallsCollideWith = naughty or santa or ropes
    val whatLightsCollideWith = naughty or santa or walls
    val whatRopesCollideWith = naughty or santa or walls
}