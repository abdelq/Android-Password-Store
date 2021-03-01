/*
 * Copyright © 2014-2020 The Android Password Store Authors. All Rights Reserved.
 * SPDX-License-Identifier: GPL-3.0-only
 */

package dev.msfjarvis.aps.ui.onboarding.fragments

import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dev.msfjarvis.aps.R
import dev.msfjarvis.aps.data.repo.PasswordRepository
import dev.msfjarvis.aps.databinding.FragmentKeySelectionBinding
import dev.msfjarvis.aps.util.activityresult.GPGKeySelectAction
import dev.msfjarvis.aps.util.extensions.commitChange
import dev.msfjarvis.aps.util.extensions.finish
import dev.msfjarvis.aps.util.extensions.sharedPrefs
import dev.msfjarvis.aps.util.extensions.viewBinding
import dev.msfjarvis.aps.util.settings.PreferenceKeys
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KeySelectionFragment : Fragment(R.layout.fragment_key_selection) {

    private val settings by lazy(LazyThreadSafetyMode.NONE) { requireActivity().applicationContext.sharedPrefs }
    private val binding by viewBinding(FragmentKeySelectionBinding::bind)
    private lateinit var keySelectAction: GPGKeySelectAction

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        keySelectAction = GPGKeySelectAction(requireActivity().activityResultRegistry, requireContext())
        binding.selectKey.setOnClickListener {
            keySelectAction.selectKey().observe(viewLifecycleOwner) { keys ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        val gpgIdentifierFile = File(PasswordRepository.getRepositoryDirectory(), ".gpg-id")
                        gpgIdentifierFile.writeText(keys.joinToString("\n"))
                    }
                    settings.edit { putBoolean(PreferenceKeys.REPOSITORY_INITIALIZED, true) }
                    requireActivity().commitChange(getString(
                        R.string.git_commit_gpg_id,
                        getString(R.string.app_name)
                    ))
                }
                finish()
            }
        }
    }

    companion object {

        fun newInstance() = KeySelectionFragment()
    }
}
