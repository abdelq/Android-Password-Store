package dev.msfjarvis.aps.util.activityresult

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.msfjarvis.aps.ui.crypto.GetKeyIdsActivity
import me.msfjarvis.openpgpktx.util.OpenPgpApi

/**
 * Wrapper for GPG key selections to keep UI classes lean.
 */
class GPGKeySelectAction(
    registry: ActivityResultRegistry,
    private val context: Context,
) {
    private val gpgKeyIds = MutableLiveData(emptyArray<String>())

    private val getKey = registry.register(REGISTRY_KEY, StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            result.data?.getStringArrayExtra(OpenPgpApi.EXTRA_KEY_IDS)?.let { keyIds ->
                gpgKeyIds.value = keyIds
            }
        } else {
            throw IllegalStateException("Failed to initialize repository state.")
        }
    }

    fun selectKey(): LiveData<Array<String>> {
        getKey.launch(Intent(context, GetKeyIdsActivity::class.java))
        return gpgKeyIds
    }


    companion object {
        private const val REGISTRY_KEY = "Select GPG Key"
    }
}
