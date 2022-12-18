package jam.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import eater.ecs.ashley.components.TransformComponent
import eater.injection.InjectionContext
import jam.core.ScoreKeeper
import jam.ecs.components.SantaClaus
import jam.map.ChristmasMapManager
import ktx.ashley.allOf

class SantaTargetSystem(private val christmasMapManager: ChristmasMapManager) :
    IteratingSystem(allOf(SantaClaus::class, TransformComponent::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val targetCity = SantaClaus.get(entity).targetCity
        val position = TransformComponent.get(entity).position
        if (!targetCity.needsGifts) {
            ScoreKeeper.difficulty++
            val cityThatNeedsGifts =
                InjectionContext.inject<ChristmasMapManager>().getClosestCityThatNeedsGifts(position)
            if (cityThatNeedsGifts != null) {
                cityThatNeedsGifts.difficulty = ScoreKeeper.difficulty
                christmasMapManager.fixCityDifficulty(cityThatNeedsGifts)
                SantaClaus.get(entity).targetCity = cityThatNeedsGifts
            }
        }
    }
}