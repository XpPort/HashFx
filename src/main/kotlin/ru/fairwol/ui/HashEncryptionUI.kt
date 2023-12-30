package ru.fairwol.ui

import at.favre.lib.crypto.bcrypt.BCrypt
import javafx.application.Platform
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.layout.HBox
import tornadofx.*
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess

class HashEncryptionUI : View("HashFX GUI") {

    init {
        primaryStage.isResizable = false
    }

    companion object {
        private const val NAME_DESCRIPTION_TEXT =
            "В меню можно задать текст и получить его зашифрованный (хэш) вид, через алгоритм bCrypt!"

        private const val NAME_BUTTON_HASH = "Вывести хэш"
        private const val NAME_TEXTAREA_PROMPT = "Введите текст"

        private const val NAME_BUTTON_EXIT = "Выйти"

        private const val SALT_BCRYPT = "SaltSecretKey123"
        private var ROUNDS_BCRYPT = 10

        private const val PROMPT_ERROR_BYTE = "Ошибка, вводимый текст не должен превышать 72 байт в кодировке UTF-8"

        private const val MAX_HEIGHT_FORM = 250.0
        private const val MAX_WIDTH_FORM = 500.0
    }

    override val root: Form = form {
        this.setPrefSize(MAX_WIDTH_FORM, MAX_HEIGHT_FORM)

        this.maxWidth = MAX_WIDTH_FORM
        this.maxHeight = MAX_HEIGHT_FORM


        val descriptionTitle: Label = label {
            this.alignment = Pos.TOP_CENTER

            this.isWrapText = true
            this.text = NAME_DESCRIPTION_TEXT
        }

        val textAreaHash: TextArea = textarea {
            this.prefHeight = MAX_WIDTH_FORM / 5
            this.prefWidth = MAX_WIDTH_FORM

            this.translateY = 20.0

            this.promptText = NAME_TEXTAREA_PROMPT
        }

        val buttonRounds: HBox = hbox {
            this.maxWidth = MAX_WIDTH_FORM / 5

            this.translateY = 60.0
            this.translateX = 0.0

            val observableArray = FXCollections.observableArrayList<Int>()
            for (slots in 4..16)
                observableArray.add(slots)

            val cell = SimpleObjectProperty<Int>()

            val comboBox = combobox(cell, observableArray) {
                this.prefWidth = MAX_WIDTH_FORM / 5
                this.prefHeight = 20.0

                selectionModel.selectFirst()
            }

            comboBox.setOnAction {
                if (comboBox.value in 4..15)
                    ROUNDS_BCRYPT = comboBox.value
            }
        }

        val buttonCheckHash: HBox = hbox {
            this.maxWidth = MAX_WIDTH_FORM / 5

            this.translateX = MAX_WIDTH_FORM / 2.7
            this.translateY = MAX_HEIGHT_FORM / 7.8

            this.button(NAME_BUTTON_HASH) {
                this.prefWidth = MAX_WIDTH_FORM / 5
                this.prefHeight = 20.0

                this.style {
                    this.backgroundColor += c("00b800")
                    this.borderRadius += box(15.px)
                }

                this.action {
                    if (textAreaHash.text.isNotEmpty()) {

                        val text: ByteArray = textAreaHash.text.toByteArray(StandardCharsets.UTF_8)

                        if (text.size > 72) {
                            textAreaHash.clear()
                            textAreaHash.promptText = PROMPT_ERROR_BYTE
                        } else {
                            val hashData: String = BCrypt
                                .withDefaults()
                                .hashToString(
                                    ROUNDS_BCRYPT,
                                    textAreaHash.text.toCharArray()
                                )

                            textAreaHash.clear()
                            textAreaHash.promptText = hashData


                            println("Rounds hash $ROUNDS_BCRYPT")
                            println(" ")
                        }
                    } else textAreaHash.promptText = NAME_TEXTAREA_PROMPT
                }
            }
        }

        val buttonExit = hbox {
            this.maxWidth = MAX_WIDTH_FORM / 5

            this.translateY = buttonCheckHash.translateY / 5
            this.translateX = 380.0

            this.button(NAME_BUTTON_EXIT) {
                this.prefWidth = MAX_WIDTH_FORM / 5
                this.prefHeight = 20.0

                this.style {
                    backgroundColor += c("EA7C65")
                    borderRadius += box(15.px)
                }

                this.action {
                    exitProcess(0)
                }
            }
        }


    }

    override fun onDock() {
        super.onDock()

        Platform.runLater {
            root.requestFocus()
        }
    }

}