package com.example.consecutivepractices.ui

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.consecutivepractices.R

@Composable
internal fun ProfileAvatar(uri: String?, modifier: Modifier = Modifier) {
    val placeholder = painterResource(R.drawable.ic_profile_placeholder)
    AsyncImage(
        model = uri?.let(Uri::parse),
        contentDescription = "Аватар",
        placeholder = placeholder,
        error = placeholder,
        fallback = placeholder,
        contentScale = ContentScale.Crop,
        modifier = Modifier.size(112.dp).clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .then(modifier)
    )
}
