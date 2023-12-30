package ru.fairwol.ui

import at.favre.lib.crypto.bcrypt.BCrypt
import javafx.application.Platform
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import kotlinx.coroutines.*
import ru.fairwol.Main

import tornadofx.*
import java.lang.Thread.sleep
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess

class HashEncryptionUI : View("HashFX GUI") {

    init {
        primaryStage.isResizable = false
    }

    companion object {
        private const val NAME_DESCRIPTION_TEXT: String =
            "В меню можно задать текст и получить его зашифрованный (хэш) вид, через алгоритм bCrypt!"

        private const val NAME_BUTTON_HASH: String = "Вывести хэш"
        private const val NAME_TEXTAREA_PROMPT: String = "Введите текст"

        private const val NAME_BUTTON_EXIT: String = "Выйти"

        private const val SALT_BCRYPT: String = "SaltSecretKey123"
        private var ROUNDS_BCRYPT: Int = 10

        private const val PROMPT_ERROR_BYTE: String =
            "Ошибка, вводимый текст не должен превышать 72 байт в кодировке UTF-8"

        private const val MAX_HEIGHT_FORM: Double = 250.0
        private const val MAX_WIDTH_FORM: Double = 500.0
    }

    var dialogBox = hbox {
        this.alignment = Pos.TOP_CENTER
        this.translateY = 25.0
        this.isVisible = false

        val dialogLabel: Label = label {
            this.text = "Ошибка, отстутствует проверяемый текст!"

            style {
                textFill = c("EA7C65")
            }
        }
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


        add(dialogBox)

        val observableArray: ObservableList<Int> = FXCollections.observableArrayList()
        for (slots in 4..16)
            observableArray.add(slots)

        val cell: SimpleObjectProperty<Int> = SimpleObjectProperty()

        val comboBox: ComboBox<Int> = combobox(cell, observableArray) {
            this.prefWidth = MAX_WIDTH_FORM / 5
            this.prefHeight = 20.0

            this.selectionModel.selectFirst()
        }

        comboBox.setOnAction {
            if (comboBox.value in 4..15)
                ROUNDS_BCRYPT = comboBox.value
        }


        val buttonCheckHash: Button = this.button(NAME_BUTTON_HASH) {
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
                    }
                } else {
                    textAreaHash.promptText = NAME_TEXTAREA_PROMPT

                    GlobalScope.launch {
                        dialogBox.isVisible = true
                        sleep(3000)
                        dialogBox.isVisible = false
                    }


                }
            }
        }


        val buttonExit = this.button(NAME_BUTTON_EXIT) {
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


        val hBoxContainer: HBox = hbox {
            this.alignment = Pos.CENTER

            this.label {
                this.text = "Кол-во итераций:"
            }

            this.spacing = 25.0
            this.translateY = 50.0

            this.add(comboBox)
            this.add(buttonCheckHash)
            this.add(buttonExit)
        }

    }

    override fun onDock() {
        super.onDock()

        Platform.runLater {
            root.requestFocus()
        }
    }

}
