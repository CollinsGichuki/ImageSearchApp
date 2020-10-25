package com.codinginflow.imagesearchapp.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.codinginflow.imagesearchapp.R
import com.codinginflow.imagesearchapp.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint

//Use the Fragment constructor to inflate the layout iso as to not need to override onCreateView
@AndroidEntryPoint //Hilt annotation to enable it to work better
class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    private val viewModel by viewModels<GalleryViewModel>()

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Bind the reference when view is created
        _binding = FragmentGalleryBinding.bind(view)

        //Instantiate the adapter
        val adapter = UnsplashPhotoAdapter()

        //Associate the adapter with the recyclerView
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                //Set the header and footer
                // The UnsplashPhotoLoadStateAdapter takes in a function property
                // adapter.retry is a function rom the paging adapter that knows how to handle retry loading of another page
                header = UnsplashPhotoLoadStateAdapter { adapter.retry() },
                footer = UnsplashPhotoLoadStateAdapter { adapter.retry()}
            )
        }

        //Observe the Photos' liveData
        viewModel.photos.observe(viewLifecycleOwner) {
            //submitList is a method in the Paging library
            // pass the lifecycle of the view of the fragment not the fragment instance itself
            // it is the pagingData we get in the lambda
            Log.i("ViewModel", "Observing the data")
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //destroy they binding
        _binding = null
    }
}