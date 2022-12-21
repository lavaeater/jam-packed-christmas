import kotlin.experimental.or

object Box2dCategories {
    const val none: Short = 0
    const val samSite: Short = 1
    const val present: Short = 2
    const val santa: Short = 4
    const val house: Short = 8
    const val lights: Short = 16
    const val santasSense: Short = 32
    const val actualSam: Short = 64

    val whatSantaCollidesWith = santa or lights or actualSam
    val whatHousesCollideWith = present or santasSense
    val whatPresentsCollideWith = house
    val whatSantasSenseCollidesWith = house
    val whatSamSitesCollideWith = santa
    val whatActualSamCollidesWith = santa
}