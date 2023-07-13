package com.example.nfcreaderappdemo

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.RadioGroup
/*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody
*/



@RequiresApi(Build.VERSION_CODES.KITKAT)
class MainActivity : AppCompatActivity(), NfcAdapter.ReaderCallback {

    private var nfcAdapter: NfcAdapter? = null
    private var selectedTagType: String? = null
    private var selectedLanguageType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        val tagTypeRadioGroup: RadioGroup = findViewById(R.id.tagTypeRadioGroup)
        tagTypeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.tokenRadioButton -> {
                    selectedTagType = "Token"
                    cardRadioButton.isChecked = false
                }
                R.id.cardRadioButton -> {
                    selectedTagType = "Card"
                    tokenRadioButton.isChecked = false
                }
            }
        }


        val languageTypeRadioGroup: RadioGroup = findViewById(R.id.languageTypeRadioGroup)
        languageTypeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedLanguageType = when (checkedId) {
                R.id.frRadioButton -> "Fr"
                R.id.arRadioButton -> "Ar"
                else -> null
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        nfcAdapter?.enableReaderMode(
            this,
            this,
            NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
            null
        )
    }

    public override fun onPause() {
        super.onPause()
        nfcAdapter?.disableReaderMode(this)
    }

    override fun onTagDiscovered(tag: Tag?) {
        if (tag == null) {
            // Handle the case when no tag is detected
            runOnUiThread {
                textView.setText(R.string.no_tag_detected)
            }
            return
        }

        val isoDep = IsoDep.get(tag)
        if (isoDep == null) {
            // Handle the case when IsoDep is not supported by the NFC tag
            runOnUiThread {
                textView.setText(R.string.tag_does_not_support_isodep)
            }
            return
        }

        isoDep.connect()
        val response = isoDep.transceive(Utils.hexStringToByteArray("00A4040007A0000002471001"))
        runOnUiThread {
            val textViewText = StringBuilder()

            if (selectedTagType != null) {
                textViewText.append("Tag Type: $selectedTagType\n")
            }
            if (selectedLanguageType != null) {
                textViewText.append("Language Type: $selectedLanguageType\n")
            }
            textViewText.append("Card Response: ${Utils.toHex(response)}")

            textView.text = textViewText.toString()
        }
        isoDep.close()

        /* runOnUiThread {
            val request = ApiRequest(selectedTagType, selectedLanguageType, Utils.toHex(response))
            val call = ApiManager.apiService.sendData(request)

            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        // Handle successful API response
                        val responseBody = response.body()
                        // TODO: Process the response as needed
                    } else {
                        // Handle API error
                        // TODO: Handle the error case
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Handle API call failure
                    // TODO: Handle the failure case
                }
            })
        } */

    }

}
