/*
 * SPDX-FileCopyrightText: 2020 Aurora OSS
 * SPDX-FileCopyrightText: 2023 The Calyx Institute
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.aurora.gplayapi

import java.io.BufferedReader
import java.io.InputStreamReader

object FileManager {
    fun loadPackages(fileName: String?): List<String>? {
        return try {
            val stringList: MutableList<String> = ArrayList()
            val inputStream = javaClass.classLoader.getResourceAsStream(fileName)
            val br = BufferedReader(InputStreamReader(inputStream))
            var strLine: String
            while (br.readLine().also { strLine = it } != null) {
                stringList.add(strLine)
            }
            stringList
        } catch (e: Exception) {
            null
        }
    }
}
