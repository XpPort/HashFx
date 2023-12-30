package ru.fairwol

import javafx.application.Application
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.stage.Stage
import ru.fairwol.ui.HashEncryptionUI
import tornadofx.*
import kotlin.reflect.KClass

class Main : App() {

    companion object {
        private const val debugBox = false

        fun Node.border() {
            style {
                borderColor += box(Color.RED)
                borderWidth += box(1.px)
            }
        }
    }

    override val primaryView: KClass<out UIComponent>
        get() = HashEncryptionUI::class

    override fun start(stage: Stage) {
        super.start(stage)

        val iconStream = javaClass.getResourceAsStream("/image/logo.jpg")
        if (iconStream != null) {
            val image = Image(iconStream)
            stage.icons.add(image)
        }

        if (debugBox) {
            val styleUI = find<HashEncryptionUI>()

            styleUI.root.childrenUnmodifiable.forEach { node ->
                if (node is Label || node is Text || node is HBox) {
                    node.border()
                }
            }
        }
    }
}

fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args)
}