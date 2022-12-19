package jam.ecs.systems

import ChristmasProp
import com.badlogic.ashley.systems.IntervalSystem
import eater.ecs.ashley.components.ChristmasPropComponent
import eater.ecs.ashley.components.CoolProp
import jam.core.ChristmasGame
import jam.ecs.components.SantaClaus
import jam.map.ChristmasMapManager
import ktx.ashley.allOf

fun ChristmasPropComponent.getChristmasCheer(): CoolProp.FloatProperty {
    return this.props[ChristmasProp.ChristmasCheer] as CoolProp.FloatProperty
}

class GameOverSystem(private val christmasMapManager: ChristmasMapManager, private val mainGame: ChristmasGame): IntervalSystem(1f) {
    private val santaAndHealthFamily = allOf(SantaClaus::class, ChristmasPropComponent::class).get()

    override fun updateInterval() {
        if(christmasMapManager.cities.none { it.needsGifts }) {
            mainGame.gotoGameVictory()
        }

        val santaClaus = engine.getEntitiesFor(santaAndHealthFamily).first()
        if(ChristmasPropComponent.get(santaClaus).getChristmasCheer().current <= 0f) {
            mainGame.goToGameOver()
        }

    }
}