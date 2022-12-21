package jam.ecs.systems

import ChristmasProperty
import com.badlogic.ashley.systems.IntervalSystem
import eater.ecs.ashley.components.EntityPropertyComponent
import eater.ecs.ashley.components.SimpleProperty
import jam.core.ChristmasGame
import jam.ecs.components.SantaClaus
import jam.map.ChristmasMapManager
import ktx.ashley.allOf

fun EntityPropertyComponent.getChristmasCheer(): SimpleProperty.FloatProperty {
    return this.props[ChristmasProperty.ChristmasCheer] as SimpleProperty.FloatProperty
}

class GameOverSystem(private val christmasMapManager: ChristmasMapManager, private val mainGame: ChristmasGame): IntervalSystem(1f) {
    private val santaAndHealthFamily = allOf(SantaClaus::class, EntityPropertyComponent::class).get()

    override fun updateInterval() {
        if(christmasMapManager.cities.none { it.needsGifts }) {
            mainGame.gotoGameVictory()
        }

        val santaClaus = engine.getEntitiesFor(santaAndHealthFamily).first()
        if(EntityPropertyComponent.get(santaClaus).getChristmasCheer().current <= 0f) {
            mainGame.goToGameOver()
        }

    }
}