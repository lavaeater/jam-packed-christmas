package jam.ecs.systems

import com.badlogic.ashley.systems.IntervalSystem
import eater.ecs.ashley.components.PropsAndStuff
import jam.core.ChristmasGame
import jam.ecs.components.SantaClaus
import jam.map.ChristmasMapManager
import ktx.ashley.allOf

class GameOverSystem(private val christmasMapManager: ChristmasMapManager, private val mainGame: ChristmasGame): IntervalSystem(1f) {
    val santaAndHealthFamily = allOf(SantaClaus::class, PropsAndStuff::class).get()

    override fun updateInterval() {
        if(christmasMapManager.cities.none { it.needsGifts }) {
            mainGame.goToGameOver()
        }

    }
}