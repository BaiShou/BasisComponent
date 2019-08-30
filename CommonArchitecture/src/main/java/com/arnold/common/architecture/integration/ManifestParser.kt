package com.arnold.common.architecture.integration

import android.content.Context
import android.content.pm.PackageManager

class ManifestParser(private val context: Context) {

    companion object {
        private val MODULE_VALUE: String
            get() = "ConfigModule"
    }

    fun parse(): MutableList<ConfigModule> {
        val modules = mutableListOf<ConfigModule>()

        val appInfo = context.packageManager.getApplicationInfo(
            context.packageName,
            PackageManager.GET_META_DATA
        )

        if (appInfo.metaData != null) {
            for (key in appInfo.metaData.keySet()) {
                if (MODULE_VALUE == appInfo.metaData.get(key)) {
                    modules.add(parseModule(key))
                }
            }
        }


        return modules
    }


    private fun parseModule(className: String): ConfigModule {
        val clazz: Class<*>
        try {
            clazz = Class.forName(className)
        } catch (e: ClassNotFoundException) {
            throw IllegalArgumentException("Unable to find ConfigModule implementation", e)
        }

        val module: Any
        try {
            module = clazz.newInstance()
        } catch (e: InstantiationException) {
            throw RuntimeException(
                "Unable to instantiate ConfigModule implementation for $clazz",
                e
            )
        } catch (e: IllegalAccessException) {
            throw RuntimeException(
                "Unable to instantiate ConfigModule implementation for $clazz",
                e
            )
        }

        if (module !is ConfigModule) {
            throw RuntimeException("Expected instanceof ConfigModule, but found: $module")
        }
        return module
    }

}