package com.aurora.gplayapi.helpers.contracts

import com.aurora.gplayapi.data.models.App

interface AppDetailsContract {
    fun getAppByPackageName(packageName: String): App
    fun getAppByPackageName(packageNameList: List<String>): List<App>
}