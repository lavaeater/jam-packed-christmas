package jam.injection

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import eater.injection.InjectionContext.Companion.inject
import jam.core.GameSettings
import ktx.assets.DisposableContainer
import ktx.assets.DisposableRegistry
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile

fun assets(): Assets {
    return inject()
}

class Assets(private val gameSettings: GameSettings) : DisposableRegistry by DisposableContainer() {
//    val buddy: Map<AnimDef, Animation<TextureRegion>> by lazy {
//        val texture = Texture("player/buddy.png".toInternalFile()).alsoRegister()
//        AnimDef.animDefs.associateWith { ad ->
//            Animation(0.1f, *Array(ad.numberOfFrames) { x ->
//                TextureRegion(texture, x * 32, ad.rowIndex * 32, 32, 32)
//            })
//        }
//    }

    val terrainSprite = Sprite(Texture("terrain/terrain.png".toInternalFile()))
    val deerSprite = Sprite(Texture("deer.png".toInternalFile())).apply {
        setOriginCenter()
        scale(gameSettings.MetersPerPixel)
    }
    val sleighSprite = Sprite(Texture("sleigh.png".toInternalFile())).apply {
        setOriginCenter()
        flip(false, false)
        scale(gameSettings.MetersPerPixel)
    }

    override fun dispose() {
        registeredDisposables.disposeSafely()
    }
}