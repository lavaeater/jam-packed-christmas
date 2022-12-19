package jam.core

import com.badlogic.gdx.Application.LOG_INFO
import com.badlogic.gdx.Gdx
import eater.core.MainGame
import eater.injection.InjectionContext.Companion.inject
import eater.screens.MusicVisualizerScreen
import eater.screens.SampleExplorerScreen
import jam.injection.Context
import jam.screens.*
import ktx.async.KtxAsync

class ChristmasGame : MainGame() {
    override fun goToGameSelect() {
        setScreen<GameSelectScreen>()
    }

    override fun create() {
        KtxAsync.initiate()
        Gdx.app.logLevel = LOG_INFO

        Context.initialize(this, false)

        addScreen(inject<SplashScreen>())
        addScreen(inject<GameSelectScreen>())
        addScreen(inject<GameOverScreen>())
        addScreen(inject<GameVictoryScreen>())
        addScreen(inject<ChristmasGameScreen>())
        setScreen<SplashScreen>()
    }


    override fun goToGameScreen() {
        setScreen<ChristmasGameScreen>()
    }

    override fun goToGameOver() {
        setScreen<GameOverScreen>()
    }

    override fun gotoGameVictory() {
        setScreen<GameVictoryScreen>()
    }

    fun goToMusicVisualizer() {
        setScreen<MusicVisualizerScreen>()
    }

    fun goToSampleExplorer() {
        setScreen<SampleExplorerScreen>()
    }
}

