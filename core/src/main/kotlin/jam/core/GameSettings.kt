package jam.core

class GameSettings {
    val GameWidth = 72f
    val AspectRatio = 16f / 9f
    val GameHeight = AspectRatio * GameWidth
    val PixelsPerMeter = 4f
    val MetersPerPixel = 1f / PixelsPerMeter
    val TimeStep = 1 / 60f
    val VelIters = 16
    val PosIters = 6
}