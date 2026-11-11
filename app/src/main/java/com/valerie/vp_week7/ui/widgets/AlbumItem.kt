package com.valerie.vp_week7.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.valerie.vp_week7.R
import com.valerie.vp_week7.model.Album
import com.valerie.vp_week7.ui.theme.GreenAccent
import com.valerie.vp_week7.ui.theme.TextPrimary
import com.valerie.vp_week7.ui.theme.TextSecondary

/** Kartu album untuk grid diskografi: cover persegi, judul, tahun (hijau) & genre. */
@Composable
fun AlbumCard(album: Album, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
    ) {
        RemoteImage(
            url = album.coverUrl,
            placeholder = album.coverRes ?: R.drawable.album_cover,
            contentDescription = album.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp)),
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = album.title,
            style = MaterialTheme.typography.titleSmall,
            color = TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(4.dp))
        Row(Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
            Text(
                text = album.year.ifEmpty { "—" },
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = GreenAccent,
            )
            Spacer(Modifier.weight(1f))
            Spacer(Modifier.width(8.dp))
            Text(
                text = album.genre,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
