package com.arnold.common.architecture.base.delegate

import android.content.Context
import android.os.Bundle
import android.view.View

interface FragmentDelegate {

    companion object{
        val FRAGMENT_DELEGATE: String
            get() = "FRAGMENT_DELEGATE"
    }



    fun onAttach(context: Context)

    fun onCreate(savedInstanceState: Bundle?)

    fun onCreateView(view: View, savedInstanceState: Bundle?)

    fun onActivityCreate(savedInstanceState: Bundle?)

    fun onStart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onSaveInstanceState(outState: Bundle)

    fun onDestroyView()

    fun onDestroy()

    fun onDetach()

    /**
     * Return true if the fragment is currently added to its activity.
     */
    fun isAdded(): Boolean
}