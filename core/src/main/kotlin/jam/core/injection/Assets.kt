package jam.core.injection

import eater.injection.InjectionContext.Companion.inject
import ktx.assets.DisposableContainer
import ktx.assets.DisposableRegistry
import ktx.assets.disposeSafely

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

    override fun dispose() {
        registeredDisposables.disposeSafely()
    }
}