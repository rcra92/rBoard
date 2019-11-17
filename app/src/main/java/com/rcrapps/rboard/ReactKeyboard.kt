package com.rcrapps.rboard

import android.app.Service
import android.content.Context
import android.content.Intent
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.media.AudioManager
import android.os.IBinder
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputConnection

class ReactKeyboard : InputMethodService(), KeyboardView.OnKeyboardActionListener {
    override fun onPress(p0: Int) {
        var ic:InputConnection = getCurrentInputConnection();
        playClick(p0);
        when (p0) {
            Keyboard.KEYCODE_DELETE -> ic.deleteSurroundingText(1, 0);
            Keyboard.KEYCODE_SHIFT -> {
                isCaps = !isCaps;
                keyboard?.setShifted(isCaps);
                kv?.invalidateAllKeys();
            }
            Keyboard.KEYCODE_DONE -> ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            else -> {
                var code: Char = p0.toChar();
                if (Character.isLetter(code) && isCaps) {
                    code = Character.toUpperCase(code);
                    ic.commitText(code.toString(), 1);
                }
            }
        }
    }

    override fun onRelease(i: Int) {
        var ic:InputConnection = getCurrentInputConnection();
        playClick(i);
        when (i) {
            Keyboard.KEYCODE_DELETE -> return
            Keyboard.KEYCODE_SHIFT -> {
                isCaps = !isCaps;
                keyboard?.setShifted(isCaps);
                kv?.invalidateAllKeys();
            }
            Keyboard.KEYCODE_DONE -> ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            else -> {
                var code: Char = i.toChar();
                if (Character.isLetter(code) && isCaps) {
                    code = Character.toUpperCase(code);
                }
                ic.commitText(code.toString(), 1);
            }
        }
    }

    override fun swipeLeft() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun swipeUp() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun swipeDown() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onText(p0: CharSequence?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun swipeRight() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var kv:KeyboardView? = null;
    private var keyboard:Keyboard? = null;

    private var isCaps:Boolean = false;

    override fun onCreate() {
        super.onCreate()
    }

    override fun onCreateInputView(): View {
        var latinKeyboard = Keyboard(this, R.xml.qwerty);
        return layoutInflater.inflate(R.layout.keyboard, null).apply {
            if (this is KeyboardView) {
                setOnKeyboardActionListener(this@ReactKeyboard)
                keyboard = latinKeyboard;
            }
        }
    }

    override fun onKey(i: Int, ints: IntArray?) {
        var ic:InputConnection = getCurrentInputConnection();
        playClick(i);
        when (i) {
            Keyboard.KEYCODE_DELETE -> return;
            Keyboard.KEYCODE_SHIFT -> {
                isCaps = !isCaps;
                keyboard?.setShifted(isCaps);
                kv?.invalidateAllKeys();
            }
            Keyboard.KEYCODE_DONE -> ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            else -> {
                var code: Char = i.toChar();
                if (Character.isLetter(code) && isCaps) {
                    code = Character.toUpperCase(code);
                    ic.commitText(code.toString(), 1);
                }
            }
        }
    }


    private fun playClick(i: Int) {
        var am:AudioManager = getSystemService(AUDIO_SERVICE) as AudioManager;

        when(i) {
            32 -> am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR)
            Keyboard.KEYCODE_DONE -> null
            10 -> am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN)
            Keyboard.KEYCODE_DELETE -> am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE)
            else -> am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD)
        }
    }
}
