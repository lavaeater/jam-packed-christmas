package jam.core

import eater.core.MainGame
import eater.injection.InjectionContext.Companion.inject
import jam.core.injection.Context
import jam.core.screens.SplashScreen
import jam.core.screens.GameOverScreen
import jam.core.screens.GameScreen
import jam.core.screens.GameSelectScreen
import ktx.async.KtxAsync

class ChristmasGame : MainGame() {
    override fun create() {
        KtxAsync.initiate()

        Context.initialize(this)

        addScreen(inject<SplashScreen>())
        addScreen(inject<GameSelectScreen>())
        addScreen(inject<GameOverScreen>())
        addScreen(inject<GameScreen>())
        setScreen<SplashScreen>()
    }

    fun goToGameSelect() {
        setScreen<GameSelectScreen>()
    }

    fun goToGameScreen() {
        setScreen<GameScreen>()
    }

    fun goToGameOver() {
        setScreen<GameOverScreen>()
    }
}

