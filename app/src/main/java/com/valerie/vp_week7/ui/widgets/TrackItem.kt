package com.valerie.vp_week7.ui.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valerie.vp_week7.model.Track
import com.valerie.vp_week7.ui.theme.DividerDark
import com.valerie.vp_week7.ui.theme.GreenAccent
import com.valerie.vp_week7.ui.theme.TextPrimary
import com.valerie.vp_week7.ui.theme.TextSecondary
import java.util.Locale

/** Satu baris di "Daftar Lagu": nomor hijau, judul + genre, durasi dengan ikon jam. */
@Composable
fun TrackRow(track: Track, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = String.format(Locale.US, "%02d", track.number),
                color = GreenAccent,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.width(60.dp),
            )
            Column(Modifier.weight(1f)) {
                Text(
                    text = track.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = track.genre,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                )
            }
            Spacer(Modifier.width(12.dp))
            Icon(
                imageVector = Icons.Outlined.Schedule,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(15.dp),
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = track.durationText,
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary,
            )
        }
        // Garis pemisah dimulai sejajar judul (bukan dari nomor), seperti di desain
        HorizontalDivider(color = DividerDark, thickness = 1.dp, modifier = Modifier.padding(start = 60.dp))
    }
}
