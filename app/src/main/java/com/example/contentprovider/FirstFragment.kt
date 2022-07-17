package com.example.contentprovider

import android.Manifest
import android.R
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.contentprovider.databinding.FragmentFirstBinding
import com.google.android.material.snackbar.Snackbar
import java.net.URI
import java.net.URL

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("Range")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.READ_CONTACTS),1)
        }


        binding.floatingActionButton.setOnClickListener { v->


            if(context?.let {
                    ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.READ_CONTACTS)
                } == PackageManager.PERMISSION_GRANTED) {
                //Content provider
                val conteResolver : ContentResolver? = context?.contentResolver
                val projection = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
                val cursor = conteResolver?.query(
                    ContactsContract.Contacts.CONTENT_URI,projection,null,null,
                    ContactsContract.Contacts.DISPLAY_NAME)
                val columnIndex = ContactsContract.Contacts.DISPLAY_NAME
                val list :ArrayList<String> = ArrayList()
                if(cursor != null) {
                    while (cursor.moveToNext()) {
                        list.add(cursor.getString(cursor.getColumnIndex(columnIndex)))
                    }

                    val adapter = ArrayAdapter(requireContext(), R.layout.simple_list_item_1,list)
                    binding.rvPeople.adapter = adapter
                }
            } else {
                Snackbar.make(view, "Permission needed! ", Snackbar.LENGTH_LONG)
                    .setAction("Access") {
                        if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_CONTACTS)){
                            ActivityCompat.requestPermissions(
                                requireActivity(),
                                arrayOf(Manifest.permission.READ_CONTACTS), 1
                            )
                        } else {
                            val intent = Intent()
                            val url = Uri.fromParts("package",activity?.packageName,null)
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            intent.data = url
                            activity?.startActivity(intent)
                        }

                    }.show()

        } }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}