package com.softradix.jetpackcomposedemo.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softradix.jetpackcomposedemo.PostsRepository
import com.softradix.jetpackcomposedemo.data.Posts
import com.softradix.jetpackcomposedemo.data.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(var postsRepository: PostsRepository) : ViewModel() {

    var postMutableLiveData: List<Posts> by mutableStateOf(listOf())

    fun getPostsData(): List<Posts> {
        viewModelScope.launch(Dispatchers.IO) {
            val resource = postsRepository.getPosts()
            resource.let {
                when(it){
                    is Resource.Loading->{

                    }
                    is Resource.Success->{
                        postMutableLiveData = it.data
                    }
                    is Resource.Failure->{
                        it.throwable
                        postMutableLiveData = emptyList()
                    }
                }
            }
        }
            return postMutableLiveData

    }


}