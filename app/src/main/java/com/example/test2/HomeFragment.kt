package com.example.test2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.test2.ui.theme.TestTheme
import androidx.compose.material3.MaterialTheme
import com.example.test2.components.AruncatorAlerte
import com.example.test2.components.RectangleDrawer

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                TestTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                        ShowMeniu(this);
                    }
                }
            }
        }
    }
}

@Composable
fun ShowMeniu(instanceOfMain: ComposeView)
{
    for(i in 1..5)
    {
        RectangleDrawer.DrawOneDraggableRectangle(xFloatPos = MainActivity.getScreenWidthAsFloat()/2+50f, yFloatPos = MainActivity.getScreenHeightAsFloat()/2+100f,
            swipeDistance = 300f, onRectangleSwiped = {})

    }
}
