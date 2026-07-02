package com.bmarche.pro.ui.components

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.bmarche.pro.ui.estArabe

/** Applique la langue de l'application (persistée automatiquement). */
fun changerLangue(tag: String) {
    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
}

/**
 * Sélecteur de langue FR / AR en « segmented control » : segment actif rempli en
 * couleur primaire, le tout dans une pilule à liseré fin.
 *
 * @param compact libellés courts (FR / ع) pour les en-têtes ; sinon libellés complets.
 */
@Composable
fun LangueSwitcher(modifier: Modifier = Modifier, compact: Boolean = false) {
    val arabe = estArabe()
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surfaceContainer,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(Modifier.padding(3.dp), verticalAlignment = Alignment.CenterVertically) {
            Segment(
                libelle = if (compact) "FR" else "Français",
                selectionne = !arabe,
                compact = compact
            ) { changerLangue("fr") }
            Segment(
                libelle = if (compact) "ع" else "العربية",
                selectionne = arabe,
                compact = compact
            ) { changerLangue("ar") }
        }
    }
}

@Composable
private fun Segment(
    libelle: String,
    selectionne: Boolean,
    compact: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(
                if (selectionne) MaterialTheme.colorScheme.primary
                else androidx.compose.ui.graphics.Color.Transparent
            )
            .clickable(onClick = onClick)
            .padding(
                horizontal = if (compact) 12.dp else 18.dp,
                vertical = if (compact) 6.dp else 8.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            libelle,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = if (selectionne) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
