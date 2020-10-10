/*
 * Copyright (C) 2018-2020 University of Waterloo.
 *
 * This file is part of Perses.
 *
 * Perses is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3, or (at your option) any later version.
 *
 * Perses is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Perses; see the file LICENSE.  If not see <http://www.gnu.org/licenses/>.
 */
package org.perses.program

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import org.perses.util.ShellCommandOnPath

abstract class LanguageKind {

  abstract val name: String

  abstract val extensions: ImmutableSet<String>

  abstract val defaultCodeFormatControl: EnumFormatControl

  abstract val allowedCodeFormatControl: ImmutableSet<EnumFormatControl>

  abstract val defaultFormmaterCommand: ShellCommandOnPath?

  fun isCodeFormatAllowed(codeFormat: EnumFormatControl) =
    allowedCodeFormatControl.contains(codeFormat)

  protected fun tryObtainingDefaultFormatter(
    formamtterCmd: String,
    defaultFlags: ImmutableList<String> = ImmutableList.of()
  ) = try {
    ShellCommandOnPath(formamtterCmd, defaultFlags)
  } catch (e: Exception) {
    null
  }
}
