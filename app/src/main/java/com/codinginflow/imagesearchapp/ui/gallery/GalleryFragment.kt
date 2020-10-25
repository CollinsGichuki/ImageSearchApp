package com.codinginflow.imagesearchapp.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.codinginflow.imagesearchapp.R
import com.codinginflow.imagesearchapp.data.UnsplashPhoto
import com.codinginflow.imagesearchapp.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint

//Use the Fragment constructor to inflate the layout iso as to not need to override onCreateView
// We implement the interface from the adapter
@AndroidEntryPoint //Hilt annotation to enable it to work better
class GalleryFragment : Fragment(R.layout.fragment_gallery), UnsplashPhotoAdapter.OnItemClickListener {

    private val viewModel by viewModels<GalleryViewModel>()

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Bind the reference when view is created
        _binding = FragmentGalleryBinding.bind(view)

        //Instantiate the adapter
        //The fragment is the listener for the click event
        val adapter = UnsplashPhotoAdapter(this)

        //Associate the adapter with the recyclerView
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null // remove the recyclerView animations
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                //Set the header and footer
                // The UnsplashPhotoLoadStateAdapter takes in a function property
                // adapter.retry is a function rom the paging adapter that knows how to handle retry loading of another page
                header = UnsplashPhotoLoadStateAdapter { adapter.retry() },
                footer = UnsplashPhotoLoadStateAdapter { adapter.retry() }
            )

            //Retry listener
            buttonRetry.setOnClickListener{
                adapter.retry()
            }
        }

        //Observe the Photos' liveData
        viewModel.photos.observe(viewLifecycleOwner) {
            //submitList is a method in the Paging library
            // pass the lifecycle of the view of the fragment not the fragment instance itself
            // it is the pagingData we get in the lambda
            Log.i("ViewModel", "Observing the data")
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        //loadState object is of type CombinedLoadState
        //It combines the load state of when we are loading new values or appending new data to it
        // We check it to know when to show which views accordingly
        adapter.addLoadStateListener { loadState ->
            binding.apply {
                //Show progress bar when loading new values
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                //Show rv when loading has finished successfully
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                //Show retry when there is an error
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        adapter.itemCount < 1){
                    //No results for the searched query
                    recyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                } else {
                    textViewEmpty.isVisible = false
                }
            }

        }

        //Active the menu item
        setHasOptionsMenu(true)
    }

    // Method in the OnItemClickListener interface
    override fun onItemClick(photo: UnsplashPhoto) {
        //Pass the photo to the details fragment and navigate to the details fragment
        val action = GalleryFragmentDirections.actionGalleryFragmentToDetailsFragment(photo)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_gallery, menu)
        //The search item
        val searchItem = menu.findItem(R.id.action_search)
        // The reference to the search view
        // Cast the searchItem to the same SearchView we used in the xml
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Start the query search
                if (query != null) {
                    //Scroll to the top
                    binding.recyclerView.scrollToPosition(0)
                    viewModel.searchPhotos(query)
                    searchView.clearFocus()//Hide the keyboard when we submit the search
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Do nothing while the user is still typing
                return true
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //destroy they binding
        _binding = null
    }
}