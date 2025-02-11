package org.godotengine.plugin.android.NFCPlugin

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.SignalInfo
import org.godotengine.godot.plugin.UsedByGodot

class GodotAndroidPlugin(godot: Godot) : GodotPlugin(godot), NfcAdapter.ReaderCallback {

    private val activity: Activity? = godot.getActivity()!!
    private var nfcAdapter: NfcAdapter? = null
    private var isReading: Boolean = false  // Tracks NFC reader state



    init {
        activity?.let {
            nfcAdapter = NfcAdapter.getDefaultAdapter(it)
        }
    }

    override fun getPluginName() = BuildConfig.GODOT_PLUGIN_NAME

    override fun getPluginSignals(): Set<SignalInfo> {
        return setOf(SignalInfo("nfc_tag_detected", String::class.java))
    }

    @UsedByGodot
    fun isNfcAvailable(): Boolean {
        return nfcAdapter != null
    }

    @UsedByGodot
    fun getState(): Boolean {
        return isReading
    }

    @UsedByGodot
    fun startNFCReading() {
        activity?.let {
            if (nfcAdapter != null) {
                val options = Bundle()
                options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250)
                nfcAdapter!!.enableReaderMode(
                    it,
                    this,
                    NfcAdapter.FLAG_READER_NFC_A or
                            NfcAdapter.FLAG_READER_NFC_B or
                            NfcAdapter.FLAG_READER_NFC_F or
                            NfcAdapter.FLAG_READER_NFC_V or
                            NfcAdapter.FLAG_READER_NFC_BARCODE or
                            NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS,
                    options
                )
                isReading = true
            }
        }
    }

    override fun onTagDiscovered(tag: Tag) {
        val tagId = bytesToHex(tag.id)
        Log.d("NFCPlugin", "NFC Tag Detected: $tagId")

        activity?.runOnUiThread {
            emitSignal("nfc_tag_detected", tagId)
        }
    }

    @UsedByGodot
    fun stopNFCReading() {
        activity?.let {
            nfcAdapter?.disableReaderMode(it)
        }
        isReading = false
    }

    private fun bytesToHex(bytes: ByteArray): String {
        return bytes.joinToString("") { "%02X".format(it) }
    }
}
