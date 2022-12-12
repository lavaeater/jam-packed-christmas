package jam.injection

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import eater.injection.InjectionContext.Companion.inject
import ktx.assets.DisposableContainer
import ktx.assets.DisposableRegistry
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile

fun assets(): Assets {
    return inject()
}

class Assets : DisposableRegistry by DisposableContainer() {
//    val buddy: Map<AnimDef, Animation<TextureRegion>> by lazy {
//        val texture = Texture("player/buddy.png".toInternalFile()).alsoRegister()
//        AnimDef.animDefs.associateWith { ad ->
//            Animation(0.1f, *Array(ad.numberOfFrames) { x ->
//                TextureRegion(texture, x * 32, ad.rowIndex * 32, 32, 32)
//            })
//        }
//    }

    val terrainTextureRegion = TextureRegion(Texture("terrain/terrain.png".toInternalFile()))
    val sleighTextureRegion = TextureRegion(Texture("sleigh.png".toInternalFile()))

    override fun dispose() {
        registeredDisposables.disposeSafely()
    }
}