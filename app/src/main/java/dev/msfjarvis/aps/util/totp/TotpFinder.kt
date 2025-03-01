/*
 * Copyright © 2014-2021 The Android Password Store Authors. All Rights Reserved.
 * SPDX-License-Identifier: GPL-3.0-only
 */

package dev.msfjarvis.aps.util.totp

/** Defines a class that can extract relevant parts of a TOTP URL for use by the app. */
interface TotpFinder {

  /** Get the TOTP secret from the given extra content. */
  fun findSecret(content: String): String?

  /** Get the number of digits required in the final OTP. */
  fun findDigits(content: String): String

  /** Get the TOTP timeout period. */
  fun findPeriod(content: String): Long

  /** Get the algorithm for the TOTP secret. */
  fun findAlgorithm(content: String): String
}
